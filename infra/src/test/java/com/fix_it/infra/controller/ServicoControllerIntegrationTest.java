package com.fix_it.infra.controller;

import com.fix_it.infra.config.IntegrationTestBase;
import com.fix_it.infra.domain.ServicoEntity;
import com.fix_it.infra.repository.SpringDataServicoRepository;
import com.fix_it.usecase.servico.input.CriarServicoInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ServicoController - Testes de Integração")
class ServicoControllerIntegrationTest extends IntegrationTestBase {

    private static final String BASE_URL = "/api/v1/servicos";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SpringDataServicoRepository servicoRepository;

    private ServicoEntity servicoExistente;

    @BeforeEach
    void setUp() {
        servicoRepository.deleteAll();

        servicoExistente = new ServicoEntity();
        servicoExistente.setNmServico("Troca de Óleo");
        servicoExistente.setDsServico("Troca de óleo do motor com filtro incluso");
        servicoExistente.setVlPrecoBase(15000L);
        servicoExistente.setDthAtualizacao(LocalDateTime.now());
        servicoExistente = servicoRepository.save(servicoExistente);
    }

    @Nested
    @DisplayName("POST /api/v1/servicos")
    class CriarServico {

        @Test
        @DisplayName("Deve criar um novo serviço com sucesso")
        void deveCriarNovoServicoComSucesso() {
            CriarServicoInput request = new CriarServicoInput(
                    "Alinhamento",
                    "Alinhamento e balanceamento das rodas",
                    12000L
            );

            ResponseEntity<Map> response = restTemplate.postForEntity(BASE_URL, request, Map.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().get("nmServico")).isEqualTo("Alinhamento");
            assertThat(response.getBody().get("id")).isNotNull();
        }
    }

    @Nested
    @DisplayName("GET /api/v1/servicos")
    class ListarServicos {

        @Test
        @DisplayName("Deve listar todos os serviços")
        void deveListarTodosServicos() {
            ResponseEntity<List> response = restTemplate.getForEntity(BASE_URL, List.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody()).hasSize(1);
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há serviços")
        void deveRetornarListaVazia() {
            servicoRepository.deleteAll();

            ResponseEntity<List> response = restTemplate.getForEntity(BASE_URL, List.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEmpty();
        }
    }

    @Nested
    @DisplayName("GET /api/v1/servicos/{id}")
    class BuscarServicoPorId {

        @Test
        @DisplayName("Deve buscar serviço por ID com sucesso")
        void deveBuscarServicoPorIdComSucesso() {
            String url = BASE_URL + "/" + servicoExistente.getId();

            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().get("nmServico")).isEqualTo("Troca de Óleo");
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/servicos/{id}")
    class RemoverServico {

        @Test
        @DisplayName("Deve remover serviço com sucesso")
        void deveRemoverServicoComSucesso() {
            String url = BASE_URL + "/" + servicoExistente.getId();

            ResponseEntity<Void> response = restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    null,
                    Void.class
            );

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            assertThat(servicoRepository.findById(servicoExistente.getId())).isEmpty();
        }
    }
}
