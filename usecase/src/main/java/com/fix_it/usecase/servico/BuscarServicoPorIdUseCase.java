package com.fix_it.usecase.servico;

import com.fix_it.usecase.servico.output.ServicoOutput;
import java.util.UUID;

public interface BuscarServicoPorIdUseCase {
    ServicoOutput executar(UUID id);
}
