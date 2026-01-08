package com.fix_it.infra.cliente;

import com.fix_it.persistence.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteEntityRepository extends JpaRepository<ClienteEntity, UUID> {

    Optional<ClienteEntity> findByCpfCnpj(String cpfCnpj);

    boolean existsByCpfCnpj(String cpfCnpj);
}

