package com.fix_it.application.usecasesimpl;

import com.fix_it.core.domain.entity.Servico;
import com.fix_it.usecase.port.ServicoRepository;
import com.fix_it.usecase.servico.AtualizarServicoUseCase;
import com.fix_it.usecase.servico.input.AtualizarServicoInput;
import com.fix_it.usecase.servico.output.ServicoOutput;
import java.util.UUID;

public class AtualizarServicoUseCaseImpl implements AtualizarServicoUseCase {

    private final ServicoRepository repository;

    public AtualizarServicoUseCaseImpl(ServicoRepository repository) {
        this.repository = repository;
    }

    @Override
    public ServicoOutput executar(UUID id, AtualizarServicoInput input) {
        Servico servico = repository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Serviço não encontrado"));

        servico.atualizar(input.nmServico(), input.dsServico(), input.vlPrecoBase());
        Servico salvo = repository.salvar(servico);

        return new ServicoOutput(
            salvo.getId(),
            salvo.getNmServico(),
            salvo.getDsServico(),
            salvo.getVlPrecoBase(),
            salvo.getDthAtualizacao()
        );
    }
}
