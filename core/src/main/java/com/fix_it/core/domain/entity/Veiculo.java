package com.fix_it.core.domain.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Veiculo {

    private final UUID id;
    private String nmVeiculo;
    private String dsVeiculo;
    private String placa;
    private String marca;
    private String modelo;
    private Integer ano;
    private LocalDateTime dthCadastro;
    private Cliente cliente;

    private Veiculo(UUID id, String nmVeiculo, String dsVeiculo, String placa, String marca, String modelo, Integer ano, LocalDateTime dthCadastro, Cliente cliente) {
        this.id = id;
        this.nmVeiculo = nmVeiculo;
        this.dsVeiculo = dsVeiculo;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.dthCadastro = dthCadastro;
        this.cliente = cliente;
    }

    public static Veiculo novo(String nmVeiculo, String dsVeiculo, String placa, String marca, String modelo, Integer ano, Cliente cliente) {
        return new Veiculo(null, nmVeiculo, dsVeiculo, placa, marca, modelo, ano, LocalDateTime.now(), cliente);
    }

    public static Veiculo of(UUID id, String nmVeiculo, String dsVeiculo, String placa, String marca, String modelo, Integer ano, LocalDateTime dthCadastro, Cliente cliente) {
        return new Veiculo(id, nmVeiculo, dsVeiculo, placa, marca, modelo, ano, dthCadastro, cliente);
    }

    public void atualizar(String nmVeiculo, String dsVeiculo, String placa, String marca, String modelo, Integer ano) {
        this.nmVeiculo = nmVeiculo;
        this.dsVeiculo = dsVeiculo;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
    }

    public UUID getId() {
        return id;
    }

    public String getNmVeiculo() {
        return nmVeiculo;
    }

    public String getDsVeiculo() {
        return dsVeiculo;
    }

    public String getPlaca() {
        return placa;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public Integer getAno() {
        return ano;
    }

    public LocalDateTime getDthCadastro() {
        return dthCadastro;
    }

    public Cliente getCliente() {
        return cliente;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Veiculo veiculo = (Veiculo) o;
        return Objects.equals(id, veiculo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
