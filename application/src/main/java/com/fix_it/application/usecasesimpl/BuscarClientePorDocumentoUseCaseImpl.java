package com.fix_it.application.usecasesimpl;

import com.fix_it.core.domain.entity.Cliente;
import com.fix_it.core.domain.exception.ClienteNaoEncontradoException;
import com.fix_it.usecase.cliente.BuscarClientePorDocumentoUseCase;
import com.fix_it.usecase.port.ClienteRepository;

import java.util.Objects;

public class BuscarClientePorDocumentoUseCaseImpl implements BuscarClientePorDocumentoUseCase {

    private final ClienteRepository repository;

    public BuscarClientePorDocumentoUseCaseImpl(ClienteRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public Cliente execute(String cpfCnpj) {
        return repository.buscarPorCpfCnpj(cpfCnpj)
                .orElseThrow(() -> new ClienteNaoEncontradoException(cpfCnpj));
    }
}
