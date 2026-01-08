package com.fix_it.application.usecasesimpl;

import com.fix_it.core.domain.entity.Veiculo;
import com.fix_it.usecase.port.VeiculoRepository;
import com.fix_it.usecase.veiculo.AtualizarVeiculoUseCase;
import com.fix_it.usecase.veiculo.input.AtualizarVeiculoInput;
import com.fix_it.usecase.veiculo.output.VeiculoOutput;
import java.util.UUID;

public class AtualizarVeiculoUseCaseImpl implements AtualizarVeiculoUseCase {

    private final VeiculoRepository repository;

    public AtualizarVeiculoUseCaseImpl(VeiculoRepository repository) {
        this.repository = repository;
    }

    @Override
    public VeiculoOutput executar(UUID id, AtualizarVeiculoInput input) {
        Veiculo veiculo = repository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Veículo não encontrado"));

        veiculo.atualizar(
            input.nmVeiculo(),
            input.dsVeiculo(),
            input.placa(),
            input.marca(),
            input.modelo(),
            input.ano()
        );

        Veiculo salvo = repository.salvar(veiculo);

        return new VeiculoOutput(
            salvo.getId(),
            salvo.getNmVeiculo(),
            salvo.getDsVeiculo(),
            salvo.getPlaca(),
            salvo.getMarca(),
            salvo.getModelo(),
            salvo.getAno(),
            salvo.getDthCadastro(),
            salvo.getCliente().getId()
        );
    }
}
