package com.fix_it.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "orcamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Orcamento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_orcamento_seq")
    @SequenceGenerator(name = "sq_orcamento_seq", sequenceName = "sq_orcamento", allocationSize = 1)
    @Column(name = "sq_orcamento")
    private Long sqOrcamento;

    @Column(name = "dth_geracao", updatable = false)
    private LocalDateTime dthGeracao;

    @PositiveOrZero
    @Column(name = "vl_total")
    private Long vlTotal;

    @NotBlank
    @Column(name = "nm_status")
    private String nmStatus; // GERADO, ENVIADO, APROVADO, REJEITADO

    @OneToOne
    @JoinColumn(name = "sq_os", unique = true)
    @NotNull
    private OrdemServico ordemServico;

    @PrePersist
    public void prePersist() {
        dthGeracao = LocalDateTime.now();
    }
}
