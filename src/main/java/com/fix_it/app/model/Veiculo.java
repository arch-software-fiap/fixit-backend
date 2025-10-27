package com.fix_it.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "veiculo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_veiculo_seq")
    @SequenceGenerator(name = "sq_veiculo_seq", sequenceName = "sq_veiculo", allocationSize = 1)
    @Column(name = "sq_veiculo")
    private Long sqVeiculo;

    @NotBlank
    @Size(max = 255)
    @Column(name = "nm_veiculo")
    private String nmVeiculo;

    @Column(name = "ds_veiculo", columnDefinition = "TEXT")
    private String dsVeiculo;

    @NotBlank
    @Size(max = 10)
    @Column(name = "placa", unique = true)
    private String placa;

    @Size(max = 100)
    @Column(name = "marca")
    private String marca;

    @Size(max = 100)
    @Column(name = "modelo")
    private String modelo;

    @Column(name = "ano")
    private Integer ano;

    @Column(name = "dth_cadastro", updatable = false)
    private LocalDateTime dthCadastro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sq_cliente")
    private Cliente cliente;

    @PrePersist
    public void prePersist() {
        dthCadastro = LocalDateTime.now();
    }
}
