package com.fix_it.infra.repository;

import com.fix_it.infra.domain.VeiculoEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataVeiculoRepository extends JpaRepository<VeiculoEntity, UUID> {
    boolean existsByPlaca(String placa);

    @EntityGraph(attributePaths = "cliente")
    Optional<VeiculoEntity> findById(UUID id);
}
