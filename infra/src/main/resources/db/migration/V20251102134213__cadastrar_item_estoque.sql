CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE UNIQUE INDEX IF NOT EXISTS ux_item_estoque_nm_tipo
    ON item_estoque (nm_item_estoque, tp_item);

WITH seed(nm, ds, preco, qt_atual, qt_min, tipo) AS (
    VALUES
        -- ---------- PEÇAS ----------
        ('Pastilha de freio dianteira', 'Jogo dianteiro; conferir espessura do disco.', 12900, 20, 5, 'PECA'),
        ('Disco de freio dianteiro', 'Par ventilado; torque conforme manual.', 35900, 8, 2, 'PECA'),
        ('Correia dentada', 'Correia sincronizadora; verificar tensionador.', 18900, 15, 3, 'PECA'),
        ('Filtro de ar do motor', 'Elemento filtrante; trocar a cada 10k km.', 6900, 30, 8, 'PECA'),
        ('Filtro de óleo', 'Compatível com linha popular 1.0/1.6.', 4900, 40, 10, 'PECA'),
        ('Velas de ignição (jogo)', 'Jogo com 4 unidades; folga padrão.', 9900, 25, 6, 'PECA'),
        ('Kit embreagem', 'Platô, disco e rolamento; conferir aplicação.', 89900, 6, 2, 'PECA'),

        -- ---------- INSUMOS ----------
        ('Óleo do motor 5W30 sintético (1L)', 'API SN/ILSAC GF-5.', 4590, 120, 30, 'INSUMO'),
        ('Fluido de freio DOT 4 (500ml)', 'Ponto de ebulição elevado.', 3490, 60, 12, 'INSUMO'),
        ('Aditivo para radiador (1L)', 'Pronto uso; longa duração.', 2990, 50, 10, 'INSUMO'),
        ('Graxa multiuso (500g)', 'Resistente à água e alta pressão.', 2790, 25, 5, 'INSUMO'),
        ('Shampoo automotivo (1L)', 'Alto rendimento; pH balanceado.', 2490, 40, 10, 'INSUMO'),
        ('Desengraxante (1L)', 'Uso profissional; biodegradável.', 2590, 35, 8, 'INSUMO'),
        ('Pano de microfibra', 'Não risca; alta absorção.', 1590, 100, 20, 'INSUMO'),
        ('Líquido limpa para-brisa (500ml)', 'Pronto uso; evita manchas.', 1990, 60, 12, 'INSUMO')
)
INSERT INTO item_estoque (
  sq_item_estoque, nm_item_estoque, ds_item_estoque,
  vl_preco_unitario, qt_estoque_atual, qt_estoque_minimo,
  tp_item, dth_atualizacao
)
SELECT
    gen_random_uuid(), s.nm, s.ds,
    s.preco, s.qt_atual, s.qt_min,
    s.tipo, now()
FROM seed s
         LEFT JOIN item_estoque t
                   ON t.nm_item_estoque = s.nm
                       AND t.tp_item = s.tipo
WHERE t.sq_item_estoque IS NULL;
