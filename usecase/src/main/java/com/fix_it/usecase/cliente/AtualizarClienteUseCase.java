package com.fix_it.usecase.cliente;

import com.fix_it.core.domain.entity.Cliente;

import java.util.UUID;

public interface AtualizarClienteUseCase {

    Cliente execute(UUID id, String nome, String cpfCnpj, String email, String telefone);
}
