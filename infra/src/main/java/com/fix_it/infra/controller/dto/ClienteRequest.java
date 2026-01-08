package com.fix_it.infra.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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
        String telefone
) {}
