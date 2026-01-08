package com.fix_it.controller.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ClienteResponse(
        UUID id,
        String nome,
        String cpfCnpj,
        String email,
        String telefone,
        LocalDate dtCadastro,
        LocalDateTime dthAtualizacao
) {}
