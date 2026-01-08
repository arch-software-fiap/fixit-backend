package com.fix_it.usecase.cliente.output;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ClienteOutput(
        UUID id,
        String nome,
        String cpfCnpj,
        String email,
        String telefone,
        LocalDate dtCadastro,
        LocalDateTime dthAtualizacao
) {
}
