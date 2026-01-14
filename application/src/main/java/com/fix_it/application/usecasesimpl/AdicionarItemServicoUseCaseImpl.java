package com.fix_it.application.usecasesimpl;

import com.fix_it.core.domain.entity.ItemServicoOS;
import com.fix_it.core.domain.entity.OrdemServico;
import com.fix_it.core.domain.entity.Servico;
import com.fix_it.core.domain.enums.SituacaoOrdemServico;
import com.fix_it.usecase.ordemservico.AdicionarItemServicoUseCase;
import com.fix_it.usecase.ordemservico.input.AdicionarItemServicoInput;
import com.fix_it.usecase.port.ItemPecaOSRepository;
import com.fix_it.usecase.port.ItemServicoOSRepository;
import com.fix_it.usecase.port.OrdemServicoRepository;
import com.fix_it.usecase.port.ServicoRepository;
import java.util.UUID;

public class AdicionarItemServicoUseCaseImpl implements AdicionarItemServicoUseCase {

    private final OrdemServicoRepository osRepository;
    private final ServicoRepository servicoRepository;
    private final ItemServicoOSRepository itemServicoRepository;
    private final ItemPecaOSRepository itemPecaRepository;

    public AdicionarItemServicoUseCaseImpl(OrdemServicoRepository osRepository, ServicoRepository servicoRepository, ItemServicoOSRepository itemServicoRepository, ItemPecaOSRepository itemPecaRepository) {
        this.osRepository = osRepository;
        this.servicoRepository = servicoRepository;
        this.itemServicoRepository = itemServicoRepository;
        this.itemPecaRepository = itemPecaRepository;
    }

    @Override
    public void executar(UUID osId, AdicionarItemServicoInput input) {
        OrdemServico os = osRepository.buscarPorId(osId)
            .orElseThrow(() -> new IllegalArgumentException("OS não encontrada"));

        validarStatusParaAlteracao(os);

        Servico servico = servicoRepository.buscarPorId(input.servicoId())
            .orElseThrow(() -> new IllegalArgumentException("Serviço não encontrado"));

        ItemServicoOS item = ItemServicoOS.novo(input.quantidadeHoras(), input.valorUnitario(), os, servico);
        itemServicoRepository.salvar(item);

        recalcular(os);
    }

    private void validarStatusParaAlteracao(OrdemServico os) {
        if (!(os.getSituacao().equals(SituacaoOrdemServico.RECEBIDA)
            || os.getSituacao().equals(SituacaoOrdemServico.EM_DIAGNOSTICO))) {
            throw new IllegalArgumentException("Não é possível alterar itens no status " + os.getSituacao());
        }
    }

    private void recalcular(OrdemServico os) {
        long totalServicos = itemServicoRepository.somarValorTotalPorOrdemServicoId(os.getId());
        long totalPecas = itemPecaRepository.somarValorTotalPorOrdemServicoId(os.getId());
        os.atualizarValores(totalServicos + totalPecas, os.getValorTotalFinal());
        osRepository.salvar(os);
    }
}
