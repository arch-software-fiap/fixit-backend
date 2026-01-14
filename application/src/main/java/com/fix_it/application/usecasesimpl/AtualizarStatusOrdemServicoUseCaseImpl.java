package com.fix_it.application.usecasesimpl;

import com.fix_it.core.domain.entity.OrdemServico;
import com.fix_it.core.domain.enums.SituacaoOrdemServico;
import com.fix_it.usecase.ordemservico.AtualizarStatusOrdemServicoUseCase;
import com.fix_it.usecase.port.OrdemServicoRepository;
import java.util.UUID;

public class AtualizarStatusOrdemServicoUseCaseImpl implements AtualizarStatusOrdemServicoUseCase {

    private final OrdemServicoRepository osRepository;

    public AtualizarStatusOrdemServicoUseCaseImpl(OrdemServicoRepository osRepository) {
        this.osRepository = osRepository;
    }

    @Override
    public void executar(UUID id, SituacaoOrdemServico situacao) {
        OrdemServico os = osRepository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Ordem de Serviço não encontrada"));

        os.alterarSituacao(situacao);

        osRepository.salvar(os);
    }
}
