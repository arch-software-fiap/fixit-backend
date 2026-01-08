package com.fix_it.application.usecasesimpl;

import com.fix_it.core.domain.entity.OrdemServico;
import com.fix_it.usecase.ordemservico.BuscarOrdemServicoPorIdUseCase;
import com.fix_it.usecase.ordemservico.output.OrdemServicoOutput;
import com.fix_it.usecase.port.OrdemServicoRepository;
import java.util.UUID;

public class BuscarOrdemServicoPorIdUseCaseImpl implements BuscarOrdemServicoPorIdUseCase {

    private final OrdemServicoRepository repository;

    public BuscarOrdemServicoPorIdUseCaseImpl(OrdemServicoRepository repository) {
        this.repository = repository;
    }

    @Override
    public OrdemServicoOutput executar(UUID id) {
        OrdemServico os = repository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Ordem de serviço não encontrada"));

        return new OrdemServicoOutput(
            os.getId(),
            os.getSituacao(),
            os.getDescricao(),
            os.getDataAberturaEm(),
            os.getDataFechamentoEm(),
            os.getValorOrcamentoTotal(),
            os.getValorTotalFinal(),
            os.getCliente().getId(),
            os.getVeiculo().getId()
        );
    }
}
