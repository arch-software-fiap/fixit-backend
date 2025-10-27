package com.fix_it.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimento_estoque")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovimentoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_movimento_estoque_seq")
    @SequenceGenerator(name = "sq_movimento_estoque_seq", sequenceName = "sq_movimento_estoque", allocationSize = 1)
    @Column(name = "sq_movimento_estoque")
    private Long sqMovimentoEstoque;

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
    @JoinColumn(name = "sq_item_estoque")
    @NotNull
    private ItemEstoque itemEstoque;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sq_item_peca_os")
    private ItemPecaOS itemPecaOS;

    @PrePersist
    public void prePersist() {
        dthMovimento = LocalDateTime.now();
    }
}

