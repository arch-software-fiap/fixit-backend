package com.fix_it.core.domain.entity;


import java.time.LocalDateTime;
import java.util.UUID;

public class Veiculo {

    private UUID id;

    private String nmVeiculo;

    private String dsVeiculo;

    private String placa;

    private String marca;

    private String modelo;

    private Integer ano;

    private LocalDateTime dthCadastro;

    private Cliente cliente;

}
