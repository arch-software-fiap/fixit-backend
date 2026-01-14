package com.fix_it.infra.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import java.util.UUID;

@Entity
@Table(name = "os_item_servico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemServicoOSEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "sq_os_item_servico", columnDefinition = "UUID", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "qt_horas")
    private Integer quantidadeHoras;

    @Column(name = "vl_preco_unitario")
    private Long valorUnitario;

    @Column(name = "vl_preco_total")
    private Long valorTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sq_ordem_servico", foreignKey = @ForeignKey(name = "FK_ITEMSERVICO_ORDEM"), referencedColumnName = "sq_ordem_servico")
    private OrdemServicoEntity ordemServico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sq_servico", foreignKey = @ForeignKey(name = "FK_ITEMSERVICO_SERVICO"), referencedColumnName = "sq_servico")
    private ServicoEntity servico;

}
