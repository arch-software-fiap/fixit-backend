package com.fix_it.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "servico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServicoEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "sq_servico", columnDefinition = "UUID", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "nm_servico")
    private String nmServico;

    @Column(name = "ds_servico", columnDefinition = "TEXT")
    private String dsServico;

    @Column(name = "vl_preco_base")
    private Long vlPrecoBase;

    @Column(name = "dth_atualizacao")
    private LocalDateTime dthAtualizacao;

}
