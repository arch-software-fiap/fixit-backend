package com.fix_it.app.service;

import com.fix_it.app.model.OrdemServico;
import com.fix_it.app.repository.ItemPecaOSRepository;
import com.fix_it.app.repository.ItemServicoOSRepository;
import com.fix_it.app.repository.OrdemServicoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrcamentoService - Recalcular orçamento")
class OrcamentoServiceTestCase {

    @Mock
    OrdemServicoRepository osRepository;
    @Mock
    ItemServicoOSRepository itemServicoRepository;
    @Mock
    ItemPecaOSRepository itemPecaRepository;

    @InjectMocks
    OrcamentoService service;

    private OrdemServico os(UUID id) {
        OrdemServico o = new OrdemServico();
        o.setId(id);
        o.setValorOrcamentoTotal(999L); // valor antigo, para garantir overwrite
        return o;
    }

    @Test
    @DisplayName("recalcular_ok → soma serviços + peças e persiste na OS")
    void recalcular_ok() {
        UUID osId = UUID.randomUUID();
        OrdemServico existente = os(osId);

        when(osRepository.findById(osId)).thenReturn(Optional.of(existente));
        when(itemServicoRepository.sumValorTotalByOsId(osId)).thenReturn(1500L);
        when(itemPecaRepository.sumValorTotalByOsId(osId)).thenReturn(3500L);
        when(osRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        OrdemServico resp = service.recalcular(osId);

        assertThat(resp.getValorOrcamentoTotal()).isEqualTo(5000L);

        ArgumentCaptor<OrdemServico> captor = ArgumentCaptor.forClass(OrdemServico.class);
        verify(osRepository).save(captor.capture());
        assertThat(captor.getValue().getValorOrcamentoTotal()).isEqualTo(5000L);

        verify(itemServicoRepository).sumValorTotalByOsId(osId);
        verify(itemPecaRepository).sumValorTotalByOsId(osId);
    }

    @Test
    @DisplayName("recalcular_semItens → funciona com zero (serviços=0, peças=0)")
    void recalcular_semItens() {
        UUID osId = UUID.randomUUID();
        OrdemServico existente = os(osId);

        when(osRepository.findById(osId)).thenReturn(Optional.of(existente));
        when(itemServicoRepository.sumValorTotalByOsId(osId)).thenReturn(0L);
        when(itemPecaRepository.sumValorTotalByOsId(osId)).thenReturn(0L);
        when(osRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        OrdemServico resp = service.recalcular(osId);

        assertThat(resp.getValorOrcamentoTotal()).isZero();
        verify(osRepository).save(any(OrdemServico.class));
    }

    @Test
    @DisplayName("recalcular_notFound → lança EntityNotFoundException quando OS não existe")
    void recalcular_notFound() {
        UUID osId = UUID.randomUUID();
        when(osRepository.findById(osId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.recalcular(osId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("OS não encontrada");
        verify(osRepository, never()).save(any());
        verify(itemServicoRepository, never()).sumValorTotalByOsId(any());
        verify(itemPecaRepository, never()).sumValorTotalByOsId(any());
    }
}
