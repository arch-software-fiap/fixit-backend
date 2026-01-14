package com.fix_it.core.domain.exception;

public class ClienteJaExisteException extends RuntimeException {
    public ClienteJaExisteException(String documento) {
        super("Cliente já existe com este número de documento " + documento);
    }
}
