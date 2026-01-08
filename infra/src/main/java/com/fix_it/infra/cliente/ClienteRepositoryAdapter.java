package com.fix_it.infra.cliente;

import com.fix_it.core.domain.entity.Cliente;
import com.fix_it.usecase.port.ClienteRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ClienteRepositoryAdapter implements ClienteRepository {

    private final SpringDataClienteJpaRepository clienteJpaRepository;
    private final ClienteMapper mapper;

    public ClienteRepositoryAdapter(SpringDataClienteJpaRepository clienteJpaRepository, ClienteMapper mapper) {
        this.clienteJpaRepository = clienteJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Cliente salvar(Cliente cliente) {
        ClienteEntity entity = mapper.toEntity(cliente);
        ClienteEntity saved = clienteJpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Cliente> buscarPorId(UUID id) {
        return clienteJpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Cliente> buscarPorCpfCnpj(String cpfCnpj) {
        return clienteJpaRepository.findByCpfCnpj(cpfCnpj).map(mapper::toDomain);
    }

    @Override
    public boolean existePorCpfCnpj(String cpfCnpj) {
        return clienteJpaRepository.existsByCpfCnpj(cpfCnpj);
    }

    @Override
    public List<Cliente> listarTodos() {
        return clienteJpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void removerPorId(UUID id) {
        clienteJpaRepository.deleteById(id);
    }
}
