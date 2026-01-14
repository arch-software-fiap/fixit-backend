package com.fix_it.usecase.ordemservico.input;

import java.util.List;
import java.util.UUID;

public record CriarOrdemServicoInput(
    UUID clienteId,
    UUID veiculoId,
    String descricao,
    List<ServicoInput> servicos,
    List<PecaInput> pecas
) {}
