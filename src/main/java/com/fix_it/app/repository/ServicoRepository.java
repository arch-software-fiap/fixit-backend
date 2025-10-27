package com.fix_it.app.repository;

import com.fix_it.app.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
    
    List<Servico> findByNmServicoContainingIgnoreCase(String nome);
    
    List<Servico> findByOrderByNmServicoAsc();
}

