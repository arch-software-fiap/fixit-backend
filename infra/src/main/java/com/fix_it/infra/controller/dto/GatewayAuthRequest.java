package com.fix_it.infra.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record GatewayAuthRequest(
        @NotBlank(message = "CPF é obrigatório")
        String cpf,
        @NotBlank(message = "Data de nascimento é obrigatória")
        @Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}", message = "Data de nascimento deve estar no formato dd/MM/yyyy")
        String dataNascimento
) {
}
