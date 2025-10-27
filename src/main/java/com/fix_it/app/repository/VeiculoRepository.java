package com.fix_it.app.repository;

import com.fix_it.app.model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    
    Optional<Veiculo> findByPlaca(String placa);
    
    boolean existsByPlaca(String placa);
    
//    List<Veiculo> findByClienteSqCliente(Long sqCliente);
}

