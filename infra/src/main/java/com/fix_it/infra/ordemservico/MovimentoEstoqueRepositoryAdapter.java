package com.fix_it.infra.ordemservico;

import com.fix_it.core.domain.entity.MovimentoEstoque;
import com.fix_it.persistence.entity.MovimentoEstoqueEntity;
import com.fix_it.persistence.repository.MovimentoEstoqueRepository;
import org.springframework.stereotype.Component;

@Component
public class MovimentoEstoqueRepositoryAdapter implements com.fix_it.usecase.port.MovimentoEstoqueRepository {

    private final MovimentoEstoqueRepository springRepository;
    private final MovimentoEstoqueMapper mapper;

    public MovimentoEstoqueRepositoryAdapter(MovimentoEstoqueRepository springRepository, MovimentoEstoqueMapper mapper) {
        this.springRepository = springRepository;
        this.mapper = mapper;
    }

    @Override
    public MovimentoEstoque salvar(MovimentoEstoque movimento) {
        MovimentoEstoqueEntity entity = mapper.toEntity(movimento);
        MovimentoEstoqueEntity salvo = springRepository.save(entity);
        return mapper.toDomain(salvo);
    }
}
