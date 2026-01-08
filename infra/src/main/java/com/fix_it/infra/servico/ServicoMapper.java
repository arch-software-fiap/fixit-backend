package com.fix_it.infra.servico;

import com.fix_it.core.domain.entity.Servico;
import com.fix_it.persistence.entity.ServicoEntity;
import org.springframework.stereotype.Component;

@Component
public class ServicoMapper {

    public ServicoEntity toEntity(Servico domain) {
        if (domain == null) return null;
        return new ServicoEntity(
            domain.getId(),
            domain.getNmServico(),
            domain.getDsServico(),
            domain.getVlPrecoBase(),
            domain.getDthAtualizacao()
        );
    }

    public Servico toDomain(ServicoEntity entity) {
        if (entity == null) return null;
        return Servico.of(
            entity.getId(),
            entity.getNmServico(),
            entity.getDsServico(),
            entity.getVlPrecoBase(),
            entity.getDthAtualizacao()
        );
    }
}
