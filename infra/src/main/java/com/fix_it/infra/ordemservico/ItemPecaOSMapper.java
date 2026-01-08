package com.fix_it.infra.ordemservico;

import com.fix_it.core.domain.entity.ItemPecaOS;
import com.fix_it.persistence.entity.ItemPecaOSEntity;
import org.springframework.stereotype.Component;

@Component
public class ItemPecaOSMapper {

    private final OrdemServicoMapper ordemServicoMapper;
    private final ItemEstoqueMapper itemEstoqueMapper;

    public ItemPecaOSMapper(OrdemServicoMapper ordemServicoMapper, ItemEstoqueMapper itemEstoqueMapper) {
        this.ordemServicoMapper = ordemServicoMapper;
        this.itemEstoqueMapper = itemEstoqueMapper;
    }

    public ItemPecaOSEntity toEntity(ItemPecaOS domain) {
        if (domain == null) return null;
        return new ItemPecaOSEntity(
            domain.getId(),
            domain.getNome(),
            domain.getDescricao(),
            domain.getQuantidade(),
            domain.getValorUnitario(),
            domain.getValorTotal(),
            ordemServicoMapper.toEntity(domain.getOrdemServico()),
            itemEstoqueMapper.toEntity(domain.getItemEstoque())
        );
    }

    public ItemPecaOS toDomain(ItemPecaOSEntity entity) {
        if (entity == null) return null;
        return ItemPecaOS.of(
            entity.getId(),
            entity.getNome(),
            entity.getDescricao(),
            entity.getQuantidade(),
            entity.getValorUnitario(),
            entity.getValorTotal(),
            ordemServicoMapper.toDomain(entity.getOrdemServico()),
            itemEstoqueMapper.toDomain(entity.getItemEstoque())
        );
    }
}
