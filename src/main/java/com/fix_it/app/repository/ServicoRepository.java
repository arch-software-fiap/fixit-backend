package com.fix_it.app.repository;

import com.fix_it.app.model.Servico;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, UUID> {

    @Override
    @EntityGraph(
            attributePaths = {
                    "itensServicoOS.servico"
            },
            type = EntityGraph.EntityGraphType.LOAD
    )
    Optional<Servico> findById(UUID uuid);
}

