package com.fix_it.infra.ordemservico;

import com.fix_it.core.domain.entity.ItemServicoOS;
import com.fix_it.persistence.entity.ItemServicoOSEntity;
import com.fix_it.persistence.repository.ItemServicoOSRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ItemServicoOSRepositoryAdapter implements com.fix_it.usecase.port.ItemServicoOSRepository {

    private final ItemServicoOSRepository springRepository;
    private final ItemServicoOSMapper mapper;

    public ItemServicoOSRepositoryAdapter(ItemServicoOSRepository springRepository, ItemServicoOSMapper mapper) {
        this.springRepository = springRepository;
        this.mapper = mapper;
    }

    @Override
    public ItemServicoOS salvar(ItemServicoOS item) {
        ItemServicoOSEntity entity = mapper.toEntity(item);
        ItemServicoOSEntity salvo = springRepository.save(entity);
        return mapper.toDomain(salvo);
    }

    @Override
    public List<ItemServicoOS> buscarPorOrdemServicoId(UUID osId) {
        return springRepository.findByOrdemServico_Id(osId).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public long somarValorTotalPorOrdemServicoId(UUID osId) {
        return springRepository.sumValorTotalByOsId(osId);
    }
}
