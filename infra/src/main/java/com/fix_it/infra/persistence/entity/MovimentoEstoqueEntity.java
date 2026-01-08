package com.fix_it.infra.persistence.entity;

import com.fix_it.core.domain.enums.TipoMovimentoEstoque;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Table(name = "movimento_estoque")
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovimentoEstoqueEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "sq_movimento_estoque", columnDefinition = "UUID", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "dth_movimento", updatable = false)
    private LocalDateTime dthMovimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "nm_tipo_movimento", nullable = false, length = 20)
    private TipoMovimentoEstoque tipo;

    @Column(name = "qt_mov")
    private Integer quantidade;

    @Column(name = "ds_movimento", columnDefinition = "TEXT")
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sq_item_estoque", foreignKey = @ForeignKey(name = "FK_MOVIMENTO_ITEMESTOQUE"), referencedColumnName = "sq_item_estoque")
    private ItemEstoqueEntity itemEstoque;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sq_item_peca_os", foreignKey = @ForeignKey(name = "FK_MOVIMENTO_ITEMPECAOS"), referencedColumnName = "sq_os_item")
    private ItemPecaOSEntity peca;

}
