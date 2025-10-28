package com.fix_it.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "os_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemPecaOS {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_os_item")
    @SequenceGenerator(name = "sq_os_item", sequenceName = "sq_os_item", allocationSize = 1)
    @Column(name = "sq_os_item")
    private Long id;

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
