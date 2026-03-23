package com.fix_it.infra.controller;

import com.fix_it.usecase.ordemservico.ConsultarAcompanhamentoUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.verify;

class AcompanhamentoPublicControllerTest {

    private final ConsultarAcompanhamentoUseCase consultarAcompanhamentoUseCase = Mockito.mock(ConsultarAcompanhamentoUseCase.class);
    private final AcompanhamentoPublicController controller = new AcompanhamentoPublicController(consultarAcompanhamentoUseCase);

    @Test
    void deveUsarClaimCpfDoTokenParaFiltrarCliente() {
        UUID osId = UUID.randomUUID();
        Jwt jwt = jwt(Map.of("cpf", "12345678901"), "12345678901");

        controller.consultar(osId, jwt);

        verify(consultarAcompanhamentoUseCase).executar(osId, "12345678901");
    }

    @Test
    void deveUsarSubjectQuandoClaimCpfNaoExistir() {
        UUID osId = UUID.randomUUID();
        Jwt jwt = jwt(Map.of(), "12345678901");

        controller.consultar(osId, jwt);

        verify(consultarAcompanhamentoUseCase).executar(osId, "12345678901");
    }

    private Jwt jwt(Map<String, Object> claims, String subject) {
        return new Jwt(
                "token-value",
                Instant.now(),
                Instant.now().plusSeconds(3600),
                Map.of("alg", "HS256"),
                claims.isEmpty() ? Map.of("sub", subject) : Map.of("sub", subject, "cpf", claims.get("cpf"))
        );
    }
}
