package com.fix_it.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "os_servico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemServicoOS {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_item_servico_os_seq")
    @SequenceGenerator(name = "sq_item_servico_os_seq", sequenceName = "sq_item_servico_os", allocationSize = 1)
    @Column(name = "sq_item_servico_os")
    private Long sqItemServicoOS;

    @Positive
    @Column(name = "qt_horas")
    private Integer qtHoras;

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
    @JoinColumn(name = "sq_servico")
    @NotNull
    private Servico servico;
}
