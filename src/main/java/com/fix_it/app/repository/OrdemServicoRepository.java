package com.fix_it.app.repository;

import com.fix_it.app.model.OrdemServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {
    
    List<OrdemServico> findByClienteSqCliente(Long sqCliente);
    
    List<OrdemServico> findByVeiculoSqVeiculo(Long sqVeiculo);
    
    List<OrdemServico> findByNmStatus(String nmStatus);
}

