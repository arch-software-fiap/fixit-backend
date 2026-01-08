package com.fix_it.infra.ordemservico;

import com.fix_it.core.domain.entity.ItemEstoque;
import com.fix_it.persistence.entity.ItemEstoqueEntity;
import com.fix_it.persistence.repository.ItemEstoqueRepository;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.UUID;

@Component
public class ItemEstoqueRepositoryAdapter implements com.fix_it.usecase.port.ItemEstoqueRepository {

    private final ItemEstoqueRepository springRepository;
    private final ItemEstoqueMapper mapper;

    public ItemEstoqueRepositoryAdapter(ItemEstoqueRepository springRepository, ItemEstoqueMapper mapper) {
        this.springRepository = springRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<ItemEstoque> buscarPorId(UUID id) {
        return springRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public ItemEstoque salvar(ItemEstoque item) {
        ItemEstoqueEntity entity = mapper.toEntity(item);
        ItemEstoqueEntity salva = springRepository.save(entity);
        return mapper.toDomain(salva);
    }

    @Override
    public int baixarEstoqueSeDisponivel(UUID id, int quantidade) {
        return springRepository.baixarEstoqueSeDisponivel(id, quantidade);
    }

    @Override
    public void estornarEstoque(UUID id, int quantidade) {
        springRepository.estornarEstoque(id, quantidade);
    }
}
