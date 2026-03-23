package com.fix_it.infra.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ClienteResponse(
        UUID id,
        String nome,
        String cpfCnpj,
        String email,
        String telefone,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dtNascimento,
        LocalDate dtCadastro,
        LocalDateTime dthAtualizacao
) {}
