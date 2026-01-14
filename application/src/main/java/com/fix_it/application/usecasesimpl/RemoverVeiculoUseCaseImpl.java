package com.fix_it.application.usecasesimpl;

import com.fix_it.usecase.port.VeiculoRepository;
import com.fix_it.usecase.veiculo.RemoverVeiculoUseCase;
import java.util.UUID;

public class RemoverVeiculoUseCaseImpl implements RemoverVeiculoUseCase {

    private final VeiculoRepository repository;

    public RemoverVeiculoUseCaseImpl(VeiculoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void executar(UUID id) {
        if (!repository.existePorId(id)) {
            throw new IllegalArgumentException("Veículo não encontrado");
        }
        repository.removerPorId(id);
    }
}
