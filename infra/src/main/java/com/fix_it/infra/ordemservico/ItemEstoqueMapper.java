package com.fix_it.infra.ordemservico;

import com.fix_it.core.domain.entity.ItemEstoque;
import com.fix_it.persistence.entity.ItemEstoqueEntity;
import org.springframework.stereotype.Component;

@Component
public class ItemEstoqueMapper {

    public ItemEstoqueEntity toEntity(ItemEstoque domain) {
        if (domain == null) return null;
        return new ItemEstoqueEntity(
            domain.getId(),
            domain.getNmItemEstoque(),
            domain.getDsItemEstoque(),
            domain.getVlPrecoUnitario(),
            domain.getQtEstoqueAtual(),
            domain.getQtEstoqueMinimo(),
            domain.getDthAtualizacao(),
            domain.getTipo(),
            domain.getAbaixoMinimo()
        );
    }

    public ItemEstoque toDomain(ItemEstoqueEntity entity) {
        if (entity == null) return null;
        return ItemEstoque.of(
            entity.getId(),
            entity.getNmItemEstoque(),
            entity.getDsItemEstoque(),
            entity.getVlPrecoUnitario(),
            entity.getQtEstoqueAtual(),
            entity.getQtEstoqueMinimo(),
            entity.getDthAtualizacao(),
            entity.getTipo(),
            entity.getAbaixoMinimo()
        );
    }
}
