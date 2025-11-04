package com.fix_it.app.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fix_it.app.common.databind.Databind;
import com.fix_it.app.model.enums.TipoMovimentoEstoque;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;
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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "nm_tipo_movimento", nullable = false, length = 20)
    @Check(constraints = "nm_tipo_movimento in ('ENTRADA', 'SAIDA', 'AJUSTE')")
    private TipoMovimentoEstoque tipo;

    @Positive
    @Column(name = "qt_mov")
    private Integer quantidade;

    @Column(name = "ds_movimento", columnDefinition = "TEXT")
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sq_item_estoque", foreignKey = @ForeignKey(name = "FK_MOVIMENTO_ITEMESTOQUE"), referencedColumnName = "sq_item_estoque")
    @NotNull
    @JsonSerialize(using = Databind.IdSerializer.class)
    @JsonDeserialize(using = Databind.IdDeserializer.class)
    private ItemEstoque itemEstoque;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = Databind.IdSerializer.class)
    @JsonDeserialize(using = Databind.IdDeserializer.class)
    @JoinColumn(name = "sq_item_peca_os", foreignKey = @ForeignKey(name = "FK_MOVIMENTO_ITEMPECAOS"), referencedColumnName = "sq_os_item")
    private ItemPecaOS peca;

    @PrePersist
    public void prePersist() {
        dthMovimento = LocalDateTime.now();
    }
}

