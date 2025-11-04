package com.fix_it.app.common.view;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.JoinFormula;


import java.util.List;
import java.util.UUID;

@Data
@Entity
@Immutable
@Subselect("""
    SELECT
        os.sq_ordem_servico AS id,
        os.nm_situacao AS status,
        os.ds_os AS descricao,
        CONCAT('Veículo: ', v.marca, ' ', v.modelo, ' - ', v.placa) AS veiculo,
        CONCAT('Cliente: ', c.nm_cliente, ' - ', c.cpf_cnpj) AS cliente,
        c.cpf_cnpj AS cpf_cnpj
    FROM public.ordem_servico os
    LEFT JOIN public.veiculo v ON v.sq_veiculo = os.sq_veiculo
    LEFT JOIN public.cliente c ON c.sq_cliente = os.sq_cliente
    
""")
public class OrdemServicoView {

    @Id
    private UUID id;
    private String status;
    private String descricao;
    private String veiculo;
    private String cliente;

    private String cpfCnpj;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinFormula("(SELECT i.id FROM os_item_servico i WHERE i.sq_ordem_servico = id)")
    private List<ItemServicoView> servicos;
}
