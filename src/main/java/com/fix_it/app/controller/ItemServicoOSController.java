package com.fix_it.app.controller;

import com.fix_it.app.common.dto.ItemServicoDTO;
import com.fix_it.app.model.ItemServicoOS;
import com.fix_it.app.service.ItemServicoOSService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/ordem-servico/{osId}/item-servico")
@AllArgsConstructor
public class ItemServicoOSController {

    private final ItemServicoOSService itemServicoOSService;

    @PostMapping
    public ResponseEntity<ItemServicoOS> adicionar(@PathVariable UUID osId,
                                                   @Valid @RequestBody ItemServicoDTO dto) {
        return ResponseEntity.ok(itemServicoOSService.adicionar(osId, dto));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> remover(@PathVariable UUID osId,
                                        @PathVariable UUID itemId) {
        itemServicoOSService.remover(osId, itemId);
        return ResponseEntity.noContent().build();
    }
}
