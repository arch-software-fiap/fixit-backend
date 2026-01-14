package com.fix_it.application.usecasesimpl;

import com.fix_it.core.domain.entity.OrdemServico;
import com.fix_it.core.domain.enums.SituacaoOrdemServico;
import com.fix_it.usecase.ordemservico.ConsultarStatusOrdemServicoUseCase;
import com.fix_it.usecase.port.OrdemServicoRepository;
import java.util.UUID;

public class ConsultarStatusOrdemServicoUseCaseImpl implements ConsultarStatusOrdemServicoUseCase {

    private final OrdemServicoRepository osRepository;

    public ConsultarStatusOrdemServicoUseCaseImpl(OrdemServicoRepository osRepository) {
        this.osRepository = osRepository;
    }

    @Override
    public SituacaoOrdemServico executar(UUID id) {
        OrdemServico os = osRepository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Ordem de Serviço não encontrada"));
        return os.getSituacao();
    }
}
