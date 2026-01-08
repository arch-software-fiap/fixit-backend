package com.fix_it.app.repository.view;

import com.fix_it.app.model.view.OrdemServicoView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrdemServicoViewRepository extends JpaRepository<OrdemServicoView, UUID> {
    Optional<OrdemServicoView> findByCpfCnpjAndId(String cpfCnpj, UUID id);
}
