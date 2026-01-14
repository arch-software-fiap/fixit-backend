package com.fix_it.usecase.ordemservico.input;

import java.util.UUID;

public record AdicionarItemServicoInput(
    UUID servicoId,
    Integer quantidadeHoras,
    Long valorUnitario
) {}
