package com.fix_it.application.usecasesimpl;

import com.fix_it.core.domain.entity.Cliente;
import com.fix_it.core.domain.exception.ClienteNaoEncontradoException;
import com.fix_it.usecase.cliente.AtualizarClienteUseCase;
import com.fix_it.usecase.cliente.input.AtualizarClienteInput;
import com.fix_it.usecase.port.ClienteRepository;
import com.fix_it.usecase.port.DocumentoValidator;

import java.time.LocalDate;
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
    public Cliente execute(AtualizarClienteInput atualizarClienteInput) {

        UUID id = atualizarClienteInput.id();

        String cpfCnpj = atualizarClienteInput.cpfCnpj();
        String nome = atualizarClienteInput.nome();
        String email = atualizarClienteInput.email();
        String telefone = atualizarClienteInput.telefone();
        LocalDate dtNascimento = atualizarClienteInput.dtNascimento();

        Cliente existente = clienteRepository.buscarPorId(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException(id));

        if (!cpfCnpj.equals(existente.getCpfCnpj())) {
            documentoValidator.validarCpfOuCnpj(cpfCnpj);
        }

        existente.atualizarDados(nome, cpfCnpj, email, telefone, dtNascimento);
        return clienteRepository.salvar(existente);
    }
}
