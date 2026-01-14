package com.fix_it.application.usecasesimpl;

import com.fix_it.core.domain.entity.Veiculo;
import com.fix_it.usecase.port.VeiculoRepository;
import com.fix_it.usecase.veiculo.BuscarVeiculoPorIdUseCase;
import com.fix_it.usecase.veiculo.output.VeiculoOutput;
import java.util.UUID;

public class BuscarVeiculoPorIdUseCaseImpl implements BuscarVeiculoPorIdUseCase {

    private final VeiculoRepository repository;

    public BuscarVeiculoPorIdUseCaseImpl(VeiculoRepository repository) {
        this.repository = repository;
    }

    @Override
    public VeiculoOutput executar(UUID id) {
        Veiculo veiculo = repository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Veículo não encontrado"));

        return new VeiculoOutput(
            veiculo.getId(),
            veiculo.getNmVeiculo(),
            veiculo.getDsVeiculo(),
            veiculo.getPlaca(),
            veiculo.getMarca(),
            veiculo.getModelo(),
            veiculo.getAno(),
            veiculo.getDthCadastro(),
            veiculo.getCliente().getId()
        );
    }
}
