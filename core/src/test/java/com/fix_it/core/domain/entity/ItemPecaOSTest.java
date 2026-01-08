package com.fix_it.core.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ItemPecaOSTest {

    @Test
    @DisplayName("Deve criar um novo item de peça OS com valor total calculado")
    void deveCriarNovoItemPecaOS() {
        String nome = "Pastilha";
        String desc = "Dianteira";
        Integer qtd = 2;
        Long valorUnitario = 5000L;
        
        ItemPecaOS item = ItemPecaOS.novo(nome, desc, qtd, valorUnitario, null, null);

        assertThat(item.getId()).isNull();
        assertThat(item.getNome()).isEqualTo(nome);
        assertThat(item.getQuantidade()).isEqualTo(qtd);
        assertThat(item.getValorUnitario()).isEqualTo(valorUnitario);
        assertThat(item.getValorTotal()).isEqualTo(10000L); // 2 * 5000
    }

    @Test
    @DisplayName("Deve criar uma instância de item de peça OS com todos os campos")
    void deveCriarItemPecaOSComOf() {
        UUID id = UUID.randomUUID();
        ItemPecaOS item = ItemPecaOS.of(id, "Peca", "Desc", 1, 100L, 100L, null, null);

        assertThat(item.getId()).isEqualTo(id);
        assertThat(item.getNome()).isEqualTo("Peca");
        assertThat(item.getValorTotal()).isEqualTo(100L);
    }

}
