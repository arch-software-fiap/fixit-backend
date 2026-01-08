package com.fix_it.usecase.ordemservico.input;

import java.util.UUID;

public record CriarOrdemServicoInput(
    UUID clienteId,
    UUID veiculoId,
    String descricao
) {}
