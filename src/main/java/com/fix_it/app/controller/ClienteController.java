package com.fix_it.app.controller;

import com.fix_it.app.model.Cliente;
import com.fix_it.app.repository.ClienteRepository;
import com.fix_it.app.service.ClienteService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/cliente")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;
    private final ClienteRepository clienteRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Cliente> create(@Valid @RequestBody Cliente cliente) {
        return ResponseEntity.ok(clienteService.create(cliente));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com o ID: " + id)));
    }

    @GetMapping("/doc/{documento:\\d{11}|\\d{14}}")
    public ResponseEntity<Cliente> findByDocumento(@PathVariable String documento) {
        return ResponseEntity.ok(
                clienteRepository.findByCpfCnpj(documento)
                        .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com o documento: " + documento))
        );
    }

    @GetMapping
    public ResponseEntity<Page<Cliente>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(clienteRepository.findAll(PageRequest.of(page, size)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> update(@PathVariable UUID id, @Valid @RequestBody Cliente cliente) {
        return ResponseEntity.ok(clienteService.update(id, cliente));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable UUID id) {
        log.info("Iniciando deleteById do clienteId = {}", id);
        if (!clienteRepository.existsById(id)) {
            throw new EntityNotFoundException("Cliente não encontrado com o ID: " + id);
        }
        clienteRepository.deleteById(id);
    }

}
