package com.fix_it.application.usecasesimpl;

import com.fix_it.core.domain.entity.Cliente;
import com.fix_it.core.domain.entity.ItemEstoque;
import com.fix_it.core.domain.entity.ItemPecaOS;
import com.fix_it.core.domain.entity.ItemServicoOS;
import com.fix_it.core.domain.entity.OrdemServico;
import com.fix_it.core.domain.entity.Servico;
import com.fix_it.core.domain.entity.Veiculo;
import com.fix_it.usecase.ordemservico.CriarOrdemServicoUseCase;
import com.fix_it.usecase.ordemservico.input.CriarOrdemServicoInput;
import com.fix_it.usecase.ordemservico.output.OrdemServicoOutput;
import com.fix_it.usecase.port.ClienteRepository;
import com.fix_it.usecase.port.ItemEstoqueRepository;
import com.fix_it.usecase.port.ItemPecaOSRepository;
import com.fix_it.usecase.port.ItemServicoOSRepository;
import com.fix_it.usecase.port.OrdemServicoRepository;
import com.fix_it.usecase.port.ServicoRepository;
import com.fix_it.usecase.port.VeiculoRepository;
import java.util.ArrayList;
import java.util.List;

public class CriarOrdemServicoUseCaseImpl implements CriarOrdemServicoUseCase {

    private final OrdemServicoRepository osRepository;
    private final ClienteRepository clienteRepository;
    private final VeiculoRepository veiculoRepository;
    private final ServicoRepository servicoRepository;
    private final ItemServicoOSRepository itemServicoOSRepository;
    private final ItemEstoqueRepository itemEstoqueRepository;
    private final ItemPecaOSRepository itemPecaOSRepository;

    public CriarOrdemServicoUseCaseImpl(OrdemServicoRepository osRepository, ClienteRepository clienteRepository, VeiculoRepository veiculoRepository, ServicoRepository servicoRepository, ItemServicoOSRepository itemServicoOSRepository, ItemEstoqueRepository itemEstoqueRepository, ItemPecaOSRepository itemPecaOSRepository) {
        this.osRepository = osRepository;
        this.clienteRepository = clienteRepository;
        this.veiculoRepository = veiculoRepository;
        this.servicoRepository = servicoRepository;
        this.itemServicoOSRepository = itemServicoOSRepository;
        this.itemEstoqueRepository = itemEstoqueRepository;
        this.itemPecaOSRepository = itemPecaOSRepository;
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

        List<ItemServicoOS> servicos = new ArrayList<>();
        if (input.servicos() != null) {
            input.servicos().forEach(servicoInput -> {
                Servico servico = servicoRepository.buscarPorId(servicoInput.id())
                    .orElseThrow(() -> new IllegalArgumentException("Serviço não encontrado"));
                ItemServicoOS itemServicoOS = ItemServicoOS.novo(servicoInput.quantidade(), servico.getValorPrecoBase(), salva, servico);
                itemServicoOSRepository.salvar(itemServicoOS);
                servicos.add(itemServicoOS);
            });
        }

        List<ItemPecaOS> pecas = new ArrayList<>();
        if (input.pecas() != null) {
            input.pecas().forEach(pecaInput -> {
                ItemEstoque itemEstoque = itemEstoqueRepository.buscarPorId(pecaInput.id())
                    .orElseThrow(() -> new IllegalArgumentException("Peça não encontrada"));
                ItemPecaOS itemPecaOS = ItemPecaOS.novo(itemEstoque.getNome(), itemEstoque.getDescricao(), pecaInput.quantidade(), itemEstoque.getValor(), salva, itemEstoque);
                itemPecaOSRepository.salvar(itemPecaOS);
                pecas.add(itemPecaOS);
            });
        }

        long valorTotalServicos = servicos.stream().mapToLong(ItemServicoOS::getValorTotal).sum();
        long valorTotalPecas = pecas.stream().mapToLong(ItemPecaOS::getValorTotal).sum();
        salva.atualizarValores(valorTotalServicos + valorTotalPecas, 0L);
        osRepository.salvar(salva);

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
