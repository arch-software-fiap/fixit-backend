package com.fix_it.application.usecasesimpl;

import com.fix_it.core.domain.entity.Cliente;
import com.fix_it.usecase.cliente.ListarClientesUseCase;
import com.fix_it.usecase.port.ClienteRepository;

import java.util.List;
import java.util.Objects;

public class ListarClientesUseCaseImpl implements ListarClientesUseCase {

    private final ClienteRepository repository;

    public ListarClientesUseCaseImpl(ClienteRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public List<Cliente> execute() {
        return repository.listarTodos();
    }
}
