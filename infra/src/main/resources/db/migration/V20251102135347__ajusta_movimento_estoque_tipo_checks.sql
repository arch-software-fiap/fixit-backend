ALTER TABLE movimento_estoque
ALTER
COLUMN nm_tipo_movimento TYPE varchar(20),
  ALTER
COLUMN nm_tipo_movimento SET NOT NULL;

ALTER TABLE movimento_estoque
    ADD CONSTRAINT ck_movimento_estoque_tipo
        CHECK (nm_tipo_movimento IN ('ENTRADA', 'SAIDA', 'AJUSTE'));

ALTER TABLE movimento_estoque
    ADD CONSTRAINT ck_movimento_estoque_qt_mov_pos
        CHECK (qt_mov IS NULL OR qt_mov > 0);
