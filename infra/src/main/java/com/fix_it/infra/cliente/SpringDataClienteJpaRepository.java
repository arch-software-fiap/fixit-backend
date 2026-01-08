package com.fix_it.infra.cliente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataClienteJpaRepository extends JpaRepository<ClienteEntity, UUID> {

    boolean existsByCpfCnpj(String cpfCnpj);

    Optional<ClienteEntity> findByCpfCnpj(String cpfCnpj);
}

