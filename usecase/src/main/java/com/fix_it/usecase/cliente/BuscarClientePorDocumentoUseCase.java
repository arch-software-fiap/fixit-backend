package com.fix_it.usecase.cliente;

import com.fix_it.core.domain.entity.Cliente;

public interface BuscarClientePorDocumentoUseCase {
    Cliente execute(String cpfCnpj);
}
