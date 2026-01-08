package com.fix_it.application.usecasesimpl;

import com.fix_it.core.domain.entity.Servico;
import com.fix_it.usecase.port.ServicoRepository;
import com.fix_it.usecase.servico.CriarServicoUseCase;
import com.fix_it.usecase.servico.input.CriarServicoInput;
import com.fix_it.usecase.servico.output.ServicoOutput;

public class CriarServicoUseCaseImpl implements CriarServicoUseCase {

    private final ServicoRepository repository;

    public CriarServicoUseCaseImpl(ServicoRepository repository) {
        this.repository = repository;
    }

    @Override
    public ServicoOutput executar(CriarServicoInput input) {
        Servico servico = Servico.novo(input.nmServico(), input.dsServico(), input.vlPrecoBase());
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
