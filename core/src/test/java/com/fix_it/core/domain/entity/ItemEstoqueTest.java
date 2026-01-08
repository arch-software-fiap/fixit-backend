package com.fix_it.core.domain.entity;

import com.fix_it.core.domain.enums.TipoItemEstoque;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ItemEstoqueTest {

    @Test
    @DisplayName("Deve criar uma instância de item de estoque com todos os campos")
    void deveCriarItemEstoqueComOf() {
        UUID id = UUID.randomUUID();
        String nome = "Pastilha de Freio";
        String desc = "Pastilha dianteira";
        Long preco = 12000L;
        Integer atual = 10;
        Integer minimo = 5;
        LocalDateTime agora = LocalDateTime.now();
        TipoItemEstoque tipo = TipoItemEstoque.PECA;
        Boolean abaixoMinimo = false;

        ItemEstoque item = ItemEstoque.of(id, nome, desc, preco, atual, minimo, agora, tipo, abaixoMinimo);

        assertThat(item.getId()).isEqualTo(id);
        assertThat(item.getNmItemEstoque()).isEqualTo(nome);
        assertThat(item.getDsItemEstoque()).isEqualTo(desc);
        assertThat(item.getVlPrecoUnitario()).isEqualTo(preco);
        assertThat(item.getQtEstoqueAtual()).isEqualTo(atual);
        assertThat(item.getQtEstoqueMinimo()).isEqualTo(minimo);
        assertThat(item.getDthAtualizacao()).isEqualTo(agora);
        assertThat(item.getTipo()).isEqualTo(tipo);
        assertThat(item.getAbaixoMinimo()).isEqualTo(abaixoMinimo);
    }

}

