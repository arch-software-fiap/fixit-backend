package com.fix_it.app.common.factory;

import com.fix_it.app.common.dto.ItemPecaDTO;
import com.fix_it.app.common.dto.ItemServicoDTO;
import com.fix_it.app.model.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrdemServicoItemFactory {

    public static ItemServicoOS criarItemServicoOS(OrdemServico os, Servico servico, ItemServicoDTO dto) {
        ItemServicoOS novoItem = new ItemServicoOS();
        novoItem.setOrdemServico(os);
        novoItem.setServico(servico);
        novoItem.setQuantidadeHoras(dto.quantidadeHoras());
        novoItem.setValorUnitario(servico.getVlPrecoBase());
        novoItem.setValorTotal(servico.getVlPrecoBase() * dto.quantidadeHoras());
        return novoItem;
    }

    public static ItemPecaOS criarItemPecaOS(OrdemServico os, ItemEstoque itemEstoque, ItemPecaDTO dto) {
        ItemPecaOS novoItem = new ItemPecaOS();
        novoItem.setOrdemServico(os);
        novoItem.setItemEstoque(itemEstoque);
        novoItem.setQuantidade(dto.quantidade());
        novoItem.setValorUnitario(dto.valorUnitario());
        novoItem.setNome(dto.nome());
        novoItem.setDescricao(dto.descricao());
        novoItem.setValorTotal(dto.quantidade() * dto.valorUnitario());
        return novoItem;
    }
}
