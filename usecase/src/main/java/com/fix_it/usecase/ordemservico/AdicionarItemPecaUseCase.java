package com.fix_it.usecase.ordemservico;

import com.fix_it.usecase.ordemservico.input.AdicionarItemPecaInput;
import java.util.UUID;

public interface AdicionarItemPecaUseCase {
    void executar(UUID osId, AdicionarItemPecaInput input);
}
