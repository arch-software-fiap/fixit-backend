package com.fix_it.app.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fix_it.app.common.databind.Databind;
import com.fix_it.app.model.enums.SituacaoOrdemServico;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ordem_servico", uniqueConstraints = {
        @UniqueConstraint(name = "PK_ordem_servico", columnNames = "sq_ordem_servico")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdemServico {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "sq_ordem_servico", columnDefinition = "UUID", nullable = false, updatable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "nm_situacao")
    @NotNull
    @Check(constraints = "nm_situacao in ('RECEBIDA', 'EM_DIAGNOSTICO', 'AGUARDANDO_APROVACAO', 'EM_EXECUCAO', 'FINALIZADA', 'ENTREGUE')")
    private SituacaoOrdemServico situacao;

    @Column(name = "ds_os", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "dth_abertura", updatable = false)
    private LocalDateTime dataAberturaEm;

    @Column(name = "dth_fechamento")
    private LocalDateTime dataFechamentoEm;

    @PositiveOrZero
    @Column(name = "vl_orcamento_total")
    private Long valorOrcamentoTotal;

    @PositiveOrZero
    @Column(name = "vl_total_final")
    private Long valorTotalFinal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = Databind.IdSerializer.class)
    @JsonDeserialize(using = Databind.IdDeserializer.class)
    @JoinColumn(name = "sq_cliente", foreignKey = @ForeignKey(name = "FK_ORDEMSERVICO_CLIENTE"), referencedColumnName = "sq_cliente")
    @NotNull
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = Databind.IdSerializer.class)
    @JsonDeserialize(using = Databind.IdDeserializer.class)
    @JoinColumn(name = "sq_veiculo", foreignKey = @ForeignKey(name = "FK_ORDEMSERVICO_VEICULO"), referencedColumnName = "sq_veiculo")
    @NotNull
    private Veiculo veiculo;

    @PrePersist
    public void prePersist() {
        dataAberturaEm = LocalDateTime.now();
    }
}
