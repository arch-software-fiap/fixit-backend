package com.fix_it.application.usecasesimpl;

import com.fix_it.core.domain.entity.Cliente;
import com.fix_it.usecase.cliente.CriarClienteUseCase;
import com.fix_it.usecase.cliente.input.CriarClienteInput;
import com.fix_it.usecase.port.ClienteRepository;
import com.fix_it.usecase.port.DocumentoValidator;

public class CriarClienteUseCaseImpl implements CriarClienteUseCase {

    private final ClienteRepository repository;
    private final DocumentoValidator validator;

    public CriarClienteUseCaseImpl(ClienteRepository repository, DocumentoValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public Cliente execute(CriarClienteInput input) {
        if (repository.existePorCpfCnpj(input.cpfCnpj())) {
            throw new IllegalArgumentException("Cliente já existe com este número de documento " + input.cpfCnpj());
        }

        validator.validarCpfOuCnpj(input.cpfCnpj());

        return repository.salvar(Cliente.novo(
                input.nome(),
                input.cpfCnpj(),
                input.email(),
                input.telefone(),
                input.dtNascimento()
        ));
    }
}
