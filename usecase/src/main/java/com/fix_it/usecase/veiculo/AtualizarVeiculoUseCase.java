package com.fix_it.usecase.veiculo;

import com.fix_it.usecase.veiculo.input.AtualizarVeiculoInput;
import com.fix_it.usecase.veiculo.output.VeiculoOutput;
import java.util.UUID;

public interface AtualizarVeiculoUseCase {
    VeiculoOutput executar(UUID id, AtualizarVeiculoInput input);
}
