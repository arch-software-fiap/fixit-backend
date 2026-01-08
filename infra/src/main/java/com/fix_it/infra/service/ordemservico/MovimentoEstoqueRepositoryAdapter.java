package com.fix_it.infra.service.ordemservico;

import com.fix_it.core.domain.entity.MovimentoEstoque;
import com.fix_it.infra.persistence.entity.MovimentoEstoqueEntity;
import com.fix_it.infra.persistence.repository.SpringDataMovimentoEstoqueRepository;
import org.springframework.stereotype.Component;

@Component
public class MovimentoEstoqueRepositoryAdapter implements com.fix_it.usecase.port.MovimentoEstoqueRepository {

    private final SpringDataMovimentoEstoqueRepository springRepository;
    private final MovimentoEstoqueMapper mapper;

    public MovimentoEstoqueRepositoryAdapter(SpringDataMovimentoEstoqueRepository springRepository, MovimentoEstoqueMapper mapper) {
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
