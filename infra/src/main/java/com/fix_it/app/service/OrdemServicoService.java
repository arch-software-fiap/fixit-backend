package com.fix_it.app.service;

import com.fix_it.app.common.dto.OrdemServicoDTO;
import com.fix_it.app.model.view.OrdemServicoView;
import com.fix_it.app.model.Cliente;
import com.fix_it.app.model.OrdemServico;
import com.fix_it.app.model.Veiculo;
import com.fix_it.app.model.enums.SituacaoOrdemServico;
import com.fix_it.app.repository.*;
import com.fix_it.app.repository.view.OrdemServicoViewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class OrdemServicoService {

    private final OrdemServicoRepository osRepository;
    private final ClienteRepository clienteRepository;
    private final VeiculoRepository veiculoRepository;
    private final ItemServicoOSRepository itemServicoRepository;
    private final ItemPecaOSRepository itemPecaRepository;
    private final OrdemServicoViewRepository ordemServicoViewRepository;

    @Transactional(readOnly = true)
    public Page<OrdemServico> findAll(int page, int size) {
        return osRepository.findAll(PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public OrdemServico findById(UUID id) {
        return osRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public OrdemServico create(OrdemServicoDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado: " + dto.clienteId()));

        Veiculo veiculo = veiculoRepository.findById(dto.veiculoId())
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado: " + dto.veiculoId()));

        if (!veiculo.getCliente().getId().equals(cliente.getId())) {
            throw new IllegalArgumentException("Veículo não pertence ao cliente informado.");
        }

        OrdemServico os = new OrdemServico();
        os.setCliente(cliente);
        os.setVeiculo(veiculo);
        os.setDescricao(dto.descricao());
        os.setSituacao(SituacaoOrdemServico.RECEBIDA);
        os.setValorOrcamentoTotal(0L);
        os.setValorTotalFinal(0L);

        os = osRepository.save(os);
        return os;
    }

    @Transactional
    public OrdemServico update(UUID osId, OrdemServicoDTO dto) {
        OrdemServico os = osRepository.findById(osId).orElseThrow(EntityNotFoundException::new);

        if (os.getSituacao().equals(SituacaoOrdemServico.FINALIZADA)
                || os.getSituacao().equals(SituacaoOrdemServico.ENTREGUE)) {
            throw new IllegalArgumentException("OS finalizada/entregue não pode ser atualizada.");
        }

        os.setDescricao(dto.descricao());
        return osRepository.save(os);
    }

    @Transactional
    public OrdemServico enviarOrcamentoParaAprovacao(UUID osId) {
        OrdemServico os = osRepository.findById(osId)
                .orElseThrow(() -> new EntityNotFoundException("OS não encontrada"));

        if (!(os.getSituacao().equals(SituacaoOrdemServico.RECEBIDA)
                || os.getSituacao().equals(SituacaoOrdemServico.EM_DIAGNOSTICO))) {
            throw new IllegalArgumentException("Status inválido para envio de orçamento: " + os.getSituacao());
        }

        os = recalcular(osId);

        os.setSituacao(SituacaoOrdemServico.AGUARDANDO_APROVACAO);
        os = osRepository.save(os);

        log.info("Orçamento da OS ({}) enviado para aprovação do cliente {}.",
                osId, os.getCliente().getNome());
        return os;
    }


    @Transactional
    public OrdemServico recalcular(UUID osId) {
        OrdemServico os = osRepository.findById(osId)
                .orElseThrow(() -> new EntityNotFoundException("OS não encontrada: " + osId));

        long totalServicos = itemServicoRepository.sumValorTotalByOsId(osId);
        long totalPecas    = itemPecaRepository.sumValorTotalByOsId(osId);

        os.setValorOrcamentoTotal(totalServicos + totalPecas);
        return osRepository.save(os);
    }

    public Page<OrdemServico> findAllByClienteCpfCnpj(String cpfCnpj, int page, int size) {
        return osRepository.findAllByCliente_CpfCnpj(cpfCnpj, PageRequest.of(page, size));
    }

    public Optional<OrdemServicoView> findByClienteCpfCnpjDetalhado(String cpfCnpj, UUID id) {
        return ordemServicoViewRepository.findByCpfCnpjAndId(cpfCnpj, id);
    }
}

