package com.fix_it.app.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fix_it.app.common.databind.Databind;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "os_item_servico", uniqueConstraints = {
        @UniqueConstraint(name = "PK_os_item_servico", columnNames = "sq_os_item_servico")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemServicoOS {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "sq_os_item_servico", columnDefinition = "UUID", nullable = false, updatable = false)
    private UUID id;

    @Positive
    @Column(name = "qt_horas")
    private Integer quantidadeHoras;

    @PositiveOrZero
    @Column(name = "vl_preco_unitario")
    private Long valorUnitario;

    @PositiveOrZero
    @Column(name = "vl_preco_total")
    private Long valorTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = Databind.IdSerializer.class)
    @JsonDeserialize(using = Databind.IdDeserializer.class)
    @JoinColumn(name = "sq_ordem_servico", foreignKey = @ForeignKey(name = "FK_ITEMSERVICO_ORDEM"), referencedColumnName = "sq_ordem_servico")
    @NotNull
    private OrdemServico ordemServico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = Databind.IdSerializer.class)
    @JsonDeserialize(using = Databind.IdDeserializer.class)
    @JoinColumn(name = "sq_servico", foreignKey = @ForeignKey(name = "FK_ITEMSERVICO_SERVICO"), referencedColumnName = "sq_servico")
    @NotNull
    private Servico servico;
}
