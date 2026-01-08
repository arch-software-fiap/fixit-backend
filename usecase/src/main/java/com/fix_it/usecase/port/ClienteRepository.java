package com.fix_it.usecase.port;

import com.fix_it.core.domain.entity.Cliente;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClienteRepository {

    Cliente salvar(Cliente cliente);

    Optional<Cliente> buscarPorId(UUID id);

    Optional<Cliente> buscarPorCpfCnpj(String cpfCnpj);

    boolean existePorCpfCnpj(String cpfCnpj);

    List<Cliente> listarTodos();

    void removerPorId(UUID id);
}
