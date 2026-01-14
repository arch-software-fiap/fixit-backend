package com.fix_it.core.domain.entity;

import java.util.Objects;
import java.util.UUID;

public class ItemPecaOS {

    private final UUID id;
    private String nome;
    private String descricao;
    private Integer quantidade;
    private Long valorUnitario;
    private Long valorTotal;
    private final OrdemServico ordemServico;
    private final ItemEstoque itemEstoque;

    private ItemPecaOS(UUID id, String nome, String descricao, Integer quantidade, Long valorUnitario, Long valorTotal, OrdemServico ordemServico, ItemEstoque itemEstoque) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.valorTotal = valorTotal;
        this.ordemServico = ordemServico;
        this.itemEstoque = itemEstoque;
    }

    public static ItemPecaOS novo(String nome, String descricao, Integer quantidade, Long valorUnitario, OrdemServico ordemServico, ItemEstoque itemEstoque) {
        return new ItemPecaOS(null, nome, descricao, quantidade, valorUnitario, (long) quantidade * valorUnitario, ordemServico, itemEstoque);
    }

    public static ItemPecaOS of(UUID id, String nome, String descricao, Integer quantidade, Long valorUnitario, Long valorTotal, OrdemServico ordemServico, ItemEstoque itemEstoque) {
        return new ItemPecaOS(id, nome, descricao, quantidade, valorUnitario, valorTotal, ordemServico, itemEstoque);
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public Long getValorUnitario() {
        return valorUnitario;
    }

    public Long getValorTotal() {
        return valorTotal;
    }

    public OrdemServico getOrdemServico() {
        return ordemServico;
    }

    public ItemEstoque getItemEstoque() {
        return itemEstoque;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ItemPecaOS that = (ItemPecaOS) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
