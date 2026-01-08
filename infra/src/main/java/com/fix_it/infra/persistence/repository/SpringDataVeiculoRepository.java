package com.fix_it.infra.persistence.repository;

import com.fix_it.infra.persistence.entity.VeiculoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataVeiculoRepository extends JpaRepository<VeiculoEntity, UUID> {
    boolean existsByPlaca(String placa);
}
