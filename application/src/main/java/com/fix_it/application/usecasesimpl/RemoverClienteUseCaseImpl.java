package com.fix_it.application.usecasesimpl;

import com.fix_it.core.domain.exception.ClienteNaoEncontradoException;
import com.fix_it.usecase.cliente.RemoverClienteUseCase;
import com.fix_it.usecase.port.ClienteRepository;

import java.util.Objects;
import java.util.UUID;

public class RemoverClienteUseCaseImpl implements RemoverClienteUseCase {

    private final ClienteRepository repository;

    public RemoverClienteUseCaseImpl(ClienteRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public void execute(UUID id) {
        boolean existe = repository.buscarPorId(id).isPresent();
        if (!existe) {
            throw new ClienteNaoEncontradoException(id);
        }
        repository.removerPorId(id);
    }
}
