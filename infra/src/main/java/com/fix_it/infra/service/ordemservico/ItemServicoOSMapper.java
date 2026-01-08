package com.fix_it.infra.service.ordemservico;

import com.fix_it.core.domain.entity.ItemServicoOS;
import com.fix_it.infra.service.servico.ServicoMapper;
import com.fix_it.infra.persistence.entity.ItemServicoOSEntity;
import org.springframework.stereotype.Component;

@Component
public class ItemServicoOSMapper {

    private final OrdemServicoMapper ordemServicoMapper;
    private final ServicoMapper servicoMapper;

    public ItemServicoOSMapper(OrdemServicoMapper ordemServicoMapper, ServicoMapper servicoMapper) {
        this.ordemServicoMapper = ordemServicoMapper;
        this.servicoMapper = servicoMapper;
    }

    public ItemServicoOSEntity toEntity(ItemServicoOS domain) {
        if (domain == null) return null;
        return new ItemServicoOSEntity(
            domain.getId(),
            domain.getQuantidadeHoras(),
            domain.getValorUnitario(),
            domain.getValorTotal(),
            ordemServicoMapper.toEntity(domain.getOrdemServico()),
            servicoMapper.toEntity(domain.getServico())
        );
    }

    public ItemServicoOS toDomain(ItemServicoOSEntity entity) {
        if (entity == null) return null;
        return ItemServicoOS.of(
            entity.getId(),
            entity.getQuantidadeHoras(),
            entity.getValorUnitario(),
            entity.getValorTotal(),
            ordemServicoMapper.toDomain(entity.getOrdemServico()),
            servicoMapper.toDomain(entity.getServico())
        );
    }
}
