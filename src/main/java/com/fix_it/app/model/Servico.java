package com.fix_it.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "servico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_servico")
    @SequenceGenerator(name = "sq_servico", sequenceName = "sq_servico", allocationSize = 1)
    @Column(name = "sq_servico")
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Column(name = "nm_servico")
    private String nmServico;

    @Column(name = "ds_servico", columnDefinition = "TEXT")
    private String dsServico;

    @PositiveOrZero
    @Column(name = "vl_preco_base")
    private Long vlPrecoBase;

    @Column(name = "dth_atualizacao")
    private LocalDateTime dthAtualizacao;


    @OneToMany(mappedBy = "servico", fetch = FetchType.LAZY)
    private List<ItemServicoOS> itensServicoOS = new ArrayList<>();

    @PreUpdate
    public void preUpdate() {
        dthAtualizacao = LocalDateTime.now();
    }
}
