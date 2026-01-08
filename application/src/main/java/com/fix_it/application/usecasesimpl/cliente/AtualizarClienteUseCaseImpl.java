package com.fix_it.application.usecasesimpl.cliente;

import com.fix_it.core.domain.entity.Cliente;
import com.fix_it.core.domain.exception.ClienteNaoEncontradoException;
import com.fix_it.usecase.cliente.AtualizarClienteUseCase;
import com.fix_it.usecase.port.ClienteRepository;
import com.fix_it.usecase.port.DocumentoValidator;

import java.util.Objects;
import java.util.UUID;

public class AtualizarClienteUseCaseImpl implements AtualizarClienteUseCase {

    private final ClienteRepository clienteRepository;
    private final DocumentoValidator documentoValidator;

    public AtualizarClienteUseCaseImpl(ClienteRepository clienteRepository,
                                       DocumentoValidator documentoValidator) {
        this.clienteRepository = Objects.requireNonNull(clienteRepository);
        this.documentoValidator = Objects.requireNonNull(documentoValidator);
    }

    @Override
    public Cliente execute(UUID id, String nome, String cpfCnpj, String email, String telefone) {
        Cliente existente = clienteRepository.buscarPorId(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException(id));

        if (!cpfCnpj.equals(existente.getCpfCnpj())) {
            documentoValidator.validarCpfOuCnpj(cpfCnpj);
        }

        existente.atualizarDados(nome, cpfCnpj, email, telefone);
        return clienteRepository.salvar(existente);
    }
}
