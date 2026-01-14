package com.fix_it.usecase.port;

import com.fix_it.core.domain.entity.ItemPecaOS;
import java.util.List;
import java.util.UUID;

public interface ItemPecaOSRepository {
    ItemPecaOS salvar(ItemPecaOS item);
    List<ItemPecaOS> buscarPorOrdemServicoId(UUID osId);
    long somarValorTotalPorOrdemServicoId(UUID osId);
}
