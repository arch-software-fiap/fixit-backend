package com.fix_it.app.repository;

import com.fix_it.app.model.OrdemServico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrdemServicoRepository extends JpaRepository<OrdemServico, UUID> {

    Page<OrdemServico> findAllByCliente_CpfCnpj(String clienteCpfCnpj, Pageable pageable);


    @Query("""
              select os from OrdemServico os
              where os.id = :osId
                and os.cliente.cpfCnpj = :doc
            """)
    Optional<OrdemServico> findByIdAndClienteDoc(@Param("osId") UUID osId,
                                                 @Param("doc") String cpfCnpj);
}

