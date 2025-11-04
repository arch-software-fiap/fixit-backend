package com.fix_it.app.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fix_it.app.common.dto.OrdemServicoDTO;
import com.fix_it.app.model.view.OrdemServicoView;
import com.fix_it.app.model.OrdemServico;
import com.fix_it.app.service.OrdemServicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ordem-servico")
@RequiredArgsConstructor
public class OrdemServicoController {

    private final OrdemServicoService ordemServicoService;

    @GetMapping
    public ResponseEntity<Page<OrdemServico>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ordemServicoService.findAll(page, size));
    }

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

    @PostMapping("/{id}/enviar-orcamento")
    public ResponseEntity<OrdemServico> enviarOrcamento(@PathVariable UUID id) {
        return ResponseEntity.ok(ordemServicoService.enviarOrcamentoParaAprovacao(id));
    }

    @PostMapping("/{id}/recalcular-orcamento")
    public ResponseEntity<OrdemServico> recalcular(@PathVariable UUID id) {
        return ResponseEntity.ok(ordemServicoService.recalcular(id));
    }

    @PutMapping("/{id}/aprovar-orcamento")
    public OrdemServico enviarOrcamentoParaAprovacao(@PathVariable UUID id) {
        return ordemServicoService.enviarOrcamentoParaAprovacao(id);
    }


    @GetMapping("/cliente/{cpfCnpj}")
    public ResponseEntity<Page<OrdemServico>> findAllByCliente(
            @PathVariable String cpfCnpj,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ordemServicoService.findAllByClienteCpfCnpj(cpfCnpj, page, size));
    }

    @GetMapping("/cliente/{cpfCnpj}/{id}")
    public ResponseEntity<OrdemServicoView> findAllByClienteDetalhado(
            @PathVariable String cpfCnpj,
            @PathVariable UUID id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var osd =  ordemServicoService.findByClienteCpfCnpjDetalhado(cpfCnpj,id).orElseThrow();
        return ResponseEntity.ok(osd);
    }

}
