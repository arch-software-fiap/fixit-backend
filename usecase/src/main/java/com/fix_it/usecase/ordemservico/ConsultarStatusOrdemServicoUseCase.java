package com.fix_it.usecase.ordemservico;

import com.fix_it.core.domain.enums.SituacaoOrdemServico;
import java.util.UUID;

public interface ConsultarStatusOrdemServicoUseCase {
    SituacaoOrdemServico executar(UUID id);
}
