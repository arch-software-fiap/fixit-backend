package com.fix_it.application.usecasesimpl;

import com.fix_it.core.domain.entity.Cliente;
import com.fix_it.core.domain.entity.OrdemServico;
import com.fix_it.core.domain.entity.Veiculo;
import com.fix_it.usecase.ordemservico.CriarOrdemServicoUseCase;
import com.fix_it.usecase.ordemservico.input.CriarOrdemServicoInput;
import com.fix_it.usecase.ordemservico.output.OrdemServicoOutput;
import com.fix_it.usecase.port.ClienteRepository;
import com.fix_it.usecase.port.OrdemServicoRepository;
import com.fix_it.usecase.port.VeiculoRepository;

public class CriarOrdemServicoUseCaseImpl implements CriarOrdemServicoUseCase {

    private final OrdemServicoRepository osRepository;
    private final ClienteRepository clienteRepository;
    private final VeiculoRepository veiculoRepository;

    public CriarOrdemServicoUseCaseImpl(OrdemServicoRepository osRepository, ClienteRepository clienteRepository, VeiculoRepository veiculoRepository) {
        this.osRepository = osRepository;
        this.clienteRepository = clienteRepository;
        this.veiculoRepository = veiculoRepository;
    }

    @Override
    public OrdemServicoOutput executar(CriarOrdemServicoInput input) {
        Cliente cliente = clienteRepository.buscarPorId(input.clienteId())
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        Veiculo veiculo = veiculoRepository.buscarPorId(input.veiculoId())
            .orElseThrow(() -> new IllegalArgumentException("Veículo não encontrado"));

        if (!veiculo.getCliente().getId().equals(cliente.getId())) {
            throw new IllegalArgumentException("Veículo não pertence ao cliente informado.");
        }

        OrdemServico os = OrdemServico.nova(input.descricao(), cliente, veiculo);
        OrdemServico salva = osRepository.salvar(os);

        return new OrdemServicoOutput(
            salva.getId(),
            salva.getSituacao(),
            salva.getDescricao(),
            salva.getDataAberturaEm(),
            salva.getDataFechamentoEm(),
            salva.getValorOrcamentoTotal(),
            salva.getValorTotalFinal(),
            salva.getCliente().getId(),
            salva.getVeiculo().getId()
        );
    }
}
