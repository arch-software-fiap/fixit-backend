package com.fix_it.usecase.ordemservico;

import com.fix_it.usecase.ordemservico.input.CriarOrdemServicoInput;
import com.fix_it.usecase.ordemservico.output.OrdemServicoOutput;

public interface CriarOrdemServicoUseCase {
    OrdemServicoOutput executar(CriarOrdemServicoInput input);
}
