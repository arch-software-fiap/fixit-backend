package com.fix_it.infra.controller.dto;

import com.fix_it.core.domain.enums.SituacaoOrdemServico;
import lombok.Data;

@Data
public class AtualizacaoStatusDTO {
    private SituacaoOrdemServico situacao;
}
