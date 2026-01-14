package com.fix_it.infra.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "veiculo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VeiculoEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "sq_veiculo", columnDefinition = "UUID", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "nm_veiculo")
    private String nmVeiculo;

    @Column(name = "ds_veiculo", columnDefinition = "TEXT")
    private String dsVeiculo;

    @Column(name = "placa", unique = true)
    private String placa;

    @Column(name = "marca")
    private String marca;

    @Column(name = "modelo")
    private String modelo;

    @Column(name = "ano")
    private Integer ano;

    @Column(name = "dth_cadastro", updatable = false)
    private LocalDateTime dthCadastro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sq_cliente", foreignKey = @ForeignKey(name = "FK_VEICULO_CLIENTE"))
    private ClienteEntity cliente;

}
