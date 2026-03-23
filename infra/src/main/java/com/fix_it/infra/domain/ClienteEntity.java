package com.fix_it.infra.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cliente", uniqueConstraints = {
        @UniqueConstraint(name = "PK_cliente", columnNames = "sq_cliente")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "sq_cliente", columnDefinition = "UUID", nullable = false, updatable = false)
    private UUID id;

    @NotBlank
    @Size(max = 255)
    @Column(name = "nm_cliente")
    private String nome;

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

    @Column(name = "dt_nascimento")
    private LocalDate dtNascimento;

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
