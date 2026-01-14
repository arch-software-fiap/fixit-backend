package com.fix_it.core.domain.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Servico {

    private final UUID id;
    private String nome;
    private String descricao;
    private Long valorPrecoBase;
    private LocalDateTime dthAtualizacao;

    private Servico(UUID id, String nome, String descricao, Long valorPrecoBase, LocalDateTime dthAtualizacao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.valorPrecoBase = valorPrecoBase;
        this.dthAtualizacao = dthAtualizacao;
    }

    public static Servico novo(String nmServico, String dsServico, Long vlPrecoBase) {
        return new Servico(null, nmServico, dsServico, vlPrecoBase, LocalDateTime.now());
    }

    public static Servico of(UUID id, String nmServico, String dsServico, Long vlPrecoBase, LocalDateTime dthAtualizacao) {
        return new Servico(id, nmServico, dsServico, vlPrecoBase, dthAtualizacao);
    }

    public void atualizar(String nmServico, String dsServico, Long vlPrecoBase) {
        this.nome = nmServico;
        this.descricao = dsServico;
        this.valorPrecoBase = vlPrecoBase;
        this.dthAtualizacao = LocalDateTime.now();
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

    public Long getValorPrecoBase() {
        return valorPrecoBase;
    }

    public LocalDateTime getDthAtualizacao() {
        return dthAtualizacao;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Servico servico = (Servico) o;
        return Objects.equals(id, servico.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
