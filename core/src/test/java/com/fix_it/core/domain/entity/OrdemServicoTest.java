package com.fix_it.core.domain.entity;

import com.fix_it.core.domain.enums.SituacaoOrdemServico;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrdemServicoTest {

    @Test
    @DisplayName("Deve criar uma nova OS com valores iniciais")
    void deveCriarNovaOS() {
        Cliente cliente = Cliente.novo("João", "123", "j@j.com", "456");
        Veiculo veiculo = Veiculo.novo("Civic", "Sedan", "ABC-1234", "Honda", "EXL", 2022, cliente);
        String descricao = "Troca de óleo";

        OrdemServico os = OrdemServico.nova(descricao, cliente, veiculo);

        assertThat(os.getId()).isNull();
        assertThat(os.getSituacao()).isEqualTo(SituacaoOrdemServico.RECEBIDA);
        assertThat(os.getDescricao()).isEqualTo(descricao);
        assertThat(os.getCliente()).isEqualTo(cliente);
        assertThat(os.getVeiculo()).isEqualTo(veiculo);
        assertThat(os.getDataAberturaEm()).isBeforeOrEqualTo(LocalDateTime.now());
        assertThat(os.getDataFechamentoEm()).isNull();
    }

    @Test
    @DisplayName("Deve criar uma instância de OS com todos os campos")
    void deveCriarOSComOf() {
        UUID id = UUID.randomUUID();
        Cliente cliente = Cliente.novo("João", "123", "j@j.com", "456");
        Veiculo veiculo = Veiculo.novo("Civic", "Sedan", "ABC-1234", "Honda", "EXL", 2022, cliente);
        LocalDateTime abertura = LocalDateTime.now().minusDays(1);
        LocalDateTime fechamento = LocalDateTime.now();

        OrdemServico os = OrdemServico.of(id, SituacaoOrdemServico.FINALIZADA, "Desc", abertura, fechamento, 100L, 100L, cliente, veiculo);

        assertThat(os.getId()).isEqualTo(id);
        assertThat(os.getSituacao()).isEqualTo(SituacaoOrdemServico.FINALIZADA);
        assertThat(os.getDataAberturaEm()).isEqualTo(abertura);
        assertThat(os.getDataFechamentoEm()).isEqualTo(fechamento);
    }

    @Test
    @DisplayName("Deve atualizar descrição da OS")
    void deveAtualizarDescricao() {
        OrdemServico os = OrdemServico.nova("Antiga", null, null);
        os.atualizarDescricao("Nova");
        assertThat(os.getDescricao()).isEqualTo("Nova");
    }

    @Test
    @DisplayName("Não deve atualizar descrição de OS finalizada ou entregue")
    void naoDeveAtualizarDescricaoOSFinalizada() {
        OrdemServico os = OrdemServico.nova("Desc", null, null);
        
        os.alterarSituacao(SituacaoOrdemServico.FINALIZADA);
        assertThatThrownBy(() -> os.atualizarDescricao("Nova"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("OS finalizada/entregue não pode ser atualizada.");

        os.alterarSituacao(SituacaoOrdemServico.ENTREGUE);
        assertThatThrownBy(() -> os.atualizarDescricao("Nova"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("OS finalizada/entregue não pode ser atualizada.");
    }

    @Test
    @DisplayName("Deve alterar situação e definir data de fechamento")
    void deveAlterarSituacao() {
        OrdemServico os = OrdemServico.nova("Desc", null, null);
        assertThat(os.getDataFechamentoEm()).isNull();

        os.alterarSituacao(SituacaoOrdemServico.EM_EXECUCAO);
        assertThat(os.getSituacao()).isEqualTo(SituacaoOrdemServico.EM_EXECUCAO);
        assertThat(os.getDataFechamentoEm()).isNull();

        os.alterarSituacao(SituacaoOrdemServico.FINALIZADA);
        assertThat(os.getSituacao()).isEqualTo(SituacaoOrdemServico.FINALIZADA);
        assertThat(os.getDataFechamentoEm()).isNotNull();
    }

    @Test
    @DisplayName("Deve atualizar valores da OS")
    void deveAtualizarValores() {
        OrdemServico os = OrdemServico.nova("Desc", null, null);
        os.atualizarValores(500L, 450L);
        assertThat(os.getValorOrcamentoTotal()).isEqualTo(500L);
        assertThat(os.getValorTotalFinal()).isEqualTo(450L);
    }

    @Test
    @DisplayName("Deve validar equals e hashCode baseados no ID")
    void deveValidarEqualsEHashCode() {
        UUID id = UUID.randomUUID();
        OrdemServico os1 = OrdemServico.of(id, null, null, null, null, null, null, null, null);
        OrdemServico os2 = OrdemServico.of(id, null, null, null, null, null, null, null, null);
        OrdemServico os3 = OrdemServico.of(UUID.randomUUID(), null, null, null, null, null, null, null, null);

        assertThat(os1).isEqualTo(os2);
        assertThat(os1).isNotEqualTo(os3);
        assertThat(os1.hashCode()).isEqualTo(os2.hashCode());
    }
}
