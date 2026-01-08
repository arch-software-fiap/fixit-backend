package com.fix_it.app.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

@JsonNaming
public record ItemServicoDTO(
        @JsonProperty("id")
        UUID id,
        @NotNull
        @JsonProperty("quantidade_horas")
        @PositiveOrZero
        Integer quantidadeHoras,
        @NotNull
        @JsonProperty("servico_id")
        UUID seq_servico) {
}
