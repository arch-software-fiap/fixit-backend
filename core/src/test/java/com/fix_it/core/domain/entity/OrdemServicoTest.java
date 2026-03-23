package com.fix_it.core.domain.entity;

import com.fix_it.core.domain.enums.SituacaoOrdemServico;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrdemServicoTest {

    @Test
    void deveCriarNovaOSComValoresIniciais() {
        Cliente cliente = Cliente.novo("João", "123", "j@j.com", "456", null);
        Veiculo veiculo = Veiculo.novo("Carro", "Descrição", "ABC1234", "Marca", "Modelo", 2020, cliente);

        OrdemServico os = OrdemServico.nova("Troca de óleo", cliente, veiculo);

        assertNull(os.getId());
        assertEquals(SituacaoOrdemServico.RECEBIDA, os.getSituacao());
        assertEquals("Troca de óleo", os.getDescricao());
        assertEquals(0L, os.getValorOrcamentoTotal());
        assertEquals(0L, os.getValorTotalFinal());
        assertEquals(cliente, os.getCliente());
        assertEquals(veiculo, os.getVeiculo());
        assertNotNull(os.getDataAberturaEm());
        assertNull(os.getDataFechamentoEm());
    }

    @Test
    void deveAtualizarDescricaoQuandoNaoFinalizada() {
        Cliente cliente = Cliente.novo("João", "123", "j@j.com", "456", null);
        Veiculo veiculo = Veiculo.novo("Carro", "Descrição", "ABC1234", "Marca", "Modelo", 2020, cliente);
        OrdemServico os = OrdemServico.nova("Antiga", cliente, veiculo);

        os.atualizarDescricao("Nova descrição");

        assertEquals("Nova descrição", os.getDescricao());
    }

    @Test
    void naoDeveAtualizarDescricaoQuandoFinalizadaOuEntregue() {
        Cliente cliente = Cliente.novo("João", "123", "j@j.com", "456", null);
        Veiculo veiculo = Veiculo.novo("Carro", "Descrição", "ABC1234", "Marca", "Modelo", 2020, cliente);
        OrdemServico os = OrdemServico.nova("Teste", cliente, veiculo);
        os.alterarSituacao(SituacaoOrdemServico.FINALIZADA);

        assertThrows(IllegalArgumentException.class, () -> os.atualizarDescricao("Outra"));
    }

    @Test
    void deveDefinirDataFechamentoQuandoFinalizadaOuEntregue() {
        Cliente cliente = Cliente.novo("João", "123", "j@j.com", "456", null);
        Veiculo veiculo = Veiculo.novo("Carro", "Descrição", "ABC1234", "Marca", "Modelo", 2020, cliente);
        OrdemServico os = OrdemServico.nova("Teste", cliente, veiculo);

        os.alterarSituacao(SituacaoOrdemServico.FINALIZADA);

        assertEquals(SituacaoOrdemServico.FINALIZADA, os.getSituacao());
        assertNotNull(os.getDataFechamentoEm());
    }

    @Test
    void deveAtualizarValores() {
        Cliente cliente = Cliente.novo("João", "123", "j@j.com", "456", null);
        Veiculo veiculo = Veiculo.novo("Carro", "Descrição", "ABC1234", "Marca", "Modelo", 2020, cliente);
        OrdemServico os = OrdemServico.nova("Teste", cliente, veiculo);

        os.atualizarValores(100L, 150L);

        assertEquals(100L, os.getValorOrcamentoTotal());
        assertEquals(150L, os.getValorTotalFinal());
    }

    @Test
    void deveUsarEqualsEHashCodePorId() {
        var id = java.util.UUID.randomUUID();
        Cliente cliente = Cliente.novo("João", "123", "j@j.com", "456", null);
        Veiculo veiculo = Veiculo.novo("Carro", "Descrição", "ABC1234", "Marca", "Modelo", 2020, cliente);
        OrdemServico os1 = OrdemServico.of(id, SituacaoOrdemServico.RECEBIDA, "A", java.time.LocalDateTime.now(), null, 0L, 0L, cliente, veiculo);
        OrdemServico os2 = OrdemServico.of(id, SituacaoOrdemServico.RECEBIDA, "B", java.time.LocalDateTime.now(), null, 0L, 0L, cliente, veiculo);

        assertEquals(os1, os2);
        assertEquals(os1.hashCode(), os2.hashCode());
        assertTrue(os1.equals(os2));
    }
}
