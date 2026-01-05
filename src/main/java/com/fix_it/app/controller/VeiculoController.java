package com.fix_it.app.controller;

import com.fix_it.app.common.dto.VeiculoDTO;
import com.fix_it.app.service.VeiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/veiculo")
@RequiredArgsConstructor
public class VeiculoController {

    private final VeiculoService veiculoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<VeiculoDTO> create(@Valid @RequestBody VeiculoDTO dto) {
        return ResponseEntity.ok(veiculoService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeiculoDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(veiculoService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<VeiculoDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(veiculoService.findAll(page, size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeiculoDTO> update(@PathVariable UUID id, @Valid @RequestBody VeiculoDTO dto) {
        return ResponseEntity.ok(veiculoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable UUID id) {
        veiculoService.deleteById(id);
    }

}
