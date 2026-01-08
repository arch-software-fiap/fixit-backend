package com.fix_it.application.usecasesimpl;

import com.fix_it.core.domain.entity.Servico;
import com.fix_it.usecase.port.ServicoRepository;
import com.fix_it.usecase.servico.BuscarServicoPorIdUseCase;
import com.fix_it.usecase.servico.output.ServicoOutput;
import java.util.UUID;

public class BuscarServicoPorIdUseCaseImpl implements BuscarServicoPorIdUseCase {

    private final ServicoRepository repository;

    public BuscarServicoPorIdUseCaseImpl(ServicoRepository repository) {
        this.repository = repository;
    }

    @Override
    public ServicoOutput executar(UUID id) {
        Servico servico = repository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Serviço não encontrado"));

        return new ServicoOutput(
            servico.getId(),
            servico.getNmServico(),
            servico.getDsServico(),
            servico.getVlPrecoBase(),
            servico.getDthAtualizacao()
        );
    }
}
