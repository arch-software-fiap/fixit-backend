package com.fix_it.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordem_servico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_os")
    @SequenceGenerator(name = "sq_os", sequenceName = "sq_os", allocationSize = 1)
    @Column(name = "sq_os")
    private Long id;

    @NotBlank
    @Column(name = "nm_status")
    private String nmStatus; // RECEBIDA, EM_DIAGNOSTICO, AGUARDANDO_APROVACAO, EM_EXECUCAO, FINALIZADA, ENTREGUE

    @Column(name = "dth_abertura", updatable = false)
    private LocalDateTime dthAbertura;

    @Column(name = "dth_fechamento")
    private LocalDateTime dthFechamento;

    @Column(name = "ds_os", columnDefinition = "TEXT")
    private String dsOs;

    @PositiveOrZero
    @Column(name = "vl_orcamento_total")
    private Long vlOrcamentoTotal;

    @PositiveOrZero
    @Column(name = "vl_total_final")
    private Long vlTotalFinal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sq_cliente")
    @NotNull
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sq_veiculo")
    @NotNull
    private Veiculo veiculo;

    @OneToOne(mappedBy = "ordemServico", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Orcamento orcamento;

    @OneToMany(mappedBy = "ordemServico", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemServicoOS> itensServico = new ArrayList<>();

    @OneToMany(mappedBy = "ordemServico", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemPecaOS> itensPeca = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        dthAbertura = LocalDateTime.now();
    }
}
