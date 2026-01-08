package com.fix_it.app.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OrdemServicoDTO(

        @JsonView({Views.Create.class, Views.Update.class})
        @JsonProperty("cliente_id")
        @NotNull UUID clienteId,

        @JsonView({Views.Create.class, Views.Update.class})
        @JsonProperty("veiculo_id")
        @NotNull UUID veiculoId,

        @JsonView({Views.Create.class, Views.Update.class})
        @NotBlank
        @JsonProperty("descricao")
        String descricao
) {
    public static class Views {
        public interface Create {}
        public interface Update {}
    }
}
