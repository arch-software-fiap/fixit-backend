CREATE
EXTENSION IF NOT EXISTS pgcrypto;

WITH seed(nm, ds, preco) AS (VALUES ('Revisão básica',
                                     'Checklist de 50 itens, troca de filtro de ar e verificação de fluidos.', 25000),
                                    ('Revisão completa',
                                     'Checklist de 100 itens, troca de filtros (ar/óleo/combustível) e fluídos.',
                                     52000),
                                    ('Troca de óleo', 'Troca de óleo do motor e reaperto geral.', 12000),
                                    ('Troca de filtro de óleo', 'Substituição do filtro de óleo do motor.', 4500),
                                    ('Alinhamento e balanceamento',
                                     'Correção de convergência e balanceamento das quatro rodas.', 18000),
                                    ('Geometria completa', 'Caster, camber e convergência com relatório.', 26000),
                                    ('Troca de pastilhas de freio',
                                     'Substituição do jogo dianteiro com inspeção dos discos.', 30000),
                                    ('Sangria do sistema de freio',
                                     'Substituição do fluido e retirada de ar do sistema.', 19000),
                                    ('Higienização do ar-condicionado',
                                     'Limpeza do sistema e troca do filtro de cabine.', 22000),
                                    ('Diagnóstico eletrônico', 'Leitura e limpeza de códigos de falha (scanner).',
                                     15000),
                                    ('Lavagem simples', 'Lavagem externa rápida com secagem.', 7000),
                                    ('Lavagem completa', 'Externa + interna, aspiração e acabamento.', 12000),
                                    ('Polimento técnico', 'Correção de micro-riscos e brilho.', 35000),
                                    ('Cristalização de pintura', 'Proteção com selante sintético.', 28000),
                                    ('Descarbonização de TBI', 'Limpeza do corpo de borboleta e regulagem lenta.',
                                     21000),
                                    ('Troca de correia dentada', 'Substituição da correia e inspeção de tensionadores.',
                                     48000),
                                    ('Troca de embreagem', 'Substituição do kit (platô, disco e rolamento).', 89000),
                                    ('Flush do arrefecimento', 'Limpeza do sistema e troca do fluido de radiador.',
                                     23000))
INSERT
INTO servico (sq_servico, nm_servico, ds_servico, vl_preco_base, dth_atualizacao)
SELECT gen_random_uuid(), s.nm, s.ds, s.preco, now()
FROM seed s
         LEFT JOIN servico t ON t.nm_servico = s.nm
WHERE t.sq_servico IS NULL;
