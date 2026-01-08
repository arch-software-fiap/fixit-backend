package com.fix_it.infra.persistence.repository;

import com.fix_it.infra.persistence.entity.ItemEstoqueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataItemEstoqueRepository extends JpaRepository<ItemEstoqueEntity, UUID> {
    @Modifying
    @Query("""
                UPDATE ItemEstoqueEntity i
                   SET i.qtEstoqueAtual = i.qtEstoqueAtual - :qtd,
                       i.dthAtualizacao = CURRENT_TIMESTAMP
                 WHERE i.id = :id
                   AND i.qtEstoqueAtual >= :qtd
            """)
    int baixarEstoqueSeDisponivel(@Param("id") UUID id, @Param("qtd") int qtd);

    @Modifying
    @Query("""
                UPDATE ItemEstoqueEntity i
                   SET i.qtEstoqueAtual = i.qtEstoqueAtual + :qtd,
                       i.dthAtualizacao = CURRENT_TIMESTAMP
                 WHERE i.id = :id
            """)
    int estornarEstoque(@Param("id") UUID id, @Param("qtd") int qtd);
}
