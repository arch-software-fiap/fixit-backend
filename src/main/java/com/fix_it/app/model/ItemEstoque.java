package com.fix_it.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "item_estoque")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_item_estoque")
    @SequenceGenerator(name = "sq_item_estoque", sequenceName = "sq_item_estoque", allocationSize = 1)
    @Column(name = "sq_item_estoque")
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Column(name = "nm_item_estoque")
    private String nmItemEstoque;

    @Column(name = "ds_item_estoque", columnDefinition = "TEXT")
    private String dsItemEstoque;

    @PositiveOrZero
    @Column(name = "vl_preco_unitario")
    private Long vlPrecoUnitario;

    @PositiveOrZero
    @Column(name = "qt_estoque_atual")
    private Integer qtEstoqueAtual;

    @PositiveOrZero
    @Column(name = "qt_estoque_minimo")
    private Integer qtEstoqueMinimo;

    @Column(name = "dth_atualizacao")
    private LocalDateTime dthAtualizacao;

    @Column(name = "tp_item")
    @NotBlank
    private String tpItem; // PECA ou INSUMO

    @OneToMany(mappedBy = "itemEstoque", fetch = FetchType.LAZY)
    private List<MovimentoEstoque> movimentosEstoque = new ArrayList<>();

    @OneToMany(mappedBy = "itemEstoque", fetch = FetchType.LAZY)
    private List<ItemPecaOS> itensPecaOS = new ArrayList<>();

    @PreUpdate
    public void preUpdate() {
        dthAtualizacao = LocalDateTime.now();
    }
}

