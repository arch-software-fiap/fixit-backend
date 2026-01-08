package com.fix_it.infra.cliente;

import com.fix_it.core.domain.entity.Cliente;
import com.fix_it.persistence.entity.ClienteEntity;
import com.fix_it.usecase.port.ClienteRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ClienteRepositoryAdapter implements ClienteRepository {

    private final ClienteEntityRepository clienteEntityRepository;
    private final ClienteMapper mapper;

    public ClienteRepositoryAdapter(ClienteEntityRepository clienteEntityRepository,
                                    ClienteMapper mapper) {
        this.clienteEntityRepository = clienteEntityRepository;
        this.mapper = mapper;
    }

    @Override
    public Cliente salvar(Cliente cliente) {
        ClienteEntity entity = mapper.toEntity(cliente);
        ClienteEntity salvo = clienteEntityRepository.save(entity);
        return mapper.toDomain(salvo);
    }

    @Override
    public Optional<Cliente> buscarPorId(UUID id) {
        return clienteEntityRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Cliente> buscarPorCpfCnpj(String cpfCnpj) {
        return clienteEntityRepository.findByCpfCnpj(cpfCnpj).map(mapper::toDomain);
    }

    @Override
    public boolean existePorCpfCnpj(String cpfCnpj) {
        return clienteEntityRepository.existsByCpfCnpj(cpfCnpj);
    }

    @Override
    public List<Cliente> listarTodos() {
        return clienteEntityRepository.findAll().stream().map(mapper::toDomain).toList();
    }
}
