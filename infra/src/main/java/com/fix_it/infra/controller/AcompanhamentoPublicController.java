package com.fix_it.infra.controller;

import com.fix_it.usecase.ordemservico.ConsultarAcompanhamentoUseCase;
import com.fix_it.usecase.ordemservico.output.AcompanhamentoOSOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/public/ordens-servico")
@RequiredArgsConstructor
public class AcompanhamentoPublicController {

    private final ConsultarAcompanhamentoUseCase consultarAcompanhamentoUseCase;

    @GetMapping("/{id}/acompanhamento")
    public AcompanhamentoOSOutput consultar(@PathVariable UUID id, @RequestParam("doc") String cpfCnpj) {
        return consultarAcompanhamentoUseCase.executar(id, cpfCnpj);
    }
}
