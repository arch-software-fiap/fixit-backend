package com.fix_it.usecase.veiculo;

import com.fix_it.usecase.veiculo.output.VeiculoOutput;
import java.util.List;

public interface ListarVeiculosUseCase {
    List<VeiculoOutput> executar();
}
