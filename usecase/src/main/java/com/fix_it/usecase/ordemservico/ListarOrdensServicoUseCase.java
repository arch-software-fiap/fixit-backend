package com.fix_it.usecase.ordemservico;

import com.fix_it.usecase.ordemservico.output.OrdemServicoOutput;
import java.util.List;

public interface ListarOrdensServicoUseCase {
    List<OrdemServicoOutput> executar();
}
