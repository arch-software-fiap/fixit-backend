package com.fix_it.usecase.ordemservico.output;

import com.fix_it.core.domain.enums.SituacaoOrdemServico;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrdemServicoOutput(
    UUID id,
    SituacaoOrdemServico situacao,
    String descricao,
    LocalDateTime dataAberturaEm,
    LocalDateTime dataFechamentoEm,
    Long valorOrcamentoTotal,
    Long valorTotalFinal,
    UUID clienteId,
    UUID veiculoId
) {}
