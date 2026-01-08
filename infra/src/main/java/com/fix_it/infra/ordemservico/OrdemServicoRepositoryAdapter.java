package com.fix_it.infra.ordemservico;

import com.fix_it.core.domain.entity.OrdemServico;
import com.fix_it.persistence.entity.OrdemServicoEntity;
import com.fix_it.persistence.repository.OrdemServicoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrdemServicoRepositoryAdapter implements com.fix_it.usecase.port.OrdemServicoRepository {

    private final OrdemServicoRepository ordemServicoRepository;
    private final OrdemServicoMapper mapper;

    public OrdemServicoRepositoryAdapter(OrdemServicoRepository ordemServicoRepository, OrdemServicoMapper mapper) {
        this.ordemServicoRepository = ordemServicoRepository;
        this.mapper = mapper;
    }

    @Override
    public OrdemServico salvar(OrdemServico os) {
        OrdemServicoEntity entity = mapper.toEntity(os);
        OrdemServicoEntity salva = ordemServicoRepository.save(entity);
        return mapper.toDomain(salva);
    }

    @Override
    public Optional<OrdemServico> buscarPorId(UUID id) {
        return ordemServicoRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<OrdemServico> listarTodas() {
        return ordemServicoRepository.findAll().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<OrdemServico> buscarPorCpfCnpjCliente(String cpfCnpj) {
        return ordemServicoRepository.findAllByCliente_CpfCnpj(cpfCnpj).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }
}
