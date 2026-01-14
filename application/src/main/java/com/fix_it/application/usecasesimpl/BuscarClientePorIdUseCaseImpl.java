package com.fix_it.application.usecasesimpl;

import com.fix_it.core.domain.entity.Cliente;
import com.fix_it.core.domain.exception.ClienteNaoEncontradoException;
import com.fix_it.usecase.cliente.BuscarClientePorIdUseCase;
import com.fix_it.usecase.port.ClienteRepository;

import java.util.Objects;
import java.util.UUID;

public class BuscarClientePorIdUseCaseImpl implements BuscarClientePorIdUseCase {

    private final ClienteRepository repository;

    public BuscarClientePorIdUseCaseImpl(ClienteRepository repo) {
        this.repository = Objects.requireNonNull(repo);
    }

    @Override
    public Cliente execute(UUID id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException(id));
    }
}
