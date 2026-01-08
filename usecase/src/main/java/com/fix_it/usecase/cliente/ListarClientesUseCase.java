package com.fix_it.usecase.cliente;

import com.fix_it.core.domain.entity.Cliente;

import java.util.List;

public interface ListarClientesUseCase {
    List<Cliente> execute();
}
