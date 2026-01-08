package com.fix_it.usecase.veiculo;

import com.fix_it.usecase.veiculo.output.VeiculoOutput;
import java.util.UUID;

public interface BuscarVeiculoPorIdUseCase {
    VeiculoOutput executar(UUID id);
}
