package com.fix_it.app.common.view;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import java.util.UUID;

@Data
@Entity
@Immutable
@Subselect("""
    SELECT
        osc.sq_ordem_servico_item AS id,
        osc.sq_ordem_servico AS ordem_servico_id,
        s.nm_servico AS nome_servico,
        osc.qt_horas AS quantidade_horas,
        osc.vl_preco_unitario AS preco_unitario,
        osc.vl_preco_total AS preco_total
    FROM public.os_item_servico osc
    LEFT JOIN public.servico s ON s.sq_servico = osc.sq_servico
""")
public class ItemServicoView {
    @Id
    private UUID id;
    private UUID ordemServicoId;
    private String nomeServico;
    private Integer quantidadeHoras;
    private Double precoUnitario;
    private Double precoTotal;
}
