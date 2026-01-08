package com.fix_it.app.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record ItemPecaDTO(
        @JsonProperty("item_estoque_id") @NotNull UUID itemEstoqueId,
        @Positive @NotNull Integer quantidade,
        String descricao
) {}
