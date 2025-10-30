package com.fix_it.app.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public record ItemPecaDTO(
        @NotBlank String nome,
        String descricao,
        @Positive Integer quantidade,
        @PositiveOrZero Long valorUnitario,
        @NotNull UUID itemEstoqueId) {
}
