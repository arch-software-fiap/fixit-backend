package com.fix_it.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import java.util.UUID;

@Entity
@Table(name = "os_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemPecaOSEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "sq_os_item", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "nm_os_item")
    private String nome;

    @Column(name = "ds_os_item", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "qt_itens")
    private Integer quantidade;

    @Column(name = "vl_preco_unitario")
    private Long valorUnitario;

    @Column(name = "vl_preco_total")
    private Long valorTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sq_ordem_servico", foreignKey = @ForeignKey(name = "FK_ITEMPECA_ORDEMSERVICO"), referencedColumnName = "sq_ordem_servico")
    private OrdemServicoEntity ordemServico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sq_item_estoque", foreignKey = @ForeignKey(name = "FK_ITEMPECA_ITEMESTOQUE"), referencedColumnName = "sq_item_estoque")
    private ItemEstoqueEntity itemEstoque;

}
