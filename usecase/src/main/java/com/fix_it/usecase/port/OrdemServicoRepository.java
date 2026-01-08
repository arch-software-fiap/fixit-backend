package com.fix_it.usecase.port;

import com.fix_it.core.domain.entity.OrdemServico;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrdemServicoRepository {
    OrdemServico salvar(OrdemServico os);
    Optional<OrdemServico> buscarPorId(UUID id);
    List<OrdemServico> listarTodas();
    List<OrdemServico> buscarPorCpfCnpjCliente(String cpfCnpj);
}
