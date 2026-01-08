package com.fix_it.infra.veiculo;

import com.fix_it.core.domain.entity.Veiculo;
import com.fix_it.persistence.entity.VeiculoEntity;
import com.fix_it.persistence.repository.VeiculoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class VeiculoRepositoryAdapter implements com.fix_it.usecase.port.VeiculoRepository {

    private final VeiculoRepository springRepository;
    private final VeiculoMapper mapper;

    public VeiculoRepositoryAdapter(VeiculoRepository springRepository, VeiculoMapper mapper) {
        this.springRepository = springRepository;
        this.mapper = mapper;
    }

    @Override
    public Veiculo salvar(Veiculo veiculo) {
        VeiculoEntity entity = mapper.toEntity(veiculo);
        VeiculoEntity salvo = springRepository.save(entity);
        return mapper.toDomain(salvo);
    }

    @Override
    public Optional<Veiculo> buscarPorId(UUID id) {
        return springRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Veiculo> listarTodos() {
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

    @Override
    public boolean existePorPlaca(String placa) {
        return springRepository.existsByPlaca(placa);
    }
}
