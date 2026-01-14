package com.fix_it.usecase.ordemservico.input;

import java.util.UUID;

public record AdicionarItemPecaInput(
    UUID itemEstoqueId,
    String nome,
    String descricao,
    Integer quantidade,
    Long valorUnitario
) {}
