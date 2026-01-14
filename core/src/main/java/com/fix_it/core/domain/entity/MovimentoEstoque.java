package com.fix_it.core.domain.entity;

import com.fix_it.core.domain.enums.TipoMovimentoEstoque;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class MovimentoEstoque {

    private final UUID id;
    private LocalDateTime dthMovimento;
    private TipoMovimentoEstoque tipo;
    private Integer quantidade;
    private String descricao;
    private final ItemEstoque itemEstoque;
    private final ItemPecaOS peca;

    private MovimentoEstoque(UUID id, LocalDateTime dthMovimento, TipoMovimentoEstoque tipo, Integer quantidade, String descricao, ItemEstoque itemEstoque, ItemPecaOS peca) {
        this.id = id;
        this.dthMovimento = dthMovimento;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.descricao = descricao;
        this.itemEstoque = itemEstoque;
        this.peca = peca;
    }

    public static MovimentoEstoque novo(TipoMovimentoEstoque tipo, Integer quantidade, String descricao, ItemEstoque itemEstoque, ItemPecaOS peca) {
        return new MovimentoEstoque(null, LocalDateTime.now(), tipo, quantidade, descricao, itemEstoque, peca);
    }

    public static MovimentoEstoque of(UUID id, LocalDateTime dthMovimento, TipoMovimentoEstoque tipo, Integer quantidade, String descricao, ItemEstoque itemEstoque, ItemPecaOS peca) {
        return new MovimentoEstoque(id, dthMovimento, tipo, quantidade, descricao, itemEstoque, peca);
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getDthMovimento() {
        return dthMovimento;
    }

    public TipoMovimentoEstoque getTipo() {
        return tipo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public ItemEstoque getItemEstoque() {
        return itemEstoque;
    }

    public ItemPecaOS getPeca() {
        return peca;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MovimentoEstoque that = (MovimentoEstoque) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

