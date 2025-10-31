package com.fix_it.app.repository;

import com.fix_it.app.model.ItemEstoque;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemEstoqueRepository extends JpaRepository<ItemEstoque, UUID> {

    @Override
    @EntityGraph(
            attributePaths = {
                    "movimentosEstoque.itemEstoque",
                    "itensPecaOS.itemEstoque"
            },
            type = EntityGraph.EntityGraphType.LOAD
    )
    Optional<ItemEstoque> findById(UUID uuid);
}

