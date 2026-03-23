package com.fix_it.application.usecasesimpl;

import com.fix_it.core.domain.entity.Cliente;
import com.fix_it.core.domain.entity.OrdemServico;
import com.fix_it.core.domain.entity.Veiculo;
import com.fix_it.core.domain.enums.SituacaoOrdemServico;
import com.fix_it.usecase.port.OrdemServicoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class ConsultarAcompanhamentoUseCaseImplTest {

    private final OrdemServicoRepository repository = Mockito.mock(OrdemServicoRepository.class);
    private final ConsultarAcompanhamentoUseCaseImpl useCase = new ConsultarAcompanhamentoUseCaseImpl(repository);

    @Test
    void deveRetornarAcompanhamentoQuandoDocumentoDoTokenConfere() {
        UUID osId = UUID.randomUUID();
        OrdemServico ordemServico = criarOrdemServico(osId, "12345678901");
        when(repository.buscarPorId(osId)).thenReturn(Optional.of(ordemServico));

        var output = useCase.executar(osId, "12345678901");

        assertThat(output.id()).isEqualTo(osId);
        assertThat(output.cpfCnpjCliente()).isEqualTo("12345678901");
        assertThat(output.nomeCliente()).isEqualTo("Cliente Teste");
    }

    @Test
    void deveFalharQuandoDocumentoDoTokenNaoConfere() {
        UUID osId = UUID.randomUUID();
        OrdemServico ordemServico = criarOrdemServico(osId, "12345678901");
        when(repository.buscarPorId(osId)).thenReturn(Optional.of(ordemServico));

        assertThatThrownBy(() -> useCase.executar(osId, "99999999999"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cliente autenticado não possui acesso a esta OS");
    }

    private OrdemServico criarOrdemServico(UUID osId, String cpfCnpj) {
        Cliente cliente = Cliente.of(
                UUID.randomUUID(),
                "Cliente Teste",
                cpfCnpj,
                "cliente@email.com",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                LocalDate.now(),
                LocalDateTime.now()
        );

        Veiculo veiculo = Veiculo.of(
                UUID.randomUUID(),
                "Carro Teste",
                "Veiculo de teste",
                "AAA1A11",
                "Marca Teste",
                "Modelo Teste",
                2024,
                LocalDateTime.now(),
                cliente
        );

        return OrdemServico.of(
                osId,
                SituacaoOrdemServico.RECEBIDA,
                "Descricao teste",
                LocalDateTime.now(),
                null,
                1000L,
                1200L,
                cliente,
                veiculo
        );
    }
}
