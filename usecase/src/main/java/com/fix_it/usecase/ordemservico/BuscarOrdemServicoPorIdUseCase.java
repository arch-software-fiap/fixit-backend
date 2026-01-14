package com.fix_it.usecase.ordemservico;

import com.fix_it.usecase.ordemservico.output.OrdemServicoOutput;
import java.util.UUID;

public interface BuscarOrdemServicoPorIdUseCase {
    OrdemServicoOutput executar(UUID id);
}
