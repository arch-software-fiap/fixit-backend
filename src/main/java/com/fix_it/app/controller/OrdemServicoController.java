package com.fix_it.app.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fix_it.app.common.dto.ItemPecaDTO;
import com.fix_it.app.common.dto.ItemServicoDTO;
import com.fix_it.app.common.dto.OrdemServicoDTO;
import com.fix_it.app.model.ItemPecaOS;
import com.fix_it.app.model.ItemServicoOS;
import com.fix_it.app.model.OrdemServico;
import com.fix_it.app.service.OrdemServicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ordens-servico")
@RequiredArgsConstructor
public class OrdemServicoController {

    private final OrdemServicoService ordemServicoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrdemServico> create(@Valid
                               @RequestBody
                               @JsonView(OrdemServicoDTO.Views.Create.class)
                               OrdemServicoDTO dto) {
        return ResponseEntity.ok(ordemServicoService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdemServico> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(ordemServicoService.findById(id));
    }

    @PutMapping("/{id}")
    public OrdemServico update(@PathVariable UUID id,
                               @Valid
                               @RequestBody
                               @JsonView(OrdemServicoDTO.Views.Update.class)
                               OrdemServicoDTO dto) {
        return ordemServicoService.update(id, dto);
    }

    @PostMapping("/{osId}/servicos")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ItemServicoOS> adicionarServico(@PathVariable UUID osId, @Valid @RequestBody ItemServicoDTO dto) {
        return ResponseEntity.ok(ordemServicoService.adicionarServico(osId, dto));
    }

    @PostMapping("/{osId}/pecas")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemPecaOS adicionarPeca(@PathVariable UUID osId, @Valid @RequestBody ItemPecaDTO dto) {
        return ordemServicoService.adicionarPeca(osId, dto);
    }

    @PutMapping("/{id}/aprovar-orcamento")
    public OrdemServico enviarOrcamentoParaAprovacao(@PathVariable UUID id) {
        return ordemServicoService.enviarOrcamentoParaAprovacao(id);
    }
}
