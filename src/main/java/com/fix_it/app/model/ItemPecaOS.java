package com.fix_it.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "os_item", uniqueConstraints = {
        @UniqueConstraint(name = "PK_os_item", columnNames = "sq_os_item")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemPecaOS {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "sq_os_item", nullable = false, updatable = false)
    private UUID id;

    @Positive
    @Column(name = "qt_itens")
    private Integer qtItens;

    @PositiveOrZero
    @Column(name = "vl_preco_unitario")
    private Long vlPrecoUnitario;

    @PositiveOrZero
    @Column(name = "vl_preco_total")
    private Long vlPrecoTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sq_ordem_servico", foreignKey = @ForeignKey(name = "FK_ITEMPECA_ORDEMSERVICO"), referencedColumnName = "sq_ordem_servico")
    @NotNull
    private OrdemServico ordemServico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sq_item_estoque", foreignKey = @ForeignKey(name = "FK_ITEMPECA_ITEMESTOQUE"), referencedColumnName = "sq_item_estoque")
    @NotNull
    private ItemEstoque itemEstoque;

    @OneToMany(mappedBy = "itemPecaOS", fetch = FetchType.LAZY)
    private List<MovimentoEstoque> movimentosEstoque = new ArrayList<>();
}
