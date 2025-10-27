package com.fix_it.app.repository;

import com.fix_it.app.model.Orcamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {
    
//    Optional<Orcamento> findByOrdemServicoSqOs(Long sqOs);
    
    List<Orcamento> findByNmStatus(String nmStatus);
    
//    boolean existsByOrdemServicoSqOs(Long sqOs);
}

