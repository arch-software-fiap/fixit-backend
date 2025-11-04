package com.fix_it.app.service;

import com.fix_it.app.common.dto.OrdemServicoDTO;
import com.fix_it.app.model.Cliente;
import com.fix_it.app.model.OrdemServico;
import com.fix_it.app.model.Veiculo;
import com.fix_it.app.model.enums.SituacaoOrdemServico;
import com.fix_it.app.repository.ClienteRepository;
import com.fix_it.app.repository.OrdemServicoRepository;
import com.fix_it.app.repository.VeiculoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrdemServicoService - Testes de regras e persistência")
class OrdemServicoServiceTestCase {

    @Mock
    OrdemServicoRepository osRepository;
    @Mock
    ClienteRepository clienteRepository;
    @Mock
    VeiculoRepository veiculoRepository;
    @Mock
    OrcamentoService orcamentoService;

    @InjectMocks
    OrdemServicoService service;

    private OrdemServicoDTO dto(UUID clienteId, UUID veiculoId, String desc) {
        return new OrdemServicoDTO(clienteId, veiculoId, desc);
    }

    private Cliente cliente(UUID id, String nome) {
        Cliente c = new Cliente();
        c.setId(id);
        c.setNome(nome);
        return c;
    }

    private Veiculo veiculo(UUID id, Cliente dono) {
        Veiculo v = new Veiculo();
        v.setId(id);
        v.setCliente(dono);
        return v;
    }

    private OrdemServico os(UUID id, Cliente c, Veiculo v, SituacaoOrdemServico st) {
        OrdemServico os = new OrdemServico();
        os.setId(id);
        os.setCliente(c);
        os.setVeiculo(v);
        os.setSituacao(st);
        return os;
    }

    @Test
    @DisplayName("findAll_ok → deve paginar resultados")
    void findAll_ok() {
        when(osRepository.findAll(PageRequest.of(0, 2)))
                .thenReturn(new PageImpl<>(List.of(new OrdemServico(), new OrdemServico())));

        var page = service.findAll(0, 2);

        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getSize()).isEqualTo(2);
    }

    @Test
    @DisplayName("findById_notFound → deve lançar EntityNotFoundException")
    void findById_notFound() {
        UUID id = UUID.randomUUID();
        when(osRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("create_ok → deve criar OS com situação RECEBIDA e valores zerados")
    void create_ok() {
        UUID clienteId = UUID.randomUUID();
        UUID veiculoId = UUID.randomUUID();

        Cliente c = cliente(clienteId, "Fulano");
        Veiculo v = veiculo(veiculoId, c);

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(c));
        when(veiculoRepository.findById(veiculoId)).thenReturn(Optional.of(v));
        when(osRepository.save(any())).thenAnswer(i -> {
            OrdemServico saved = i.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        var resp = service.create(dto(clienteId, veiculoId, "Troca de óleo"));

        assertThat(resp.getId()).isNotNull();
        assertThat(resp.getSituacao()).isEqualTo(SituacaoOrdemServico.RECEBIDA);
        assertThat(resp.getValorOrcamentoTotal()).isZero();
        assertThat(resp.getValorTotalFinal()).isZero();
        verify(osRepository).save(any(OrdemServico.class));
    }

    @Test
    @DisplayName("create_clienteNaoEncontrado → deve lançar 404 se cliente não existe")
    void create_clienteNaoEncontrado() {
        UUID clienteId = UUID.randomUUID();
        UUID veiculoId = UUID.randomUUID();
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(dto(clienteId, veiculoId, "X")))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Cliente não encontrado");
    }

    @Test
    @DisplayName("create_veiculoNaoEncontrado → deve lançar 404 se veículo não existe")
    void create_veiculoNaoEncontrado() {
        UUID clienteId = UUID.randomUUID();
        UUID veiculoId = UUID.randomUUID();
        Cliente c = cliente(clienteId, "Fulano");

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(c));
        when(veiculoRepository.findById(veiculoId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(dto(clienteId, veiculoId, "X")))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Veículo não encontrado");
    }

    @Test
    @DisplayName("create_veiculoDeOutroCliente → deve falhar se veículo não pertence ao cliente")
    void create_veiculoDeOutroCliente() {
        UUID clienteId = UUID.randomUUID();
        UUID outroClienteId = UUID.randomUUID();
        UUID veiculoId = UUID.randomUUID();

        Cliente c = cliente(clienteId, "Fulano");
        Cliente outro = cliente(outroClienteId, "Beltrano");
        Veiculo v = veiculo(veiculoId, outro); // veículo de outro cliente

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(c));
        when(veiculoRepository.findById(veiculoId)).thenReturn(Optional.of(v));

        assertThatThrownBy(() -> service.create(dto(clienteId, veiculoId, "X")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Veículo não pertence ao cliente");
    }

    @Test
    @DisplayName("update_ok → deve atualizar descrição quando OS não finalizada/entregue")
    void update_ok() {
        UUID osId = UUID.randomUUID();
        Cliente c = cliente(UUID.randomUUID(), "Fulano");
        Veiculo v = veiculo(UUID.randomUUID(), c);
        OrdemServico existente = os(osId, c, v, SituacaoOrdemServico.EM_DIAGNOSTICO);

        when(osRepository.findById(osId)).thenReturn(Optional.of(existente));
        when(osRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        var resp = service.update(osId, dto(c.getId(), v.getId(), "Nova descrição"));

        assertThat(resp.getDescricao()).isEqualTo("Nova descrição");
        verify(osRepository).save(existente);
    }

    @Test
    @DisplayName("update_bloqueado → deve falhar se OS FINALIZADA")
    void update_bloqueado_finalizada() {
        UUID osId = UUID.randomUUID();
        OrdemServico existente = new OrdemServico();
        existente.setId(osId);
        existente.setSituacao(SituacaoOrdemServico.FINALIZADA);
        when(osRepository.findById(osId)).thenReturn(Optional.of(existente));

        assertThatThrownBy(() -> service.update(osId, dto(UUID.randomUUID(), UUID.randomUUID(), "desc")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("não pode ser atualizada");
    }

    @Test
    @DisplayName("update_bloqueado → deve falhar se OS ENTREGUE")
    void update_bloqueado_entregue() {
        UUID osId = UUID.randomUUID();
        OrdemServico existente = new OrdemServico();
        existente.setId(osId);
        existente.setSituacao(SituacaoOrdemServico.ENTREGUE);
        when(osRepository.findById(osId)).thenReturn(Optional.of(existente));

        assertThatThrownBy(() -> service.update(osId, dto(UUID.randomUUID(), UUID.randomUUID(), "desc")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("não pode ser atualizada");
    }

    @Test
    @DisplayName("enviarOrcamento_paraAprovacao_ok (RECEBIDA) → deve recalcular e mudar status para AGUARDANDO_APROVACAO")
    void enviarOrcamento_paraAprovacao_ok_recebida() {
        UUID osId = UUID.randomUUID();
        Cliente c = cliente(UUID.randomUUID(), "Fulano");
        Veiculo v = veiculo(UUID.randomUUID(), c);

        OrdemServico existente = os(osId, c, v, SituacaoOrdemServico.RECEBIDA);

        when(osRepository.findById(osId)).thenReturn(Optional.of(existente));
        when(orcamentoService.recalcular(osId)).thenAnswer(i -> existente);
        when(osRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        var resp = service.enviarOrcamentoParaAprovacao(osId);

        assertThat(resp.getSituacao()).isEqualTo(SituacaoOrdemServico.AGUARDANDO_APROVACAO);
        verify(orcamentoService).recalcular(osId);
        verify(osRepository).save(existente);
    }

    @Test
    @DisplayName("enviarOrcamento_paraAprovacao_ok (EM_DIAGNOSTICO) → também deve funcionar")
    void enviarOrcamento_paraAprovacao_ok_emDiagnostico() {
        UUID osId = UUID.randomUUID();
        Cliente c = cliente(UUID.randomUUID(), "Fulano");
        Veiculo v = veiculo(UUID.randomUUID(), c);
        OrdemServico existente = os(osId, c, v, SituacaoOrdemServico.EM_DIAGNOSTICO);

        when(osRepository.findById(osId)).thenReturn(Optional.of(existente));
        when(orcamentoService.recalcular(osId)).thenReturn(existente);
        when(osRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        var resp = service.enviarOrcamentoParaAprovacao(osId);

        assertThat(resp.getSituacao()).isEqualTo(SituacaoOrdemServico.AGUARDANDO_APROVACAO);
        verify(orcamentoService).recalcular(osId);
    }

    @Test
    @DisplayName("enviarOrcamento_paraAprovacao_statusInvalido → deve falhar quando status não é RECEBIDA/EM_DIAGNOSTICO")
    void enviarOrcamento_paraAprovacao_statusInvalido() {
        UUID osId = UUID.randomUUID();
        OrdemServico existente = new OrdemServico();
        existente.setId(osId);
        existente.setSituacao(SituacaoOrdemServico.EM_EXECUCAO);

        when(osRepository.findById(osId)).thenReturn(Optional.of(existente));

        assertThatThrownBy(() -> service.enviarOrcamentoParaAprovacao(osId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Status inválido");
        verify(orcamentoService, never()).recalcular(any());
        verify(osRepository, never()).save(any());
    }

    @Test
    @DisplayName("enviarOrcamento_paraAprovacao_notFound → deve lançar 404 se OS não existir")
    void enviarOrcamento_paraAprovacao_notFound() {
        UUID osId = UUID.randomUUID();
        when(osRepository.findById(osId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.enviarOrcamentoParaAprovacao(osId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("OS não encontrada");
    }
}
