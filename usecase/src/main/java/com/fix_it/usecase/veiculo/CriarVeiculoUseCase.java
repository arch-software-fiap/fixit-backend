package com.fix_it.usecase.veiculo;

import com.fix_it.usecase.veiculo.input.CriarVeiculoInput;
import com.fix_it.usecase.veiculo.output.VeiculoOutput;

public interface CriarVeiculoUseCase {
    VeiculoOutput executar(CriarVeiculoInput input);
}
