package com.fix_it.application.usecasesimpl;

import com.fix_it.core.domain.entity.OrdemServico;
import com.fix_it.core.domain.enums.SituacaoOrdemServico;
import com.fix_it.usecase.ordemservico.AprovarOrcamentoUseCase;
import com.fix_it.usecase.port.OrdemServicoRepository;
import java.util.UUID;

public class AprovarOrcamentoUseCaseImpl implements AprovarOrcamentoUseCase {

    private final OrdemServicoRepository osRepository;

    public AprovarOrcamentoUseCaseImpl(OrdemServicoRepository osRepository) {
        this.osRepository = osRepository;
    }

    @Override
    public void executar(UUID id, boolean aprovado) {
        OrdemServico os = osRepository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Ordem de Serviço não encontrada"));

        if (aprovado) {
            os.alterarSituacao(SituacaoOrdemServico.EM_EXECUCAO);
        } else {
            os.alterarSituacao(SituacaoOrdemServico.FINALIZADA);
        }

        osRepository.salvar(os);
    }
}
