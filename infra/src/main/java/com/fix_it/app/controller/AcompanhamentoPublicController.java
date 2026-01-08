package com.fix_it.app.controller;

import com.fix_it.app.common.dto.AcompanhamentoOSDTO;
import com.fix_it.app.service.AcompanhamentoPublicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/public/ordens-servico")
@RequiredArgsConstructor
public class AcompanhamentoPublicController {

    private final AcompanhamentoPublicService service;

    @GetMapping("/{id}/acompanhamento")
    public ResponseEntity<AcompanhamentoOSDTO> consultar(@PathVariable UUID id,
                                                         @RequestParam("doc") String cpfCnpj) {
        return ResponseEntity.ok(service.consultar(id, cpfCnpj));
    }


}
