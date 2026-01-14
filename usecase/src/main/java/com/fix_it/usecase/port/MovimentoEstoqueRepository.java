package com.fix_it.usecase.port;

import com.fix_it.core.domain.entity.MovimentoEstoque;

public interface MovimentoEstoqueRepository {
    MovimentoEstoque salvar(MovimentoEstoque movimento);
}
