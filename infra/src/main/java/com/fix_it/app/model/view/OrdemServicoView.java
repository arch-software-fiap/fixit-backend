package com.fix_it.app.model.view;

import com.fix_it.app.common.converter.JsonListConverter;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import java.util.List;
import java.util.UUID;
@Data
@Entity
@Immutable
@Subselect("SELECT * FROM public.vw_ordem_servico")
public class OrdemServicoView {

    @Id
    private UUID id;
    private String status;
    private String descricao;
    private String veiculo;
    private String cliente;
    private String cpfCnpj;

    @Column(name = "itens_servico", columnDefinition = "json")
    @Convert(converter = JsonListConverter.class)
    private List<ItemServicoView> servicos;

    @Column(name = "itens_peca", columnDefinition = "json")
    @Convert(converter = JsonListConverter.class)
    private List<ItemPecaView> pecas;
}
