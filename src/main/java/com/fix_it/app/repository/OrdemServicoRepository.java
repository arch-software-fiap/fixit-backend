package com.fix_it.app.repository;

import com.fix_it.app.model.OrdemServico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrdemServicoRepository extends JpaRepository<OrdemServico, UUID> {

    @Override
    @EntityGraph(
            attributePaths = {
                    "cliente", "veiculo",
                    "itensServico.ordemServico",
                    "itensPeca.ordemServico"
            },
            type = EntityGraph.EntityGraphType.LOAD
    )
    Optional<OrdemServico> findById(UUID id);
}

