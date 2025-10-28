package com.fix_it.app.model;

import com.fix_it.app.model.enums.SituacaoOrcamento;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

import java.time.LocalDateTime;

import static com.fix_it.app.model.enums.SituacaoOrcamento.GERADO;

@Entity
@Table(name = "orcamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Orcamento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_orcamento")
    @SequenceGenerator(name = "sq_orcamento", sequenceName = "sq_orcamento", allocationSize = 1)
    @Column(name = "sq_orcamento")
    private Long id;

    @Column(name = "dth_geracao", updatable = false)
    private LocalDateTime dthGeracao;

    @PositiveOrZero
    @Column(name = "vl_total")
    private Long vlTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "nm_situacao")
    @NotNull
    @Check(constraints = "nm_situacao in ('GERADO', 'ENVIADO', 'APROVADO', 'REJEITADO')")
    private SituacaoOrcamento situacao = GERADO;

    @OneToOne
    @JoinColumn(name = "sq_ordem_servico", unique = true, foreignKey = @ForeignKey(name = "FK_ORCAMENTO_ORDEMSERVICO"), referencedColumnName = "sq_ordem_servico")
    @NotNull
    private OrdemServico ordemServico;

    @PrePersist
    public void prePersist() {
        dthGeracao = LocalDateTime.now();
    }
}
