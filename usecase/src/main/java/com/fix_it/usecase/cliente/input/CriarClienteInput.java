package com.fix_it.usecase.cliente.input;

public record CriarClienteInput(
        String nome,
        String cpfCnpj,
        String email,
        String telefone
) {
}
