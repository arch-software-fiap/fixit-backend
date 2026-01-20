package com.fix_it.infra.repository;

import com.fix_it.infra.domain.OrdemServicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataOrdemServicoRepository extends JpaRepository<OrdemServicoEntity, UUID> {
    List<OrdemServicoEntity> findAllByCliente_CpfCnpj(String cpfCnpj);

    @Query("SELECT os FROM OrdemServicoEntity os WHERE os.situacao NOT IN ('FINALIZADA', 'ENTREGUE') ORDER BY CASE os.situacao WHEN 'EM_EXECUCAO' THEN 0 WHEN 'AGUARDANDO_APROVACAO' THEN 1 WHEN 'EM_DIAGNOSTICO' THEN 2 WHEN 'RECEBIDA' THEN 3 ELSE 4 END, os.dataAberturaEm ASC")
    List<OrdemServicoEntity> listarParaAcompanhamento();

    @Query("SELECT os FROM OrdemServicoEntity os JOIN FETCH os.cliente JOIN FETCH os.veiculo WHERE os.id = :id")
    Optional<OrdemServicoEntity> findByIdWithDependencies(@Param("id") UUID id);
}
