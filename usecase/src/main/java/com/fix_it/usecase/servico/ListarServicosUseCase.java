package com.fix_it.usecase.servico;

import com.fix_it.usecase.servico.output.ServicoOutput;
import java.util.List;

public interface ListarServicosUseCase {
    List<ServicoOutput> executar();
}
