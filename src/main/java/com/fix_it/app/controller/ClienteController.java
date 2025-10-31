package com.fix_it.app.controller;

import com.fix_it.app.common.dto.VeiculoDTO;
import com.fix_it.app.model.Cliente;
import com.fix_it.app.repository.ClienteRepository;
import com.fix_it.app.service.ClienteService;
import com.fix_it.app.service.VeiculoService;
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
@RequestMapping("/cliente")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;
    private final ClienteRepository clienteRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente create(@Valid @RequestBody Cliente cliente) {
        return clienteService.create(cliente);
    }

    @GetMapping("/{id}")
    public Cliente findById(@PathVariable UUID id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com o ID: " + id));
    }

    @GetMapping("/{documento}")
    public Cliente findByDocumento(@PathVariable String documento) {
        return clienteRepository.findByCpfCnpj(documento)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com o documento: " + documento));
    }

    @GetMapping
    public Page<Cliente> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return clienteRepository.findAll(PageRequest.of(page, size));
    }

    @PutMapping("/{id}")
    public Cliente update(@PathVariable UUID id, @Valid @RequestBody Cliente cliente) {
        return clienteService.update(id, cliente);
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

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
