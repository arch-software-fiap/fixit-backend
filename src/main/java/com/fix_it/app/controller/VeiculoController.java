package com.fix_it.app.controller;

import com.fix_it.app.common.dto.VeiculoDTO;
import com.fix_it.app.service.VeiculoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/veiculos")
@RequiredArgsConstructor
public class VeiculoController {

    private final VeiculoService veiculoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VeiculoDTO create(@Valid @RequestBody VeiculoDTO dto) {
        return veiculoService.create(dto);
    }

    @GetMapping("/{id}")
    public VeiculoDTO findById(@PathVariable UUID id) {
        return veiculoService.findById(id);
    }

    @GetMapping
    public Page<VeiculoDTO> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return veiculoService.findAll(page, size);
    }

    @PutMapping("/{id}")
    public VeiculoDTO update(@PathVariable UUID id, @Valid @RequestBody VeiculoDTO dto) {
        return veiculoService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable UUID id) {
        veiculoService.deleteById(id);
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
