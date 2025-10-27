package com.fix_it.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_cliente_seq")
    @SequenceGenerator(name = "sq_cliente_seq", sequenceName = "sq_cliente", allocationSize = 1)
    @Column(name = "sq_cliente")
    private Long sqCliente;

    @NotBlank
    @Size(max = 255)
    @Column(name = "nm_cliente")
    private String nmCliente;

    @Column(name = "ds_cliente", columnDefinition = "TEXT")
    private String dsCliente;

    @NotBlank
    @Size(max = 18)
    @Column(name = "cpf_cnpj", unique = true)
    private String cpfCnpj;

    @Email
    @Size(max = 255)
    @Column(name = "email")
    private String email;

    @Size(max = 20)
    @Column(name = "telefone")
    private String telefone;

    @Column(name = "dt_cadastro", updatable = false)
    private LocalDate dtCadastro;

    @Column(name = "dth_atualizacao")
    private LocalDateTime dthAtualizacao;

    @PrePersist
    public void prePersist() {
        dtCadastro = LocalDate.now();
        dthAtualizacao = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        dthAtualizacao = LocalDateTime.now();
    }
}
