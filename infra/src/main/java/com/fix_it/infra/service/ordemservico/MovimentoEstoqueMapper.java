package com.fix_it.infra.service.ordemservico;

import com.fix_it.core.domain.entity.MovimentoEstoque;
import com.fix_it.infra.domain.MovimentoEstoqueEntity;
import org.springframework.stereotype.Component;

@Component
public class MovimentoEstoqueMapper {

    private final ItemEstoqueMapper itemEstoqueMapper;
    private final ItemPecaOSMapper itemPecaOSMapper;

    public MovimentoEstoqueMapper(ItemEstoqueMapper itemEstoqueMapper, ItemPecaOSMapper itemPecaOSMapper) {
        this.itemEstoqueMapper = itemEstoqueMapper;
        this.itemPecaOSMapper = itemPecaOSMapper;
    }

    public MovimentoEstoqueEntity toEntity(MovimentoEstoque domain) {
        if (domain == null) return null;
        return new MovimentoEstoqueEntity(
            domain.getId(),
            domain.getDthMovimento(),
            domain.getTipo(),
            domain.getQuantidade(),
            domain.getDescricao(),
            itemEstoqueMapper.toEntity(domain.getItemEstoque()),
            itemPecaOSMapper.toEntity(domain.getPeca())
        );
    }

    public MovimentoEstoque toDomain(MovimentoEstoqueEntity entity) {
        if (entity == null) return null;
        return MovimentoEstoque.of(
            entity.getId(),
            entity.getDthMovimento(),
            entity.getTipo(),
            entity.getQuantidade(),
            entity.getDescricao(),
            itemEstoqueMapper.toDomain(entity.getItemEstoque()),
            itemPecaOSMapper.toDomain(entity.getPeca())
        );
    }
}
