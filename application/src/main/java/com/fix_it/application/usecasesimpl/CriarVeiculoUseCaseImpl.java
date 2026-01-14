package com.fix_it.application.usecasesimpl;

import com.fix_it.core.domain.entity.Cliente;
import com.fix_it.core.domain.entity.Veiculo;
import com.fix_it.usecase.port.ClienteRepository;
import com.fix_it.usecase.port.VeiculoRepository;
import com.fix_it.usecase.veiculo.CriarVeiculoUseCase;
import com.fix_it.usecase.veiculo.input.CriarVeiculoInput;
import com.fix_it.usecase.veiculo.output.VeiculoOutput;

public class CriarVeiculoUseCaseImpl implements CriarVeiculoUseCase {

    private final VeiculoRepository veiculoRepository;
    private final ClienteRepository clienteRepository;

    public CriarVeiculoUseCaseImpl(VeiculoRepository veiculoRepository, ClienteRepository clienteRepository) {
        this.veiculoRepository = veiculoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public VeiculoOutput executar(CriarVeiculoInput input) {
        if (veiculoRepository.existePorPlaca(input.placa())) {
            throw new IllegalArgumentException("Veículo já cadastrado com esta placa");
        }

        Cliente cliente = clienteRepository.buscarPorId(input.clienteId())
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        Veiculo veiculo = Veiculo.novo(
            input.nmVeiculo(),
            input.dsVeiculo(),
            input.placa(),
            input.marca(),
            input.modelo(),
            input.ano(),
            cliente
        );

        Veiculo salvo = veiculoRepository.salvar(veiculo);

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
