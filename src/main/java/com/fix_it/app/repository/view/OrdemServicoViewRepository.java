package com.fix_it.app.repository.view;

import com.fix_it.app.model.view.OrdemServicoView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrdemServicoViewRepository extends JpaRepository<OrdemServicoView, UUID> {
    Page<OrdemServicoView> findAllByCpfCnpj(String cpfCnpj, Pageable pageable);
}
