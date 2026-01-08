package com.fix_it.usecase.servico;

import com.fix_it.usecase.servico.input.CriarServicoInput;
import com.fix_it.usecase.servico.output.ServicoOutput;

public interface CriarServicoUseCase {
    ServicoOutput executar(CriarServicoInput input);
}
