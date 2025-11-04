package com.fix_it.app.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fix_it.app.common.databind.Databind;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

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

    @NotBlank
    @Size(max = 255)
    @Column(name = "nm_os_item")
    private String nome;

    @Column(name = "ds_os_item", columnDefinition = "TEXT")
    private String descricao;

    @Positive
    @Column(name = "qt_itens")
    private Integer quantidade;

    @PositiveOrZero
    @Column(name = "vl_preco_unitario")
    private Long valorUnitario;

    @PositiveOrZero
    @Column(name = "vl_preco_total")
    private Long valorTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = Databind.IdSerializer.class)
    @JsonDeserialize(using = Databind.IdDeserializer.class)
    @JoinColumn(name = "sq_ordem_servico", foreignKey = @ForeignKey(name = "FK_ITEMPECA_ORDEMSERVICO"), referencedColumnName = "sq_ordem_servico")
    @NotNull
    private OrdemServico ordemServico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = Databind.IdSerializer.class)
    @JsonDeserialize(using = Databind.IdDeserializer.class)
    @JoinColumn(name = "sq_item_estoque", foreignKey = @ForeignKey(name = "FK_ITEMPECA_ITEMESTOQUE"), referencedColumnName = "sq_item_estoque")
    @NotNull
    private ItemEstoque itemEstoque;

    @PrePersist @PreUpdate
    private void calcularTotal() {
        if (valorUnitario != null && quantidade != null) {
            this.valorTotal = valorUnitario * quantidade;
        }
    }

}
