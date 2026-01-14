package com.fix_it.usecase.servico;

import com.fix_it.usecase.servico.input.AtualizarServicoInput;
import com.fix_it.usecase.servico.output.ServicoOutput;
import java.util.UUID;

public interface AtualizarServicoUseCase {
    ServicoOutput executar(UUID id, AtualizarServicoInput input);
}
