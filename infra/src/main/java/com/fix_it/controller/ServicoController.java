package com.fix_it.controller;

import com.fix_it.usecase.servico.*;
import com.fix_it.usecase.servico.input.AtualizarServicoInput;
import com.fix_it.usecase.servico.input.CriarServicoInput;
import com.fix_it.usecase.servico.output.ServicoOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/servicos")
@RequiredArgsConstructor
public class ServicoController {

    private final CriarServicoUseCase criarServicoUseCase;
    private final AtualizarServicoUseCase atualizarServicoUseCase;
    private final BuscarServicoPorIdUseCase buscarServicoPorIdUseCase;
    private final ListarServicosUseCase listarServicosUseCase;
    private final RemoverServicoUseCase removerServicoUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServicoOutput create(@RequestBody CriarServicoInput input) {
        return criarServicoUseCase.executar(input);
    }

    @GetMapping("/{id}")
    public ServicoOutput findById(@PathVariable UUID id) {
        return buscarServicoPorIdUseCase.executar(id);
    }

    @GetMapping
    public List<ServicoOutput> findAll() {
        return listarServicosUseCase.executar();
    }

    @PutMapping("/{id}")
    public ServicoOutput update(@PathVariable UUID id, @RequestBody AtualizarServicoInput input) {
        return atualizarServicoUseCase.executar(id, input);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable UUID id) {
        removerServicoUseCase.executar(id);
    }
}
