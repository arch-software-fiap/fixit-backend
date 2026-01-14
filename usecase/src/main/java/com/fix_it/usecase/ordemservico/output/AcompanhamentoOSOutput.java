package com.fix_it.usecase.ordemservico.output;

import com.fix_it.core.domain.enums.SituacaoOrdemServico;
import java.time.LocalDateTime;
import java.util.UUID;

public record AcompanhamentoOSOutput(
    UUID id,
    String nomeCliente,
    String cpfCnpjCliente,
    String placaVeiculo,
    SituacaoOrdemServico situacao,
    Long valorOrcamentoTotal,
    Long valorTotalFinal,
    LocalDateTime dataAberturaEm,
    LocalDateTime dataFechamentoEm
) {}
