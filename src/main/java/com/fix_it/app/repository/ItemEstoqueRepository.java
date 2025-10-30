package com.fix_it.app.repository;

import com.fix_it.app.model.ItemEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemEstoqueRepository extends JpaRepository<ItemEstoque, UUID> {

    List<ItemEstoque> findByNmItemEstoqueContainingIgnoreCase(String nome);

//    List<ItemEstoque> findByTpItem(String tpItem);

    List<ItemEstoque> findByOrderByNmItemEstoqueAsc();

    List<ItemEstoque> findByQtEstoqueAtualLessThanEqual(Integer qtEstoqueMinimo);
}

