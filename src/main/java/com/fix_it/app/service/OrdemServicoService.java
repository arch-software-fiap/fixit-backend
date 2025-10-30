package com.fix_it.app.service;

import com.fix_it.app.common.dto.ItemPecaDTO;
import com.fix_it.app.common.dto.ItemServicoDTO;
import com.fix_it.app.common.dto.OrdemServicoDTO;
import com.fix_it.app.common.factory.OrdemServicoItemFactory;
import com.fix_it.app.model.*;
import com.fix_it.app.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.fix_it.app.model.enums.SituacaoOrdemServico.*;

@Service
@AllArgsConstructor
@Slf4j
//TODO: PENDENTE TESTES UNITÁRIOS
public class OrdemServicoService {

    private final OrdemServicoRepository osRepository;
    private final ServicoRepository servicoRepository;
    private final ItemEstoqueRepository itemEstoqueRepository;
    private final ItemServicoOSRepository itemServicoRepository;
    private final ItemPecaOSRepository itemPecaRepository;
    private final ClienteRepository clienteRepository;
    private final VeiculoRepository veiculoRepository;

    @Transactional
    public OrdemServico create(OrdemServicoDTO dto) {
        log.info("Iniciando criação de nova Ordem de Serviço...");

        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + dto.clienteId()));

        Veiculo veiculo = veiculoRepository.findById(dto.veiculoId())
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado com ID: " + dto.veiculoId()));

        if (!veiculo.getCliente().getId().equals(cliente.getId())) {
            throw new IllegalArgumentException("O veículo selecionado não pertence ao cliente informado.");
        }

        OrdemServico os = new OrdemServico();
        os.setCliente(cliente);
        os.setVeiculo(veiculo);
        os.setDescricao(dto.descricao());

        os.setSituacao(RECEBIDA);
        os.setDataAberturaEm(LocalDateTime.now());
        os.setValorOrcamentoTotal(0L);
        os.setValorTotalFinal(0L);

        return osRepository.save(os);
    }

    @Transactional
    public OrdemServico update(UUID osId, OrdemServicoDTO dto) {
        OrdemServico os = osRepository.findById(osId).orElseThrow(EntityNotFoundException::new);

        if (os.getSituacao().equals(FINALIZADA) || os.getSituacao().equals(ENTREGUE)) {
            throw new IllegalArgumentException("Não é possível atualizar uma OS que já foi finalizada ou entregue.");
        }

        os.setDescricao(dto.descricao());

        log.info("Ordem de Serviço ID ({}) atualizada.", osId);
        return osRepository.save(os);
    }

    @Transactional(readOnly = true)
    public OrdemServico findById(UUID id) {
        return osRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ordem de Serviço não encontrada com ID: " + id));
    }

    @Transactional
    public ItemServicoOS adicionarServico(UUID osId, ItemServicoDTO dto) {
        OrdemServico os = osRepository.findById(osId)
                .orElseThrow(() -> new EntityNotFoundException("Ordem de Serviço não encontrada"));

        Servico servico = servicoRepository.findById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado"));

        if (!os.getSituacao().equals(RECEBIDA) && !os.getSituacao().equals(EM_DIAGNOSTICO)) {
            throw new IllegalArgumentException("Não é possível adicionar itens a uma OS com status " + os.getSituacao());
        }

        ItemServicoOS novoItem = OrdemServicoItemFactory.criarItemServicoOS(os, servico, dto);
        itemServicoRepository.save(novoItem);

        recalcularOrcamentoTotal(os);
        osRepository.save(os);

        return novoItem;
    }

    @Transactional
    public ItemPecaOS adicionarPeca(UUID osId, ItemPecaDTO dto) {
        OrdemServico os = osRepository.findById(osId)
                .orElseThrow(() -> new EntityNotFoundException("Ordem de Serviço não encontrada"));

        ItemEstoque itemEstoque = itemEstoqueRepository.findById(dto.itemEstoqueId())
                .orElseThrow(() -> new EntityNotFoundException("Item de estoque não encontrado"));

        if (!os.getSituacao().equals(RECEBIDA) && !os.getSituacao().equals(EM_DIAGNOSTICO)) {
            throw new IllegalArgumentException("Não é possível adicionar itens a uma OS com status " + os.getSituacao());
        }

        ItemPecaOS novoItem = OrdemServicoItemFactory.criarItemPecaOS(os, itemEstoque, dto);
        itemPecaRepository.save(novoItem);

        recalcularOrcamentoTotal(os);
        osRepository.save(os);

        return novoItem;
    }

    @Transactional
    public OrdemServico recalcularOrcamentoTotal(OrdemServico os) {
        os.getItensServico().size();
        os.getItensPeca().size();

        long totalServicos = os.getItensServico().stream()
                .mapToLong(ItemServicoOS::getValorTotal)
                .sum();

        long totalPecas = os.getItensPeca().stream()
                .mapToLong(ItemPecaOS::getValorTotal)
                .sum();

        os.setValorOrcamentoTotal(totalServicos + totalPecas);
        return osRepository.save(os);
    }

    @Transactional
    public OrdemServico enviarOrcamentoParaAprovacao(UUID osId) {
        OrdemServico os = osRepository.findById(osId)
                .orElseThrow(() -> new EntityNotFoundException("Ordem de Serviço não encontrada"));

        if (!os.getSituacao().equals(RECEBIDA) && !os.getSituacao().equals(EM_DIAGNOSTICO)) {
            throw new IllegalArgumentException("Não é possível enviar orçamento para aprovação de uma OS com status " + os.getSituacao());
        }

        // Recalcula o orçamento antes de enviar (garantir que está atualizado)
        recalcularOrcamentoTotal(os);

        os.setSituacao(AGUARDANDO_APROVACAO);
        OrdemServico osAtualizada = osRepository.save(os);

        // TODO: Implementar lógica de envio de notificação para o cliente
        log.info("Orçamento da OS ID ({}) foi enviado para aprovação do cliente {}.", osId, os.getCliente().getNmCliente());

        return osAtualizada;
    }

}
