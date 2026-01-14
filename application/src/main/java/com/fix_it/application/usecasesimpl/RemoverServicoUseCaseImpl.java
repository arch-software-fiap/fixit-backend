package com.fix_it.application.usecasesimpl;

import com.fix_it.usecase.port.ServicoRepository;
import com.fix_it.usecase.servico.RemoverServicoUseCase;
import java.util.UUID;

public class RemoverServicoUseCaseImpl implements RemoverServicoUseCase {

    private final ServicoRepository repository;

    public RemoverServicoUseCaseImpl(ServicoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void executar(UUID id) {
        if (!repository.existePorId(id)) {
            throw new IllegalArgumentException("Serviço não encontrado");
        }
        repository.removerPorId(id);
    }
}
