package com.fix_it.app.common.dto;

import com.fix_it.app.model.enums.SituacaoOrdemServico;

import java.time.LocalDateTime;
import java.util.UUID;

public record AcompanhamentoOSDTO(
        UUID osId,
        String cliente,
        String cpfCnpj,
        String veiculoPlaca,
        SituacaoOrdemServico situacao,
        Long valorOrcamentoTotal,
        Long valorTotalFinal,
        LocalDateTime dataAberturaEm,
        LocalDateTime dataFechamentoEm
) {
}
