package com.fix_it.usecase.port;

import com.fix_it.core.domain.entity.Veiculo;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VeiculoRepository {
    Veiculo salvar(Veiculo veiculo);
    Optional<Veiculo> buscarPorId(UUID id);
    List<Veiculo> listarTodos();
    void removerPorId(UUID id);
    boolean existePorId(UUID id);
    boolean existePorPlaca(String placa);
}
