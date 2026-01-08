package com.fix_it.persistence.repository;

import com.fix_it.persistence.entity.OrdemServicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository(value = "ordemServicoJpaRepository")
public interface OrdemServicoRepository extends JpaRepository<OrdemServicoEntity, UUID> {
    List<OrdemServicoEntity> findAllByCliente_CpfCnpj(String cpfCnpj);
}
