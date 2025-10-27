package com.fix_it.app.repository;

import com.fix_it.app.model.ItemPecaOS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPecaOSRepository extends JpaRepository<ItemPecaOS, Long> {
    
//    List<ItemPecaOS> findByOrdemServicoSqOs(Long sqOs);
    
//    List<ItemPecaOS> findByItemEstoqueSqItemEstoque(Long sqItemEstoque);
}

