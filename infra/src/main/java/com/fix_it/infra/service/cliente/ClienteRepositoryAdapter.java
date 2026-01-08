package com.fix_it.infra.service.cliente;

import com.fix_it.core.domain.entity.Cliente;
import com.fix_it.infra.persistence.entity.ClienteEntity;
import com.fix_it.infra.persistence.repository.SpringDataClienteJpaRepository;
import com.fix_it.usecase.port.ClienteRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ClienteRepositoryAdapter implements ClienteRepository {

    private final SpringDataClienteJpaRepository clientRepository;
    private final ClienteMapper mapper;

    public ClienteRepositoryAdapter(SpringDataClienteJpaRepository clientRepository, ClienteMapper mapper) {
        this.clientRepository = clientRepository;
        this.mapper = mapper;
    }

    @Override
    public Cliente salvar(Cliente cliente) {
        ClienteEntity entity = mapper.toEntity(cliente);
        ClienteEntity saved = clientRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Cliente> buscarPorId(UUID id) {
        return clientRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Cliente> buscarPorCpfCnpj(String cpfCnpj) {
        return clientRepository.findByCpfCnpj(cpfCnpj).map(mapper::toDomain);
    }

    @Override
    public boolean existePorCpfCnpj(String cpfCnpj) {
        return clientRepository.existsByCpfCnpj(cpfCnpj);
    }

    @Override
    public List<Cliente> listarTodos() {
        return clientRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void removerPorId(UUID id) {
        clientRepository.deleteById(id);
    }
}
