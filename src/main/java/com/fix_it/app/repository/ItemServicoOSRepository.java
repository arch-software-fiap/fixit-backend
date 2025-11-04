package com.fix_it.app.repository;

import com.fix_it.app.model.ItemServicoOS;
import com.fix_it.app.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemServicoOSRepository extends JpaRepository<ItemServicoOS, UUID> {

    List<ItemServicoOS> findByOrdemServico_Id(UUID osId);

    List<ItemServicoOS> findByServico(Servico servico);

    @Query("""
               select coalesce(sum(i.valorTotal),0)
               from ItemServicoOS i
               where i.ordemServico.id = :osId
            """)
    long sumValorTotalByOsId(@Param("osId") UUID osId);


    List<ItemServicoOS> findAllByOrdemServico_Id(UUID osId);

    Optional<ItemServicoOS> findByServico_Id(UUID servicoId);
}

