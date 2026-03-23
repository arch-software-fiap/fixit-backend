package com.fix_it.infra.controller;

import com.fix_it.usecase.ordemservico.ConsultarAcompanhamentoUseCase;
import com.fix_it.usecase.ordemservico.output.AcompanhamentoOSOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/public/ordens-servico")
@RequiredArgsConstructor
@Slf4j
public class AcompanhamentoPublicController {

    private final ConsultarAcompanhamentoUseCase consultarAcompanhamentoUseCase;

    @GetMapping("/{id}/acompanhamento")
    public AcompanhamentoOSOutput consultar(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt) {
        String cpfCnpj = extrairCpfCnpj(jwt);
        log.info("Consultando acompanhamento da ordem de serviço {} para cliente {}", id, cpfCnpj);
        return consultarAcompanhamentoUseCase.executar(id, cpfCnpj);
    }

    private String extrairCpfCnpj(Jwt jwt) {
        String cpf = jwt.getClaimAsString("cpf");
        if (cpf != null && !cpf.isBlank()) {
            return cpf;
        }

        String subject = jwt.getSubject();
        if (subject != null && !subject.isBlank()) {
            return subject;
        }

        throw new IllegalArgumentException("Token JWT não contém identificador do cliente");
    }
}
