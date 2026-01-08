package com.fix_it.usecase.cliente;

import com.fix_it.core.domain.entity.Cliente;

import java.util.UUID;

public interface BuscarClientePorIdUseCase {
    Cliente execute(UUID id);
}
