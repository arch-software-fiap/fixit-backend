package com.fix_it.app.service;

import com.fix_it.app.common.dto.ItemServicoDTO;
import com.fix_it.app.common.factory.OrdemServicoItemFactory;
import com.fix_it.app.model.ItemServicoOS;
import com.fix_it.app.model.OrdemServico;
import com.fix_it.app.model.Servico;
import com.fix_it.app.model.enums.SituacaoOrdemServico;
import com.fix_it.app.repository.ItemServicoOSRepository;
import com.fix_it.app.repository.OrdemServicoRepository;
import com.fix_it.app.repository.ServicoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ItemServicoOSService {

    private final OrdemServicoRepository osRepository;
    private final ServicoRepository servicoRepository;
    private final ItemServicoOSRepository itemServicoRepository;
    private final OrdemServicoService ordemServicoService;

    @Transactional
    public ItemServicoOS adicionar(UUID osId, ItemServicoDTO dto) {
        OrdemServico os = osRepository.findById(osId)
                .orElseThrow(() -> new EntityNotFoundException("OS não encontrada"));

        validarStatusParaAlteracao(os);

        Servico servico = servicoRepository.findById(dto.seq_servico())
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado"));

        var idServicos = itemServicoRepository.findAllByOrdemServico_Id(osId)
                .stream().map(itemServicoOS ->{ return itemServicoOS.getServico().getId();}).toList();

        ItemServicoOS Item;
        if(idServicos.contains(dto.seq_servico())){
            var i =  itemServicoRepository.findByServico_Id(dto.seq_servico()).orElseThrow();
            Item = OrdemServicoItemFactory.atualizaItemServicoOS(i, servico, dto);
        }else {
            Item = OrdemServicoItemFactory.criarItemServicoOS(os, servico, dto);
        }

        itemServicoRepository.save(Item);
        ordemServicoService.recalcular(osId);
        return Item;
    }

    @Transactional
    public void remover(UUID osId, UUID itemId) {
        OrdemServico os = osRepository.findById(osId)
                .orElseThrow(() -> new EntityNotFoundException("OS não encontrada"));

        validarStatusParaAlteracao(os);

        itemServicoRepository.deleteById(itemId);
        ordemServicoService.recalcular(osId);
    }

    private void validarStatusParaAlteracao(OrdemServico os) {
        if (!(os.getSituacao().equals(SituacaoOrdemServico.RECEBIDA)
                || os.getSituacao().equals(SituacaoOrdemServico.EM_DIAGNOSTICO))) {
            throw new IllegalArgumentException("Não é possível alterar itens no status " + os.getSituacao());
        }
    }
}
