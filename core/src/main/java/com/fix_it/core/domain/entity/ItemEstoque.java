package com.fix_it.core.domain.entity;

import com.fix_it.core.domain.enums.TipoItemEstoque;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ItemEstoque {

    private final UUID id;
    private String nmItemEstoque;
    private String dsItemEstoque;
    private Long vlPrecoUnitario;
    private Integer qtEstoqueAtual;
    private Integer qtEstoqueMinimo;
    private LocalDateTime dthAtualizacao;
    private TipoItemEstoque tipo;
    private Boolean abaixoMinimo;

    private ItemEstoque(UUID id, String nmItemEstoque, String dsItemEstoque, Long vlPrecoUnitario, Integer qtEstoqueAtual, Integer qtEstoqueMinimo, LocalDateTime dthAtualizacao, TipoItemEstoque tipo, Boolean abaixoMinimo) {
        this.id = id;
        this.nmItemEstoque = nmItemEstoque;
        this.dsItemEstoque = dsItemEstoque;
        this.vlPrecoUnitario = vlPrecoUnitario;
        this.qtEstoqueAtual = qtEstoqueAtual;
        this.qtEstoqueMinimo = qtEstoqueMinimo;
        this.dthAtualizacao = dthAtualizacao;
        this.tipo = tipo;
        this.abaixoMinimo = abaixoMinimo;
    }

    public static ItemEstoque of(UUID id, String nmItemEstoque, String dsItemEstoque, Long vlPrecoUnitario, Integer qtEstoqueAtual, Integer qtEstoqueMinimo, LocalDateTime dthAtualizacao, TipoItemEstoque tipo, Boolean abaixoMinimo) {
        return new ItemEstoque(id, nmItemEstoque, dsItemEstoque, vlPrecoUnitario, qtEstoqueAtual, qtEstoqueMinimo, dthAtualizacao, tipo, abaixoMinimo);
    }

    public UUID getId() {
        return id;
    }

    public String getNmItemEstoque() {
        return nmItemEstoque;
    }

    public String getDsItemEstoque() {
        return dsItemEstoque;
    }

    public Long getVlPrecoUnitario() {
        return vlPrecoUnitario;
    }

    public Integer getQtEstoqueAtual() {
        return qtEstoqueAtual;
    }

    public Integer getQtEstoqueMinimo() {
        return qtEstoqueMinimo;
    }

    public LocalDateTime getDthAtualizacao() {
        return dthAtualizacao;
    }

    public TipoItemEstoque getTipo() {
        return tipo;
    }

    public Boolean getAbaixoMinimo() {
        return abaixoMinimo;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ItemEstoque that = (ItemEstoque) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

