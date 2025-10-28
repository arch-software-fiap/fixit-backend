package com.fix_it.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Table(name = "movimento_estoque", uniqueConstraints = {
        @UniqueConstraint(name = "PK_movimento_estoque", columnNames = "sq_movimento_estoque")
})
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovimentoEstoque {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "sq_movimento_estoque", columnDefinition = "UUID", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "dth_movimento", updatable = false)
    private LocalDateTime dthMovimento;

    @NotBlank
    @Column(name = "nm_tipo_movimento")
    private String nmTipoMovimento; // ENTRADA, SAIDA, AJUSTE

    @Positive
    @Column(name = "qt_mov")
    private Integer qtMov;

    @Column(name = "ds_movimento", columnDefinition = "TEXT")
    private String dsMovimento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sq_item_estoque", foreignKey = @ForeignKey(name = "FK_MOVIMENTO_ITEMESTOQUE"), referencedColumnName = "sq_item_estoque")
    @NotNull
    private ItemEstoque itemEstoque;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sq_item_peca_os", foreignKey = @ForeignKey(name = "FK_MOVIMENTO_ITEMPECAOS"), referencedColumnName = "sq_os_item")
    private ItemPecaOS itemPecaOS;

    @PrePersist
    public void prePersist() {
        dthMovimento = LocalDateTime.now();
    }
}

