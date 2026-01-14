package com.fix_it.infra.service.servico;

import com.fix_it.core.domain.entity.Servico;
import com.fix_it.infra.domain.ServicoEntity;
import com.fix_it.infra.repository.SpringDataServicoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ServicoRepositoryAdapter implements com.fix_it.usecase.port.ServicoRepository {

    private final SpringDataServicoRepository springRepository;
    private final ServicoMapper mapper;

    public ServicoRepositoryAdapter(SpringDataServicoRepository springRepository, ServicoMapper mapper) {
        this.springRepository = springRepository;
        this.mapper = mapper;
    }

    @Override
    public Servico salvar(Servico servico) {
        ServicoEntity entity = mapper.toEntity(servico);
        ServicoEntity salvo = springRepository.save(entity);
        return mapper.toDomain(salvo);
    }

    @Override
    public Optional<Servico> buscarPorId(UUID id) {
        return springRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Servico> listarTodos() {
        return springRepository.findAll().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public void removerPorId(UUID id) {
        springRepository.deleteById(id);
    }

    @Override
    public boolean existePorId(UUID id) {
        return springRepository.existsById(id);
    }
}
