package com.fix_it.app.service;

import com.fix_it.app.common.dto.ItemPecaDTO;
import com.fix_it.app.common.factory.OrdemServicoItemFactory;
import com.fix_it.app.model.ItemEstoque;
import com.fix_it.app.model.ItemPecaOS;
import com.fix_it.app.model.OrdemServico;
import com.fix_it.app.model.enums.SituacaoOrdemServico;
import com.fix_it.app.repository.ItemEstoqueRepository;
import com.fix_it.app.repository.ItemPecaOSRepository;
import com.fix_it.app.repository.OrdemServicoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ItemPecaOSService {

    private final OrdemServicoRepository osRepository;
    private final ItemEstoqueRepository itemEstoqueRepository;
    private final ItemPecaOSRepository itemPecaRepository;
    private final OrcamentoService orcamentoService;

    @Transactional
    public ItemPecaOS adicionar(UUID osId, ItemPecaDTO dto) {
        OrdemServico os = osRepository.findById(osId)
                .orElseThrow(() -> new EntityNotFoundException("OS não encontrada"));

        validarStatusParaAlteracao(os);

        ItemEstoque estoque = itemEstoqueRepository.findById(dto.itemEstoqueId())
                .orElseThrow(() -> new EntityNotFoundException("Item de estoque não encontrado"));

        ItemPecaOS novoItem = OrdemServicoItemFactory.criarItemPecaOS(os, estoque, dto);
        itemPecaRepository.save(novoItem);

        orcamentoService.recalcular(osId);
        return novoItem;
    }

    @Transactional
    public void remover(UUID osId, UUID itemId) {
        OrdemServico os = osRepository.findById(osId)
                .orElseThrow(() -> new EntityNotFoundException("OS não encontrada"));

        validarStatusParaAlteracao(os);

        itemPecaRepository.deleteById(itemId);
        orcamentoService.recalcular(osId);
    }

    private void validarStatusParaAlteracao(OrdemServico os) {
        if (!(os.getSituacao().equals(SituacaoOrdemServico.RECEBIDA)
                || os.getSituacao().equals(SituacaoOrdemServico.EM_DIAGNOSTICO))) {
            throw new IllegalArgumentException("Não é possível alterar itens no status " + os.getSituacao());
        }
    }
}
