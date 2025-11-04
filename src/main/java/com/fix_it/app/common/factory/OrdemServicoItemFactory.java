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

    public static ItemServicoOS atualizaItemServicoOS(ItemServicoOS i, Servico servico, ItemServicoDTO dto){
        i.setQuantidadeHoras(i.getQuantidadeHoras() + dto.quantidadeHoras());
        i.setValorTotal(i.getValorTotal() + servico.getVlPrecoBase() * dto.quantidadeHoras());
        return i;
    }


    public static ItemPecaOS criarItemPecaOS(OrdemServico os, ItemEstoque estoque, ItemPecaDTO dto) {
        ItemPecaOS item = new ItemPecaOS();
        item.setOrdemServico(os);
        item.setItemEstoque(estoque);

        item.setNome(estoque.getNmItemEstoque());
        item.setValorUnitario(estoque.getVlPrecoUnitario());

        item.setDescricao(dto.descricao() != null ? dto.descricao() : estoque.getDsItemEstoque());

        item.setQuantidade(dto.quantidade());

        long unit = estoque.getVlPrecoUnitario() == null ? 0L : estoque.getVlPrecoUnitario();
        long total = Math.multiplyExact(unit, dto.quantidade().longValue());
        item.setValorTotal(total);
        return item;
    }
}
