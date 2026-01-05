package com.fix_it.app.repository;

import com.fix_it.app.model.ItemPecaOS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemPecaOSRepository extends JpaRepository<ItemPecaOS, UUID> {

    List<ItemPecaOS> findByOrdemServico_Id(UUID osId);

    @Query("""
               select coalesce(sum(i.valorTotal),0)
               from ItemPecaOS i
               where i.ordemServico.id = :osId
            """)
    long sumValorTotalByOsId(@Param("osId") UUID osId);
}

