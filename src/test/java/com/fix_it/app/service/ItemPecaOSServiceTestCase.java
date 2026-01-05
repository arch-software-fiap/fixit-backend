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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.fix_it.app.model.enums.SituacaoOrdemServico.RECEBIDA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ItemPecaOSService - adicionar/remover peças na OS, baixa/estorno de estoque")
class ItemPecaOSServiceTestCase {

    @Mock
    OrdemServicoRepository osRepository;
    @Mock
    ItemEstoqueRepository itemEstoqueRepository;
    @Mock
    ItemPecaOSRepository itemPecaRepository;
    @Mock
    MovimentoEstoqueRepository movimentoEstoqueRepository;
    @Mock
    OrdemServicoService ordemServicoService;

    @InjectMocks
    ItemPecaOSService service;

    private OrdemServico os(UUID id, SituacaoOrdemServico st) {
        OrdemServico o = new OrdemServico();
        o.setId(id);
        o.setSituacao(st);
        return o;
    }

    private ItemEstoque estoque(UUID id, String nome) {
        ItemEstoque e = new ItemEstoque();
        e.setId(id);
        e.setNmItemEstoque(nome);
        return e;
    }

    private ItemPecaDTO dto(UUID estoqueId, Integer qtd) {
        ItemPecaDTO d = mock(ItemPecaDTO.class);
        when(d.itemEstoqueId()).thenReturn(estoqueId);
        when(d.quantidade()).thenReturn(qtd);
        return d;
    }

    private ItemPecaOS itemPeca(OrdemServico os, ItemEstoque e, Integer qtd) {
        ItemPecaOS p = new ItemPecaOS();
        p.setId(UUID.randomUUID());
        p.setOrdemServico(os);
        p.setItemEstoque(e);
        p.setQuantidade(qtd);
        return p;
    }


    @Test
    @DisplayName("adicionarLista_vazio → retorna lista vazia e não toca repos")
    void adicionarLista_vazio() {
        UUID osId = UUID.randomUUID();
        var resp = service.adicionarLista(osId, List.of());
        assertThat(resp).isEmpty();
        verifyNoInteractions(osRepository, itemEstoqueRepository, itemPecaRepository, movimentoEstoqueRepository, ordemServicoService);
    }

    @Test
    @DisplayName("adicionarLista_null → retorna lista vazia e não toca repos")
    void adicionarLista_null() {
        UUID osId = UUID.randomUUID();
        var resp = service.adicionarLista(osId, null);
        assertThat(resp).isEmpty();
        verifyNoInteractions(osRepository, itemEstoqueRepository, itemPecaRepository, movimentoEstoqueRepository, ordemServicoService);
    }

    @Test
    @DisplayName("adicionarLista_ok → baixa estoque, cria itens e movimentos, salva em lote e recalcula orçamento")
    void adicionarLista_ok() {
        UUID osId = UUID.randomUUID();
        UUID estA = UUID.randomUUID();
        UUID estB = UUID.randomUUID();

        OrdemServico existente = os(osId, RECEBIDA);
        ItemEstoque ea = estoque(estA, "Filtro de Óleo");
        ItemEstoque eb = estoque(estB, "Correia Dentada");

        ItemPecaDTO dtoA = dto(estA, 2);
        ItemPecaDTO dtoB = dto(estB, 1);

        when(osRepository.findById(osId)).thenReturn(Optional.of(existente));
        when(itemEstoqueRepository.findById(estA)).thenReturn(Optional.of(ea));
        when(itemEstoqueRepository.findById(estB)).thenReturn(Optional.of(eb));
        when(itemEstoqueRepository.baixarEstoqueSeDisponivel(estA, 2)).thenReturn(1);
        when(itemEstoqueRepository.baixarEstoqueSeDisponivel(estB, 1)).thenReturn(1);

        // saveAll devolve a própria lista
        when(itemPecaRepository.saveAll(anyList())).thenAnswer(i -> i.getArgument(0));
        when(movimentoEstoqueRepository.saveAll(anyList())).thenAnswer(i -> i.getArgument(0));

        ItemPecaOS pecaA = itemPeca(existente, ea, 2);
        ItemPecaOS pecaB = itemPeca(existente, eb, 1);

        try (MockedStatic<OrdemServicoItemFactory> mocked = mockStatic(OrdemServicoItemFactory.class)) {
            mocked.when(() -> OrdemServicoItemFactory.criarItemPecaOS(existente, ea, dtoA)).thenReturn(pecaA);
            mocked.when(() -> OrdemServicoItemFactory.criarItemPecaOS(existente, eb, dtoB)).thenReturn(pecaB);

            List<ItemPecaOS> resp = service.adicionarLista(osId, List.of(dtoA, dtoB));

            assertThat(resp).hasSize(2);
            assertThat(resp).extracting(ItemPecaOS::getItemEstoque).containsExactlyInAnyOrder(ea, eb);

            ArgumentCaptor<List<ItemPecaOS>> capItens = ArgumentCaptor.forClass(List.class);
            verify(itemPecaRepository).saveAll(capItens.capture());
            assertThat(capItens.getValue()).containsExactlyInAnyOrder(pecaA, pecaB);

            ArgumentCaptor<List<MovimentoEstoque>> capMovs = ArgumentCaptor.forClass(List.class);
            verify(movimentoEstoqueRepository).saveAll(capMovs.capture());
            assertThat(capMovs.getValue()).hasSize(2)
                    .allSatisfy(m -> {
                        assertThat(m.getTipo()).isEqualTo(TipoMovimentoEstoque.SAIDA);
                        assertThat(m.getQuantidade()).isPositive();
                        assertThat(m.getItemEstoque()).isIn(ea, eb);
                        assertThat(m.getPeca()).isIn(pecaA, pecaB);
                    });

            verify(ordemServicoService).recalcular(osId);
            mocked.verify(() -> OrdemServicoItemFactory.criarItemPecaOS(existente, ea, dtoA));
            mocked.verify(() -> OrdemServicoItemFactory.criarItemPecaOS(existente, eb, dtoB));
        }
    }

    @Test
    @DisplayName("adicionarLista_osNotFound → lança EntityNotFoundException")
    void adicionarLista_osNotFound() {
        UUID osId = UUID.randomUUID();
        ItemPecaDTO d = mock(ItemPecaDTO.class);
        when(osRepository.findById(osId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.adicionarLista(osId, List.of(d)))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("OS não encontrada");
    }


    @Test
    @DisplayName("adicionarLista_statusInvalido → lança IllegalArgumentException quando OS não permite alteração")
    void adicionarLista_statusInvalido() {
        UUID osId = UUID.randomUUID();
        OrdemServico existente = os(osId, SituacaoOrdemServico.AGUARDANDO_APROVACAO);
        when(osRepository.findById(osId)).thenReturn(Optional.of(existente));

        ItemPecaDTO d = mock(ItemPecaDTO.class);

        assertThatThrownBy(() -> service.adicionarLista(osId, List.of(d)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Não é possível alterar itens");

        verifyNoInteractions(itemEstoqueRepository, itemPecaRepository, movimentoEstoqueRepository, ordemServicoService);
    }


    @Test
    @DisplayName("adicionarLista_quantidadeInvalida → deve falhar quando quantidade <= 0")
    void adicionarLista_quantidadeInvalida() {
        UUID osId = UUID.randomUUID();
        OrdemServico existente = os(osId, RECEBIDA);
        when(osRepository.findById(osId)).thenReturn(Optional.of(existente));

        ItemPecaDTO d0 = dto(UUID.randomUUID(), 0);
        ItemPecaDTO dn = dto(UUID.randomUUID(), -1);

        assertThatThrownBy(() -> service.adicionarLista(osId, List.of(d0)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Quantidade deve ser > 0");

        assertThatThrownBy(() -> service.adicionarLista(osId, List.of(dn)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Quantidade deve ser > 0");
    }

    @Test
    @DisplayName("adicionarLista_itemEstoqueObrigatorio → deve falhar quando itemEstoqueId é null")
    void adicionarLista_itemEstoqueObrigatorio() {
        UUID osId = UUID.randomUUID();
        OrdemServico existente = os(osId, RECEBIDA);
        when(osRepository.findById(osId)).thenReturn(Optional.of(existente));

        ItemPecaDTO d = mock(ItemPecaDTO.class);
        when(d.itemEstoqueId()).thenReturn(null);

        assertThatThrownBy(() -> service.adicionarLista(osId, List.of(d)))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("item_estoque_id obrigatório");
    }


    @Test
    @DisplayName("adicionarLista_itemEstoqueNotFound → lança EntityNotFoundException se item não existe")
    void adicionarLista_itemEstoqueNotFound() {
        UUID osId = UUID.randomUUID();
        UUID estId = UUID.randomUUID();
        OrdemServico existente = os(osId, RECEBIDA);
        when(osRepository.findById(osId)).thenReturn(Optional.of(existente));
        when(itemEstoqueRepository.findById(estId)).thenReturn(Optional.empty());

        ItemPecaDTO d = dto(estId, 1);

        assertThatThrownBy(() -> service.adicionarLista(osId, List.of(d)))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Item de estoque não encontrado");
    }

    @Test
    @DisplayName("adicionarLista_estoqueInsuficiente → deve falhar quando baixa retorna 0")
    void adicionarLista_estoqueInsuficiente() {
        UUID osId = UUID.randomUUID();
        UUID estId = UUID.randomUUID();

        OrdemServico existente = os(osId, RECEBIDA);
        ItemEstoque est = estoque(estId, "Pastilha de Freio");

        when(osRepository.findById(osId)).thenReturn(Optional.of(existente));
        when(itemEstoqueRepository.findById(estId)).thenReturn(Optional.of(est));
        when(itemEstoqueRepository.baixarEstoqueSeDisponivel(estId, 3)).thenReturn(0);

        ItemPecaDTO d = dto(estId, 3);

        assertThatThrownBy(() -> service.adicionarLista(osId, List.of(d)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Estoque insuficiente");

        verify(itemPecaRepository, never()).saveAll(anyList());
        verify(movimentoEstoqueRepository, never()).saveAll(anyList());
        verify(ordemServicoService, never()).recalcular(any());
    }

    @Test
    @DisplayName("adicionar_ok → delega para adicionarLista e retorna o primeiro item")
    void adicionar_ok() {
        UUID osId = UUID.randomUUID();
        UUID estId = UUID.randomUUID();

        OrdemServico existente = os(osId, RECEBIDA);
        ItemEstoque est = estoque(estId, "Óleo 5W30");

        ItemPecaDTO d = dto(estId, 1);

        when(osRepository.findById(osId)).thenReturn(Optional.of(existente));
        when(itemEstoqueRepository.findById(estId)).thenReturn(Optional.of(est));
        when(itemEstoqueRepository.baixarEstoqueSeDisponivel(estId, 1)).thenReturn(1);

        ItemPecaOS criado = itemPeca(existente, est, 1);
        when(itemPecaRepository.saveAll(anyList())).thenAnswer(i -> i.getArgument(0));
        when(movimentoEstoqueRepository.saveAll(anyList())).thenAnswer(i -> i.getArgument(0));

        try (MockedStatic<OrdemServicoItemFactory> mocked = mockStatic(OrdemServicoItemFactory.class)) {
            mocked.when(() -> OrdemServicoItemFactory.criarItemPecaOS(existente, est, d)).thenReturn(criado);

            ItemPecaOS resp = service.adicionar(osId, d);

            assertThat(resp).isNotNull();
            assertThat(resp.getItemEstoque()).isEqualTo(est);
            verify(ordemServicoService).recalcular(osId);
        }
    }

    @Test
    @DisplayName("remover_ok_comEstorno → estorna estoque, cria movimento de ENTRADA, deleta e recalcula")
    void remover_ok_comEstorno() {
        UUID osId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        OrdemServico existente = os(osId, SituacaoOrdemServico.EM_DIAGNOSTICO);
        ItemEstoque est = estoque(UUID.randomUUID(), "Filtro de Ar");
        ItemPecaOS item = new ItemPecaOS();
        item.setId(itemId);
        item.setItemEstoque(est);
        item.setQuantidade(4);

        when(osRepository.findById(osId)).thenReturn(Optional.of(existente));
        when(itemPecaRepository.findById(itemId)).thenReturn(Optional.of(item));

        service.remover(osId, itemId);

        verify(itemEstoqueRepository).estornarEstoque(est.getId(), 4);
        // movimento individual salvo
        ArgumentCaptor<MovimentoEstoque> capMov = ArgumentCaptor.forClass(MovimentoEstoque.class);
        verify(movimentoEstoqueRepository).save(capMov.capture());
        assertThat(capMov.getValue().getTipo()).isEqualTo(TipoMovimentoEstoque.ENTRADA);
        assertThat(capMov.getValue().getQuantidade()).isEqualTo(4);
        assertThat(capMov.getValue().getItemEstoque()).isEqualTo(est);
        assertThat(capMov.getValue().getPeca()).isEqualTo(item);

        verify(itemPecaRepository).deleteById(itemId);
        verify(ordemServicoService).recalcular(osId);
    }

    @Test
    @DisplayName("remover_ok_semEstorno → se quantidade null/0 não estorna nem cria movimento")
    void remover_ok_semEstorno() {
        UUID osId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        OrdemServico existente = os(osId, RECEBIDA);
        ItemEstoque est = estoque(UUID.randomUUID(), "Junta Homocinética");
        ItemPecaOS item = new ItemPecaOS();
        item.setId(itemId);
        item.setItemEstoque(est);
        item.setQuantidade(0); // sem estorno

        when(osRepository.findById(osId)).thenReturn(Optional.of(existente));
        when(itemPecaRepository.findById(itemId)).thenReturn(Optional.of(item));

        service.remover(osId, itemId);

        verify(itemEstoqueRepository, never()).estornarEstoque(any(), anyInt());
        verify(movimentoEstoqueRepository, never()).save(any(MovimentoEstoque.class));
        verify(itemPecaRepository).deleteById(itemId);
        verify(ordemServicoService).recalcular(osId);
    }

    @Test
    @DisplayName("remover_osNotFound → lança EntityNotFoundException")
    void remover_osNotFound() {
        UUID osId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        when(osRepository.findById(osId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.remover(osId, itemId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("OS não encontrada");

        verifyNoInteractions(itemPecaRepository, itemEstoqueRepository, movimentoEstoqueRepository, ordemServicoService);
    }

    @Test
    @DisplayName("remover_itemNotFound → lança EntityNotFoundException para item inexistente")
    void remover_itemNotFound() {
        UUID osId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        OrdemServico existente = os(osId, RECEBIDA);

        when(osRepository.findById(osId)).thenReturn(Optional.of(existente));
        when(itemPecaRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.remover(osId, itemId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Item da OS não encontrado");
    }

    @Test
    @DisplayName("remover_statusInvalido → lança IllegalArgumentException e não estorna/recacula")
    void remover_statusInvalido() {
        UUID osId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        OrdemServico existente = os(osId, SituacaoOrdemServico.AGUARDANDO_APROVACAO);

        when(osRepository.findById(osId)).thenReturn(Optional.of(existente));

        assertThatThrownBy(() -> service.remover(osId, itemId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Não é possível alterar itens");

        verify(itemPecaRepository, never()).findById(any());
        verify(itemEstoqueRepository, never()).estornarEstoque(any(), anyInt());
        verify(movimentoEstoqueRepository, never()).save(any());
        verify(ordemServicoService, never()).recalcular(any());
    }
}
