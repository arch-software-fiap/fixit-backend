package com.fix_it.controller;

import com.fix_it.usecase.ordemservico.*;
import com.fix_it.usecase.ordemservico.input.AdicionarItemPecaInput;
import com.fix_it.usecase.ordemservico.input.AdicionarItemServicoInput;
import com.fix_it.usecase.ordemservico.input.CriarOrdemServicoInput;
import com.fix_it.usecase.ordemservico.output.OrdemServicoOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ordem-servico")
@RequiredArgsConstructor
public class OrdemServicoController {

    private final CriarOrdemServicoUseCase criarOrdemServicoUseCase;
    private final BuscarOrdemServicoPorIdUseCase buscarOrdemServicoPorIdUseCase;
    private final AdicionarItemServicoUseCase adicionarItemServicoUseCase;
    private final AdicionarItemPecaUseCase adicionarItemPecaUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrdemServicoOutput create(@RequestBody CriarOrdemServicoInput input) {
        return criarOrdemServicoUseCase.executar(input);
    }

    @GetMapping("/{id}")
    public OrdemServicoOutput findById(@PathVariable UUID id) {
        return buscarOrdemServicoPorIdUseCase.executar(id);
    }

    @PostMapping("/{osId}/item-servico")
    public void adicionarItemServico(@PathVariable UUID osId, @RequestBody AdicionarItemServicoInput input) {
        adicionarItemServicoUseCase.executar(osId, input);
    }

    @PostMapping("/{osId}/item-peca")
    public void adicionarItemPeca(@PathVariable UUID osId, @RequestBody AdicionarItemPecaInput input) {
        adicionarItemPecaUseCase.executar(osId, input);
    }
}
