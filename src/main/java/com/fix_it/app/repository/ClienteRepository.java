package com.fix_it.app.repository;

import com.fix_it.app.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    Optional<Cliente> findByCpfCnpj(String cpfCnpj);
    
    boolean existsByCpfCnpj(String cpfCnpj);
}

