package com.fix_it.application.usecasesimpl;

import com.fix_it.core.domain.entity.ItemEstoque;
import com.fix_it.core.domain.entity.ItemPecaOS;
import com.fix_it.core.domain.entity.MovimentoEstoque;
import com.fix_it.core.domain.entity.OrdemServico;
import com.fix_it.core.domain.enums.SituacaoOrdemServico;
import com.fix_it.core.domain.enums.TipoMovimentoEstoque;
import com.fix_it.usecase.ordemservico.AdicionarItemPecaUseCase;
import com.fix_it.usecase.ordemservico.input.AdicionarItemPecaInput;
import com.fix_it.usecase.port.*;
import java.util.UUID;

public class AdicionarItemPecaUseCaseImpl implements AdicionarItemPecaUseCase {

    private final OrdemServicoRepository osRepository;
    private final ItemEstoqueRepository itemEstoqueRepository;
    private final ItemPecaOSRepository itemPecaRepository;
    private final MovimentoEstoqueRepository movimentoEstoqueRepository;
    private final ItemServicoOSRepository itemServicoRepository;

    public AdicionarItemPecaUseCaseImpl(OrdemServicoRepository osRepository, ItemEstoqueRepository itemEstoqueRepository, ItemPecaOSRepository itemPecaRepository, MovimentoEstoqueRepository movimentoEstoqueRepository, ItemServicoOSRepository itemServicoRepository) {
        this.osRepository = osRepository;
        this.itemEstoqueRepository = itemEstoqueRepository;
        this.itemPecaRepository = itemPecaRepository;
        this.movimentoEstoqueRepository = movimentoEstoqueRepository;
        this.itemServicoRepository = itemServicoRepository;
    }

    @Override
    public void executar(UUID osId, AdicionarItemPecaInput input) {
        OrdemServico os = osRepository.buscarPorId(osId)
            .orElseThrow(() -> new IllegalArgumentException("OS não encontrada"));

        validarStatusParaAlteracao(os);

        ItemEstoque estoque = itemEstoqueRepository.buscarPorId(input.itemEstoqueId())
            .orElseThrow(() -> new IllegalArgumentException("Item de estoque não encontrado"));

        int atualizados = itemEstoqueRepository.baixarEstoqueSeDisponivel(estoque.getId(), input.quantidade());
        if (atualizados == 0) {
            throw new IllegalStateException("Estoque insuficiente para " + estoque.getNmItemEstoque());
        }

        ItemPecaOS itemPeca = ItemPecaOS.novo(
            input.nome(),
            input.descricao(),
            input.quantidade(),
            input.valorUnitario(),
            os,
            estoque
        );
        ItemPecaOS salvo = itemPecaRepository.salvar(itemPeca);

        MovimentoEstoque movimento = MovimentoEstoque.novo(
            TipoMovimentoEstoque.SAIDA,
            input.quantidade(),
            "Baixa para OS " + os.getId(),
            estoque,
            salvo
        );
        movimentoEstoqueRepository.salvar(movimento);

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
