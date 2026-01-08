package com.fix_it.core.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ServicoTest {

    @Test
    @DisplayName("Deve criar um novo serviço com valores iniciais")
    void deveCriarNovoServico() {
        String nome = "Troca de Óleo";
        String descricao = "Troca de óleo e filtro";
        Long preco = 15000L;

        Servico servico = Servico.novo(nome, descricao, preco);

        assertThat(servico.getId()).isNull();
        assertThat(servico.getNmServico()).isEqualTo(nome);
        assertThat(servico.getDsServico()).isEqualTo(descricao);
        assertThat(servico.getVlPrecoBase()).isEqualTo(preco);
        assertThat(servico.getDthAtualizacao()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    @DisplayName("Deve criar uma instância de serviço com todos os campos")
    void deveCriarServicoComOf() {
        UUID id = UUID.randomUUID();
        String nome = "Troca de Óleo";
        String descricao = "Troca de óleo e filtro";
        Long preco = 15000L;
        LocalDateTime agora = LocalDateTime.now();

        Servico servico = Servico.of(id, nome, descricao, preco, agora);

        assertThat(servico.getId()).isEqualTo(id);
        assertThat(servico.getNmServico()).isEqualTo(nome);
        assertThat(servico.getDsServico()).isEqualTo(descricao);
        assertThat(servico.getVlPrecoBase()).isEqualTo(preco);
        assertThat(servico.getDthAtualizacao()).isEqualTo(agora);
    }

    @Test
    @DisplayName("Deve atualizar os dados do serviço")
    void deveAtualizarServico() {
        Servico servico = Servico.novo("Nome Antigo", "Descricao Antiga", 10000L);
        LocalDateTime dthCriacao = servico.getDthAtualizacao();

        servico.atualizar("Nome Novo", "Descricao Nova", 20000L);

        assertThat(servico.getNmServico()).isEqualTo("Nome Novo");
        assertThat(servico.getDsServico()).isEqualTo("Descricao Nova");
        assertThat(servico.getVlPrecoBase()).isEqualTo(20000L);
        assertThat(servico.getDthAtualizacao()).isAfterOrEqualTo(dthCriacao);
    }

}
