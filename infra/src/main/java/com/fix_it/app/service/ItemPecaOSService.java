package com.fix_it.app.service;

import com.fix_it.app.common.dto.ItemPecaDTO;
import com.fix_it.app.common.factory.OrdemServicoItemFactory;
import com.fix_it.app.model.ItemEstoque;
import com.fix_it.app.model.ItemPecaOS;
import com.fix_it.app.model.MovimentoEstoque;
import com.fix_it.app.model.OrdemServico;
import com.fix_it.app.model.enums.SituacaoOrdemServico;
import com.fix_it.app.model.enums.TipoMovimentoEstoque;
import com.fix_it.app.repository.ItemEstoqueRepository;
import com.fix_it.app.repository.ItemPecaOSRepository;
import com.fix_it.app.repository.MovimentoEstoqueRepository;
import com.fix_it.app.repository.OrdemServicoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class ItemPecaOSService {

    private final OrdemServicoRepository osRepository;
    private final ItemEstoqueRepository itemEstoqueRepository;
    private final ItemPecaOSRepository itemPecaRepository;
    private final MovimentoEstoqueRepository movimentoEstoqueRepository;
    private final OrdemServicoService ordemServicoService;

    /**
     * Adiciona UMA lista de itens de peça à OS:
     * - baixa estoque de forma atômica (fail se faltou)
     * - cria MovimentoEstoque de SAIDA para cada item
     */
    @Transactional
    public List<ItemPecaOS> adicionarLista(UUID osId, List<ItemPecaDTO> itens) {
        if (itens == null || itens.isEmpty()) return List.of();

        OrdemServico os = osRepository.findById(osId)
                .orElseThrow(() -> new EntityNotFoundException("OS não encontrada"));
        validarStatusParaAlteracao(os);

        Map<UUID, ItemEstoque> cacheEstoque = new HashMap<>();
        for (ItemPecaDTO dto : itens) {
            Objects.requireNonNull(dto.itemEstoqueId(), "item_estoque_id obrigatório");
            int qtd = Optional.ofNullable(dto.quantidade()).orElse(0);
            if (qtd <= 0) throw new IllegalArgumentException("Quantidade deve ser > 0");

            ItemEstoque e = cacheEstoque.computeIfAbsent(dto.itemEstoqueId(), id ->
                    itemEstoqueRepository.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException("Item de estoque não encontrado: " + id))
            );

            int updated = itemEstoqueRepository.baixarEstoqueSeDisponivel(e.getId(), qtd);
            if (updated == 0) {
                throw new IllegalStateException(
                        "Estoque insuficiente para '" + e.getNmItemEstoque() +
                                "'. Solicitado: " + qtd
                );
            }
        }

        List<ItemPecaOS> itensOS = new ArrayList<>(itens.size());
        List<MovimentoEstoque> movimentos = new ArrayList<>(itens.size());

        for (ItemPecaDTO dto : itens) {
            ItemEstoque e = cacheEstoque.get(dto.itemEstoqueId());

            ItemPecaOS novo = OrdemServicoItemFactory.criarItemPecaOS(os, e, dto);
            itensOS.add(novo);

            MovimentoEstoque mov = new MovimentoEstoque();
            mov.setItemEstoque(e);
            mov.setPeca(novo);
            mov.setTipo(TipoMovimentoEstoque.SAIDA);
            mov.setQuantidade(dto.quantidade());
            mov.setDescricao("Baixa para OS " + os.getId());
            mov.setDthMovimento(LocalDateTime.now());
            movimentos.add(mov);
        }

        itemPecaRepository.saveAll(itensOS);
        movimentoEstoqueRepository.saveAll(movimentos);

        ordemServicoService.recalcular(osId);
        return itensOS;
    }

    @Transactional
    public ItemPecaOS adicionar(UUID osId, ItemPecaDTO dto) {
        return adicionarLista(osId, List.of(dto)).getFirst();
    }

    /**
     * Remove um item da OS e estorna o estoque correspondente.
     */
    @Transactional
    public void remover(UUID osId, UUID itemId) {
        OrdemServico os = osRepository.findById(osId)
                .orElseThrow(() -> new EntityNotFoundException("OS não encontrada"));

        validarStatusParaAlteracao(os);

        ItemPecaOS item = itemPecaRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item da OS não encontrado"));

        // estorna estoque
        int qtd = Optional.ofNullable(item.getQuantidade()).orElse(0);
        if (qtd > 0) {
            itemEstoqueRepository.estornarEstoque(item.getItemEstoque().getId(), qtd);

            MovimentoEstoque mov = new MovimentoEstoque();
            mov.setItemEstoque(item.getItemEstoque());
            mov.setPeca(item);
            mov.setTipo(TipoMovimentoEstoque.ENTRADA);
            mov.setQuantidade(qtd);
            mov.setDescricao("Estorno por remoção do item da OS " + os.getId());
            mov.setDthMovimento(LocalDateTime.now());
            movimentoEstoqueRepository.save(mov);
        }

        itemPecaRepository.deleteById(itemId);
        ordemServicoService.recalcular(osId);
    }

    private void validarStatusParaAlteracao(OrdemServico os) {
        if (!(os.getSituacao().equals(SituacaoOrdemServico.RECEBIDA)
                || os.getSituacao().equals(SituacaoOrdemServico.EM_DIAGNOSTICO))) {
            throw new IllegalArgumentException("Não é possível alterar itens no status " + os.getSituacao());
        }
    }
}
