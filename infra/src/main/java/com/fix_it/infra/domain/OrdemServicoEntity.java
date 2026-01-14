package com.fix_it.infra.domain;

import com.fix_it.core.domain.enums.SituacaoOrdemServico;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ordem_servico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdemServicoEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "sq_ordem_servico", columnDefinition = "UUID", nullable = false, updatable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "nm_situacao")
    private SituacaoOrdemServico situacao;

    @Column(name = "ds_os", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "dth_abertura", updatable = false)
    private LocalDateTime dataAberturaEm;

    @Column(name = "dth_fechamento")
    private LocalDateTime dataFechamentoEm;

    @Column(name = "vl_orcamento_total")
    private Long valorOrcamentoTotal;

    @Column(name = "vl_total_final")
    private Long valorTotalFinal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sq_cliente", foreignKey = @ForeignKey(name = "FK_ORDEMSERVICO_CLIENTE"), referencedColumnName = "sq_cliente")
    private ClienteEntity cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sq_veiculo", foreignKey = @ForeignKey(name = "FK_ORDEMSERVICO_VEICULO"), referencedColumnName = "sq_veiculo")
    private VeiculoEntity veiculo;

}
