package com.fix_it.core.domain.exception;

import java.util.UUID;

public class ClienteNaoEncontradoException extends RuntimeException {

    public ClienteNaoEncontradoException(UUID id) {
        super("Cliente não encontrado com id " + id);
    }

    public ClienteNaoEncontradoException(String documento) {
        super("Cliente não encontrado com documento " + documento);
    }
}
