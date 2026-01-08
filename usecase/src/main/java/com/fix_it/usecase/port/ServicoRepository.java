package com.fix_it.usecase.port;

import com.fix_it.core.domain.entity.Servico;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServicoRepository {
    Servico salvar(Servico servico);
    Optional<Servico> buscarPorId(UUID id);
    List<Servico> listarTodos();
    void removerPorId(UUID id);
    boolean existePorId(UUID id);
}
