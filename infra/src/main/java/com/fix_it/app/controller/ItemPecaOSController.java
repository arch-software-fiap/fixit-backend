package com.fix_it.app.controller;

import com.fix_it.app.common.dto.ItemPecaDTO;
import com.fix_it.app.model.ItemPecaOS;
import com.fix_it.app.service.ItemPecaOSService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/ordem-servico/{osId}/item-peca")
public class ItemPecaOSController {

    private final ItemPecaOSService itemPecaOSService;

    @PostMapping
    public ResponseEntity<ItemPecaOS> adicionar(@PathVariable("osId") UUID osId,
                                                @Valid @RequestBody ItemPecaDTO dto) {
        ItemPecaOS created = itemPecaOSService.adicionar(osId, dto);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/_batch")
    public ResponseEntity<List<ItemPecaOS>> adicionarLista(@PathVariable("osId") UUID osId,
                                                           @Valid @RequestBody List<@Valid ItemPecaDTO> itens) {
        List<ItemPecaOS> created = itemPecaOSService.adicionarLista(osId, itens);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> remover(@PathVariable("osId") UUID osId,
                                        @PathVariable("itemId") UUID itemId) {
        itemPecaOSService.remover(osId, itemId);
        return ResponseEntity.noContent().build();
    }
}
