package com.fix_it.persistence.repository;

import com.fix_it.persistence.entity.ItemPecaOSEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemPecaOSRepository extends JpaRepository<ItemPecaOSEntity, UUID> {
    List<ItemPecaOSEntity> findByOrdemServico_Id(UUID osId);

    @Query("""
               select coalesce(sum(i.valorTotal),0)
               from ItemPecaOSEntity i
               where i.ordemServico.id = :osId
            """)
    long sumValorTotalByOsId(@Param("osId") UUID osId);
}
