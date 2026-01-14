package com.fix_it.usecase.port;

import com.fix_it.core.domain.entity.ItemEstoque;
import java.util.Optional;
import java.util.UUID;

public interface ItemEstoqueRepository {
    Optional<ItemEstoque> buscarPorId(UUID id);
    ItemEstoque salvar(ItemEstoque item);
    int baixarEstoqueSeDisponivel(UUID id, int quantidade);
    void estornarEstoque(UUID id, int quantidade);
}
