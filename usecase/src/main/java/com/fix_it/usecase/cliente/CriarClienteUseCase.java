package com.fix_it.usecase.cliente;

import com.fix_it.core.domain.entity.Cliente;
import com.fix_it.usecase.cliente.input.CriarClienteInput;

public interface CriarClienteUseCase {

    Cliente execute(CriarClienteInput input);
}
