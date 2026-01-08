package com.fix_it.core.domain.entity;

import java.util.Objects;
import java.util.UUID;

public class ItemServicoOS {

    private final UUID id;
    private Integer quantidadeHoras;
    private Long valorUnitario;
    private Long valorTotal;
    private final OrdemServico ordemServico;
    private final Servico servico;

    private ItemServicoOS(UUID id, Integer quantidadeHoras, Long valorUnitario, Long valorTotal, OrdemServico ordemServico, Servico servico) {
        this.id = id;
        this.quantidadeHoras = quantidadeHoras;
        this.valorUnitario = valorUnitario;
        this.valorTotal = valorTotal;
        this.ordemServico = ordemServico;
        this.servico = servico;
    }

    public static ItemServicoOS novo(Integer quantidadeHoras, Long valorUnitario, OrdemServico ordemServico, Servico servico) {
        return new ItemServicoOS(null, quantidadeHoras, valorUnitario, (long) quantidadeHoras * valorUnitario, ordemServico, servico);
    }

    public static ItemServicoOS of(UUID id, Integer quantidadeHoras, Long valorUnitario, Long valorTotal, OrdemServico ordemServico, Servico servico) {
        return new ItemServicoOS(id, quantidadeHoras, valorUnitario, valorTotal, ordemServico, servico);
    }

    public UUID getId() {
        return id;
    }

    public Integer getQuantidadeHoras() {
        return quantidadeHoras;
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

    public Servico getServico() {
        return servico;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ItemServicoOS that = (ItemServicoOS) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
