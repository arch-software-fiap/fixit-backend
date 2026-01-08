package com.fix_it.app.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.Immutable;

import java.util.UUID;

@Data
@Immutable
public class ItemPecaView {

    private UUID id;

    @JsonProperty("nome_servico")
    private String nomeServico;

    @JsonProperty("quantidade_itens")
    private Integer quantidadeItens;

    @JsonProperty("preco_unitario")
    private Double precoUnitario;

    @JsonProperty("preco_total")
    private Double precoTotal;
}
