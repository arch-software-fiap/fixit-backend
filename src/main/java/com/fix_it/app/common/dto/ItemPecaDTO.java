package com.fix_it.app.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public record ItemPecaDTO(
        @NotBlank String nome,
        String descricao,
        @Positive Integer quantidade,
        @JsonProperty("valor_unitario")
        @PositiveOrZero Long valorUnitario,
        @JsonProperty("item_estoque_id")
        @NotNull UUID itemEstoqueId) {
}
