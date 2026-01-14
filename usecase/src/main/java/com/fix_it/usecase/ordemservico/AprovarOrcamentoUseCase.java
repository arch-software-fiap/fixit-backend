package com.fix_it.usecase.ordemservico;

import java.util.UUID;

public interface AprovarOrcamentoUseCase {
    void executar(UUID id, boolean aprovado);
}
