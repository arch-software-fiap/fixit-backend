package com.fix_it.app.service;

import com.fix_it.app.common.dto.AcompanhamentoOSDTO;
import com.fix_it.app.repository.OrdemServicoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AcompanhamentoPublicService {

    private final OrdemServicoRepository osRepository;

    @Transactional(readOnly = true)
    public AcompanhamentoOSDTO consultar(UUID osId, String cpfCnpj) {
        var os = osRepository.findByIdAndClienteDoc(osId, cpfCnpj)
                .orElseThrow(() -> new EntityNotFoundException("OS não encontrada para o documento informado"));

        return new AcompanhamentoOSDTO(
                os.getId(),
                os.getCliente().getNome(),
                os.getCliente().getCpfCnpj(),
                os.getVeiculo().getPlaca(),
                os.getSituacao(),
                os.getValorOrcamentoTotal(),
                os.getValorTotalFinal(),
                os.getDataAberturaEm(),
                os.getDataFechamentoEm()
        );
    }
}
