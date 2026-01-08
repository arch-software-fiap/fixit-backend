package com.fix_it.core.domain.entity;

import com.fix_it.core.domain.enums.TipoItemEstoque;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemEstoque {

    private UUID id;

    private String nmItemEstoque;

    private String dsItemEstoque;

    private Long vlPrecoUnitario;

    private Integer qtEstoqueAtual;

    private Integer qtEstoqueMinimo;

    private LocalDateTime dthAtualizacao;

    private TipoItemEstoque tipo;

    private List<MovimentoEstoque> movimentosEstoque = new ArrayList<>();

    private List<ItemPecaOS> itensPecaOS = new ArrayList<>();

    private Boolean abaixoMinimo;

}

