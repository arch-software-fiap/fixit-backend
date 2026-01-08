package com.fix_it.controller;

import com.fix_it.controller.dto.ClienteRequest;
import com.fix_it.controller.dto.ClienteResponse;
import com.fix_it.controller.mapper.ClienteApiMapper;
import com.fix_it.usecase.cliente.AtualizarClienteUseCase;
import com.fix_it.usecase.cliente.CriarClienteUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/cliente")
@RequiredArgsConstructor
public class ClienteController {


    private final CriarClienteUseCase criarClienteUseCase;
    private final AtualizarClienteUseCase atualizarClienteUseCase;
    private final ClienteApiMapper mapper;

    @PostMapping
    public ResponseEntity<ClienteResponse> criar(@RequestBody @Valid ClienteRequest request) {
        var cliente = criarClienteUseCase.execute(
                request.nome(),
                request.cpfCnpj(),
                request.email(),
                request.telefone()
        );

        var response = mapper.toResponse(cliente);

        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> atualizar(@PathVariable UUID id,
                                                     @RequestBody @Valid ClienteRequest request) {

        var cliente = atualizarClienteUseCase.execute(
                id,
                request.nome(),
                request.cpfCnpj(),
                request.email(),
                request.telefone()
        );

        var response = mapper.toResponse(cliente);
        return ResponseEntity.ok().body(response);
    }
}
