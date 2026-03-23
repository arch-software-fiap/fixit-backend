package com.fix_it.infra.controller;

import com.fix_it.infra.controller.dto.ClienteRequest;
import com.fix_it.infra.controller.dto.ClienteResponse;
import com.fix_it.infra.controller.mapper.ClienteApiMapper;
import com.fix_it.usecase.cliente.AtualizarClienteUseCase;
import com.fix_it.usecase.cliente.BuscarClientePorDocumentoUseCase;
import com.fix_it.usecase.cliente.BuscarClientePorIdUseCase;
import com.fix_it.usecase.cliente.CriarClienteUseCase;
import com.fix_it.usecase.cliente.ListarClientesUseCase;
import com.fix_it.usecase.cliente.RemoverClienteUseCase;
import com.fix_it.usecase.cliente.input.AtualizarClienteInput;
import com.fix_it.usecase.cliente.input.CriarClienteInput;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final CriarClienteUseCase criarClienteUseCase;
    private final AtualizarClienteUseCase atualizarClienteUseCase;
    private final ListarClientesUseCase listarClientesUseCase;
    private final BuscarClientePorIdUseCase buscarClientePorIdUseCase;
    private final BuscarClientePorDocumentoUseCase buscarClientePorDocumentoUseCase;
    private final RemoverClienteUseCase removerClienteUseCase;

    private final ClienteApiMapper mapper;

    @PostMapping
    public ResponseEntity<ClienteResponse> criar(@RequestBody @Valid ClienteRequest request) {
        var input = new CriarClienteInput(
                request.nome(),
                request.cpfCnpj(),
                request.email(),
                request.telefone(),
                request.dtNascimento()
        );

        var cliente = criarClienteUseCase.execute(input);
        var response = mapper.toResponse(cliente);

        return ResponseEntity
                .ok()
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> atualizar(@PathVariable UUID id,
                                                     @RequestBody @Valid ClienteRequest request) {
        var input = new AtualizarClienteInput(
                id,
                request.nome(),
                request.cpfCnpj(),
                request.email(),
                request.telefone(),
                request.dtNascimento()
        );

        var cliente = atualizarClienteUseCase.execute(input);
        var response = mapper.toResponse(cliente);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listar() {
        var clientes = listarClientesUseCase.execute();
        var response = clientes.stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable UUID id) {
        var cliente = buscarClientePorIdUseCase.execute(id);
        return ResponseEntity.ok(mapper.toResponse(cliente));
    }

    @GetMapping("/documento/{cpfCnpj}")
    public ResponseEntity<ClienteResponse> buscarPorDocumento(@PathVariable String cpfCnpj) {
        var cliente = buscarClientePorDocumentoUseCase.execute(cpfCnpj);
        return ResponseEntity.ok(mapper.toResponse(cliente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable UUID id) {
        removerClienteUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
