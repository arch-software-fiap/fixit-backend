package com.fix_it.usecase.port;

import com.fix_it.core.domain.entity.ItemServicoOS;
import java.util.List;
import java.util.UUID;

public interface ItemServicoOSRepository {
    ItemServicoOS salvar(ItemServicoOS item);
    List<ItemServicoOS> buscarPorOrdemServicoId(UUID osId);
    long somarValorTotalPorOrdemServicoId(UUID osId);
}
