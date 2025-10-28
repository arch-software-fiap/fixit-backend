package com.fix_it.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "servico", uniqueConstraints = {
        @UniqueConstraint(name = "PK_servico", columnNames = "sq_servico")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Servico {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "sq_servico", columnDefinition = "UUID", nullable = false, updatable = false)
    private UUID id;

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
