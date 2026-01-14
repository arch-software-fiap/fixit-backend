package com.fix_it.usecase.ordemservico;

import com.fix_it.core.domain.enums.SituacaoOrdemServico;
import java.util.UUID;

public interface AtualizarStatusOrdemServicoUseCase {
    void executar(UUID id, SituacaoOrdemServico situacao);
}
