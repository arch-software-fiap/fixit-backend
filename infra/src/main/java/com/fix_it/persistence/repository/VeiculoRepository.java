package com.fix_it.persistence.repository;

import com.fix_it.persistence.entity.VeiculoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VeiculoRepository extends JpaRepository<VeiculoEntity, UUID> {
    boolean existsByPlaca(String placa);
}
