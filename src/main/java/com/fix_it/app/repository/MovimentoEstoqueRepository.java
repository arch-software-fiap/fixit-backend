package com.fix_it.app.repository;

import com.fix_it.app.model.MovimentoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimentoEstoqueRepository extends JpaRepository<MovimentoEstoque, Long> {
    
//    List<MovimentoEstoque> findByItemEstoqueSqItemEstoque(Long sqItemEstoque);
    
    List<MovimentoEstoque> findByNmTipoMovimento(String nmTipoMovimento);
    
    List<MovimentoEstoque> findByDthMovimentoBetween(LocalDateTime inicio, LocalDateTime fim);
    
//    List<MovimentoEstoque> findByItemPecaOSSqItemPecaOS(Long sqItemPecaOS);
}

