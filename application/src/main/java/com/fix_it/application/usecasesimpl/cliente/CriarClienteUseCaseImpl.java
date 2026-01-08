package com.fix_it.application.usecasesimpl.cliente;

import com.fix_it.core.domain.entity.Cliente;
import com.fix_it.core.domain.exception.ClienteJaExisteException;
import com.fix_it.usecase.cliente.CriarClienteUseCase;
import com.fix_it.usecase.port.ClienteRepository;
import com.fix_it.usecase.port.DocumentoValidator;

import java.util.Objects;

public class CriarClienteUseCaseImpl implements CriarClienteUseCase {

    private final ClienteRepository clienteRepository;
    private final DocumentoValidator documentoValidator;

    public CriarClienteUseCaseImpl(ClienteRepository clienteRepository,
                                   DocumentoValidator documentoValidator) {
        this.clienteRepository = Objects.requireNonNull(clienteRepository);
        this.documentoValidator = Objects.requireNonNull(documentoValidator);
    }

    @Override
    public Cliente execute(String nome, String cpfCnpj, String email, String telefone) {
        validarClienteNaoExiste(cpfCnpj);
        documentoValidator.validarCpfOuCnpj(cpfCnpj);

        Cliente cliente = Cliente.novo(nome, cpfCnpj, email, telefone);
        return clienteRepository.salvar(cliente);
    }

    private void validarClienteNaoExiste(String documento) {
        boolean existe = clienteRepository.existePorCpfCnpj(documento);
        if (existe) throw new ClienteJaExisteException(documento);
    }
}
