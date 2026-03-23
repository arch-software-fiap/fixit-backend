package com.fix_it.usecase.cliente.input;

import java.time.LocalDate;
import java.util.UUID;

public record AtualizarClienteInput(
        UUID id,
        String nome,
        String cpfCnpj,
        String email,
        String telefone,
        LocalDate dtNascimento
) {
}
