package com.fix_it.infra.service.ordemservico;

import com.fix_it.core.domain.entity.ItemPecaOS;
import com.fix_it.infra.domain.ItemPecaOSEntity;
import com.fix_it.infra.repository.SpringDataItemPecaOSRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ItemPecaOSRepositoryAdapter implements com.fix_it.usecase.port.ItemPecaOSRepository {

    private final SpringDataItemPecaOSRepository springRepository;
    private final ItemPecaOSMapper mapper;

    public ItemPecaOSRepositoryAdapter(SpringDataItemPecaOSRepository springRepository, ItemPecaOSMapper mapper) {
        this.springRepository = springRepository;
        this.mapper = mapper;
    }

    @Override
    public ItemPecaOS salvar(ItemPecaOS item) {
        ItemPecaOSEntity entity = mapper.toEntity(item);
        ItemPecaOSEntity salvo = springRepository.save(entity);
        return mapper.toDomain(salvo);
    }

    @Override
    public List<ItemPecaOS> buscarPorOrdemServicoId(UUID osId) {
        return springRepository.findByOrdemServico_Id(osId).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public long somarValorTotalPorOrdemServicoId(UUID osId) {
        return springRepository.sumValorTotalByOsId(osId);
    }
}
