package com.fix_it.infra.persistence.repository;

import com.fix_it.infra.persistence.entity.MovimentoEstoqueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataMovimentoEstoqueRepository extends JpaRepository<MovimentoEstoqueEntity, UUID> {
}
