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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ItemServicoOSService - Regras de adicionar/remover itens de serviço na OS")
class ItemServicoOSServiceTestCase {

    @Mock
    OrdemServicoRepository osRepository;
    @Mock
    ServicoRepository servicoRepository;
    @Mock
    ItemServicoOSRepository itemServicoRepository;

    @Mock
    OrdemServicoService ordemServicoService;

    @InjectMocks
    ItemServicoOSService service;

    private OrdemServico os(UUID id, SituacaoOrdemServico st) {
        OrdemServico o = new OrdemServico();
        o.setId(id);
        o.setSituacao(st);
        return o;
    }

    private Servico servico(UUID id) {
        Servico s = new Servico();
        s.setId(id);
        return s;
    }


    @Test
    @DisplayName("adicionar_ok → deve criar item via factory, salvar e recalcular orçamento")
    void adicionar_ok() {
        UUID osId = UUID.randomUUID();
        UUID servicoId = UUID.randomUUID();

        OrdemServico existente = os(osId, SituacaoOrdemServico.RECEBIDA);
        Servico serv = servico(servicoId);

        ItemServicoDTO dto = mock(ItemServicoDTO.class);
        when(dto.seq_servico()).thenReturn(servicoId);

        when(osRepository.findById(osId)).thenReturn(Optional.of(existente));
        when(servicoRepository.findById(servicoId)).thenReturn(Optional.of(serv));
        when(itemServicoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        ItemServicoOS fakeCriado = new ItemServicoOS();
        fakeCriado.setId(UUID.randomUUID());
        fakeCriado.setOrdemServico(existente);
        fakeCriado.setServico(serv);

        try (MockedStatic<OrdemServicoItemFactory> mocked = mockStatic(OrdemServicoItemFactory.class)) {
            mocked.when(() -> OrdemServicoItemFactory.criarItemServicoOS(existente, serv, dto))
                    .thenReturn(fakeCriado);

            ItemServicoOS resp = service.adicionar(osId, dto);

            assertThat(resp).isNotNull();
            assertThat(resp.getOrdemServico()).isSameAs(existente);
            assertThat(resp.getServico()).isSameAs(serv);

            verify(itemServicoRepository).save(fakeCriado);
            verify(ordemServicoService).recalcular(osId);
            mocked.verify(() -> OrdemServicoItemFactory.criarItemServicoOS(existente, serv, dto));
        }
    }

    @Test
    @DisplayName("adicionar_osNotFound → lança EntityNotFoundException quando OS não existe")
    void adicionar_osNotFound() {
        UUID osId = UUID.randomUUID();
        ItemServicoDTO dto = mock(ItemServicoDTO.class);

        when(osRepository.findById(osId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.adicionar(osId, dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("OS não encontrada");

        verifyNoInteractions(servicoRepository, itemServicoRepository, ordemServicoService);
    }

    @Test
    @DisplayName("adicionar_servicoNotFound → lança EntityNotFoundException quando Serviço não existe")
    void adicionar_servicoNotFound() {
        UUID osId = UUID.randomUUID();
        UUID servicoId = UUID.randomUUID();
        OrdemServico existente = os(osId, SituacaoOrdemServico.RECEBIDA);

        ItemServicoDTO dto = mock(ItemServicoDTO.class);
        when(dto.seq_servico()).thenReturn(servicoId);

        when(osRepository.findById(osId)).thenReturn(Optional.of(existente));
        when(servicoRepository.findById(servicoId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.adicionar(osId, dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Serviço não encontrado");

        verify(itemServicoRepository, never()).save(any());
        verify(ordemServicoService, never()).recalcular(any());
    }

    @Test
    @DisplayName("adicionar_statusInvalido → lança IllegalArgumentException para status que não permite alteração")
    void adicionar_statusInvalido() {
        UUID osId = UUID.randomUUID();
        OrdemServico existente = os(osId, SituacaoOrdemServico.EM_EXECUCAO);
        ItemServicoDTO dto = mock(ItemServicoDTO.class);

        when(osRepository.findById(osId)).thenReturn(Optional.of(existente));

        assertThatThrownBy(() -> service.adicionar(osId, dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Não é possível alterar itens");

        verifyNoInteractions(servicoRepository);
        verify(itemServicoRepository, never()).save(any());
        verify(ordemServicoService, never()).recalcular(any());
    }

    @Test
    @DisplayName("remover_ok → deve deletar item e recalcular orçamento quando status permite")
    void remover_ok() {
        UUID osId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        OrdemServico existente = os(osId, SituacaoOrdemServico.EM_DIAGNOSTICO);

        when(osRepository.findById(osId)).thenReturn(Optional.of(existente));

        service.remover(osId, itemId);

        verify(itemServicoRepository).deleteById(itemId);
        verify(ordemServicoService).recalcular(osId);
    }

    @Test
    @DisplayName("remover_osNotFound → lança EntityNotFoundException quando OS não existe")
    void remover_osNotFound() {
        UUID osId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        when(osRepository.findById(osId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.remover(osId, itemId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("OS não encontrada");

        verify(itemServicoRepository, never()).deleteById(any());
        verify(ordemServicoService, never()).recalcular(any());
    }

    @Test
    @DisplayName("remover_statusInvalido → lança IllegalArgumentException e não deleta")
    void remover_statusInvalido() {
        UUID osId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        OrdemServico existente = os(osId, SituacaoOrdemServico.AGUARDANDO_APROVACAO);

        when(osRepository.findById(osId)).thenReturn(Optional.of(existente));

        assertThatThrownBy(() -> service.remover(osId, itemId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Não é possível alterar itens");

        verify(itemServicoRepository, never()).deleteById(any());
        verify(ordemServicoService, never()).recalcular(any());
    }
}
