package com.fix_it.infra.controller;

import com.fix_it.usecase.veiculo.*;
import com.fix_it.usecase.veiculo.input.AtualizarVeiculoInput;
import com.fix_it.usecase.veiculo.input.CriarVeiculoInput;
import com.fix_it.usecase.veiculo.output.VeiculoOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/veiculo")
@RequiredArgsConstructor
public class VeiculoController {

    private final CriarVeiculoUseCase criarVeiculoUseCase;
    private final AtualizarVeiculoUseCase atualizarVeiculoUseCase;
    private final BuscarVeiculoPorIdUseCase buscarVeiculoPorIdUseCase;
    private final ListarVeiculosUseCase listarVeiculosUseCase;
    private final RemoverVeiculoUseCase removerVeiculoUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VeiculoOutput create(@RequestBody CriarVeiculoInput input) {
        return criarVeiculoUseCase.executar(input);
    }

    @GetMapping("/{id}")
    public VeiculoOutput findById(@PathVariable UUID id) {
        return buscarVeiculoPorIdUseCase.executar(id);
    }

    @GetMapping
    public List<VeiculoOutput> findAll() {
        return listarVeiculosUseCase.executar();
    }

    @PutMapping("/{id}")
    public VeiculoOutput update(@PathVariable UUID id, @RequestBody AtualizarVeiculoInput input) {
        return atualizarVeiculoUseCase.executar(id, input);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable UUID id) {
        removerVeiculoUseCase.executar(id);
    }
}
