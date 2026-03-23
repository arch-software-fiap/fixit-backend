package com.fix_it.infra.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ClienteRequest(

        @NotBlank
        @Size(max = 255)
        String nome,

        @NotBlank
        @Size(max = 18)
        String cpfCnpj,

        @Email
        @Size(max = 255)
        String email,

        @Size(max = 20)
        String telefone,

        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dtNascimento
) {}
