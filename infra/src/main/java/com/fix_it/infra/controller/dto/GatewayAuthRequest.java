package com.fix_it.infra.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record GatewayAuthRequest(
        @NotBlank(message = "CPF é obrigatório")
        String cpf
) {
}
