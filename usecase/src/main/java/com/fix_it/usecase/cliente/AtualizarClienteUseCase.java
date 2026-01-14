package com.fix_it.usecase.cliente;

import com.fix_it.core.domain.entity.Cliente;
import com.fix_it.usecase.cliente.input.AtualizarClienteInput;

public interface AtualizarClienteUseCase {

    Cliente execute(AtualizarClienteInput input);
}
