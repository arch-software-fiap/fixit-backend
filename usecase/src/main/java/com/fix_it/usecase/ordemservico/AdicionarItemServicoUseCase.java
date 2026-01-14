package com.fix_it.usecase.ordemservico;

import com.fix_it.usecase.ordemservico.input.AdicionarItemServicoInput;
import java.util.UUID;

public interface AdicionarItemServicoUseCase {
    void executar(UUID osId, AdicionarItemServicoInput input);
}
