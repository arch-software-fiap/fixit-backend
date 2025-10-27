package com.fix_it.app.repository;

import com.fix_it.app.model.ItemServicoOS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemServicoOSRepository extends JpaRepository<ItemServicoOS, Long> {
    
    List<ItemServicoOS> findByOrdemServicoSqOs(Long sqOs);
    
    List<ItemServicoOS> findByServicoSqServico(Long sqServico);
}

