package com.fix_it.app.repository;

import com.fix_it.app.model.MovimentoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MovimentoEstoqueRepository extends JpaRepository<MovimentoEstoque, UUID> {

}

