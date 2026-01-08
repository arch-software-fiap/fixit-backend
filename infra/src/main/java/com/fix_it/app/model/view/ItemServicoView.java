package com.fix_it.app.model.view;

import lombok.Data;
import java.util.UUID;
import org.hibernate.annotations.Immutable;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Immutable
public class ItemServicoView {
    private UUID id;

    @JsonProperty("nome_servico")
    private String nomeServico;

    @JsonProperty("quantidade_horas")
    private Integer quantidadeHoras;

    @JsonProperty("preco_unitario")
    private Double precoUnitario;

    @JsonProperty("preco_total")
    private Double precoTotal;
}