package com.fix_it.core.domain.entity;

import com.fix_it.core.domain.enums.TipoMovimentoEstoque;

import java.time.LocalDateTime;
import java.util.UUID;

public class MovimentoEstoque {

    private UUID id;

    private LocalDateTime dthMovimento;

    private TipoMovimentoEstoque tipo;

    private Integer quantidade;

    private String descricao;

    private ItemEstoque itemEstoque;

    private ItemPecaOS peca;

    public void prePersist() {
        dthMovimento = LocalDateTime.now();
    }
}

