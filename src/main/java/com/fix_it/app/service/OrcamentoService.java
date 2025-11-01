package com.fix_it.app.service;

import com.fix_it.app.model.OrdemServico;
import com.fix_it.app.repository.ItemPecaOSRepository;
import com.fix_it.app.repository.ItemServicoOSRepository;
import com.fix_it.app.repository.OrdemServicoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
public class OrcamentoService {

    private final OrdemServicoRepository osRepository;
    private final ItemServicoOSRepository itemServicoRepository;
    private final ItemPecaOSRepository itemPecaRepository;

    @Transactional
    public OrdemServico recalcular(UUID osId) {
        OrdemServico os = osRepository.findById(osId)
                .orElseThrow(() -> new EntityNotFoundException("OS não encontrada: " + osId));

        long totalServicos = itemServicoRepository.sumValorTotalByOsId(osId);
        long totalPecas    = itemPecaRepository.sumValorTotalByOsId(osId);

        os.setValorOrcamentoTotal(totalServicos + totalPecas);
        return osRepository.save(os);
    }
}
