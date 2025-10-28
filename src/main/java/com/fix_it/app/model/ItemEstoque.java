package com.fix_it.app.model;

import com.fix_it.app.model.enums.TipoItemEstoque;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "item_estoque", uniqueConstraints = {
        @UniqueConstraint(name = "PK_item_estoque", columnNames = "sq_item_estoque")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemEstoque {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "sq_item_estoque", nullable = false, updatable = false)
    private UUID id;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "TP_ITEM")
    @NotNull
    @Check(constraints = "TP_ITEM in ('PECA', 'INSUMO')")
    private TipoItemEstoque tipo;

    @OneToMany(mappedBy = "itemEstoque", fetch = FetchType.LAZY)
    private List<MovimentoEstoque> movimentosEstoque = new ArrayList<>();

    @OneToMany(mappedBy = "itemEstoque", fetch = FetchType.LAZY)
    private List<ItemPecaOS> itensPecaOS = new ArrayList<>();

    @PreUpdate
    public void preUpdate() {
        dthAtualizacao = LocalDateTime.now();
    }
}

