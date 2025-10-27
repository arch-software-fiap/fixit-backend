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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_item_peca_os_seq")
    @SequenceGenerator(name = "sq_item_peca_os_seq", sequenceName = "sq_item_peca_os", allocationSize = 1)
    @Column(name = "sq_item_peca_os")
    private Long sqItemPecaOS;

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
    @JoinColumn(name = "sq_os")
    @NotNull
    private OrdemServico ordemServico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sq_item_estoque")
    @NotNull
    private ItemEstoque itemEstoque;

    @OneToMany(mappedBy = "itemPecaOS", fetch = FetchType.LAZY)
    private List<MovimentoEstoque> movimentosEstoque = new ArrayList<>();
}
