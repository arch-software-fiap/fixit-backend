package com.fix_it.usecase.cliente.input;

import java.time.LocalDate;

public record CriarClienteInput(
        String nome,
        String cpfCnpj,
        String email,
        String telefone,
        LocalDate dtNascimento
) {
}
