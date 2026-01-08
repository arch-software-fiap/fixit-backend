ALTER TABLE item_estoque
    ADD COLUMN IF NOT EXISTS st_abaixo_minimo boolean
    GENERATED ALWAYS AS (qt_estoque_atual < qt_estoque_minimo) STORED;

CREATE INDEX IF NOT EXISTS ix_item_estoque_abaixo_minimo_true
    ON item_estoque (st_abaixo_minimo)
    WHERE st_abaixo_minimo = true;
