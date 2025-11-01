package com.fix_it.app.controller;

import com.fix_it.app.common.dto.ItemPecaDTO;
import com.fix_it.app.model.ItemPecaOS;
import com.fix_it.app.service.ItemPecaOSService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/ordens-servico{osId}/pecas")
@AllArgsConstructor
public class ItemPecaOSController {

    private final ItemPecaOSService itemPecaOSService;

    @PostMapping
    public ResponseEntity<ItemPecaOS> adicionar(@PathVariable UUID osId,
                                                @Valid @RequestBody ItemPecaDTO dto) {
        return ResponseEntity.ok(itemPecaOSService.adicionar(osId, dto));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> remover(@PathVariable UUID osId,
                                        @PathVariable UUID itemId) {
        itemPecaOSService.remover(osId, itemId);
        return ResponseEntity.noContent().build();
    }
}
