package com.fix_it.core.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Cliente {

    private final UUID id;
    private String nome;
    private String cpfCnpj;
    private String email;
    private String telefone;
    private LocalDate dtNascimento;
    private final LocalDate dtCadastro;
    private LocalDateTime dthAtualizacao;

    private Cliente(UUID id, String nome, String cpfCnpj, String email, String telefone, LocalDate dtNascimento, LocalDate dtCadastro, LocalDateTime dthAtualizacao) {
        this.id = id;
        this.nome = nome;
        this.cpfCnpj = cpfCnpj;
        this.email = email;
        this.telefone = telefone;
        this.dtNascimento = dtNascimento;
        this.dtCadastro = dtCadastro;
        this.dthAtualizacao = dthAtualizacao;
    }

    public static Cliente novo(String nome, String cpfCnpj, String email, String telefone, LocalDate dtNascimento) {
        LocalDate hoje = LocalDate.now();
        LocalDateTime agora = LocalDateTime.now();
        return new Cliente(null, nome, cpfCnpj, email, telefone, dtNascimento, hoje, agora);
    }

    public static Cliente of(UUID id, String nome, String cpfCnpj, String email, String telefone, LocalDate dtNascimento, LocalDate dtCadastro, LocalDateTime dthAtualizacao) {
        return new Cliente(id, nome, cpfCnpj, email, telefone, dtNascimento, dtCadastro, dthAtualizacao);
    }

    public void atualizarDados(String nome, String cpfCnpj, String email, String telefone, LocalDate dtNascimento) {
        this.nome = nome;
        this.cpfCnpj = cpfCnpj;
        this.email = email;
        this.telefone = telefone;
        this.dtNascimento = dtNascimento;
        this.dthAtualizacao = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public LocalDate getDtNascimento() {
        return dtNascimento;
    }

    public LocalDate getDtCadastro() {
        return dtCadastro;
    }

    public LocalDateTime getDthAtualizacao() {
        return dthAtualizacao;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(getId(), cliente.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}
