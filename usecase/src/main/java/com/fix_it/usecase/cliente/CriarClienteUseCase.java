package com.fix_it.usecase.cliente;

import com.fix_it.core.domain.entity.Cliente;

public interface CriarClienteUseCase {

    Cliente execute(String nome, String cpfCnpj, String email, String telefone);
}
