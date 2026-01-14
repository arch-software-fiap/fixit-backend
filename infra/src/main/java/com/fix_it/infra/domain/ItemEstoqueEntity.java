package com.fix_it.infra.domain;

import com.fix_it.core.domain.enums.TipoItemEstoque;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "item_estoque")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemEstoqueEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "sq_item_estoque", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "nm_item_estoque")
    private String nome;

    @Column(name = "ds_item_estoque", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "vl_preco_unitario")
    private Long valor;

    @Column(name = "qt_estoque_atual")
    private Integer qtEstoqueAtual;

    @Column(name = "qt_estoque_minimo")
    private Integer qtEstoqueMinimo;

    @Column(name = "dth_atualizacao")
    private LocalDateTime dthAtualizacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "TP_ITEM")
    private TipoItemEstoque tipo;

    @Column(name = "st_abaixo_minimo", insertable = false, updatable = false)
    private Boolean abaixoMinimo;

}
