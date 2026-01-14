package com.fix_it.infra.service.ordemservico;

import com.fix_it.core.domain.entity.ItemEstoque;
import com.fix_it.infra.domain.ItemEstoqueEntity;
import org.springframework.stereotype.Component;

@Component
public class ItemEstoqueMapper {

    public ItemEstoqueEntity toEntity(ItemEstoque domain) {
        if (domain == null) return null;
        return new ItemEstoqueEntity(
            domain.getId(),
            domain.getNome(),
            domain.getDescricao(),
            domain.getValor(),
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
            entity.getNome(),
            entity.getDescricao(),
            entity.getValor(),
            entity.getQtEstoqueAtual(),
            entity.getQtEstoqueMinimo(),
            entity.getDthAtualizacao(),
            entity.getTipo(),
            entity.getAbaixoMinimo()
        );
    }
}
