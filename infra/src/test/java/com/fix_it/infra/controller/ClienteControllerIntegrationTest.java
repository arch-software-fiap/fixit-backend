package com.fix_it.infra.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fix_it.infra.config.IntegrationTestBase;
import com.fix_it.infra.controller.dto.ClienteRequest;
import com.fix_it.infra.domain.ClienteEntity;
import com.fix_it.infra.repository.SpringDataClienteJpaRepository;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ClienteController - Testes de Integração")
class ClienteControllerIntegrationTest extends IntegrationTestBase {

    private static final String BASE_URL = "/api/v1/clientes";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SpringDataClienteJpaRepository clienteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ClienteEntity clienteExistente;

    @BeforeEach
    void setUp() {
        clienteRepository.deleteAll();

        clienteExistente = new ClienteEntity();
        clienteExistente.setNome("João Silva");
        clienteExistente.setCpfCnpj("12345678901");
        clienteExistente.setEmail("joao@email.com");
        clienteExistente.setTelefone("11999999999");
        clienteExistente.setDtNascimento(LocalDate.of(1990, 1, 1));
        clienteExistente = clienteRepository.save(clienteExistente);
    }

    @Nested
    @DisplayName("POST /api/v1/clientes")
    class CriarCliente {

        @Test
        @DisplayName("Deve criar um novo cliente com sucesso")
        void deveCriarNovoClienteComSucesso() {
            ClienteRequest request = new ClienteRequest(
                    "Maria Santos",
                    "98765432100",
                    "maria@email.com",
                    "11888888888",
                    LocalDate.of(1992, 2, 2)
            );

            ResponseEntity<Map> response = restTemplate.postForEntity(BASE_URL, request, Map.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().get("nome")).isEqualTo("Maria Santos");
            assertThat(response.getBody().get("cpfCnpj")).isEqualTo("98765432100");
            assertThat(response.getBody().get("id")).isNotNull();
            assertThat(response.getBody().get("dtNascimento")).isEqualTo("02/02/1992");
        }

        @Test
        @DisplayName("Deve retornar erro ao criar cliente com dados inválidos")
        void deveRetornarErroComDadosInvalidos() {
            ClienteRequest request = new ClienteRequest(
                    "",
                    "",
                    "email-invalido",
                    "11888888888",
                    LocalDate.of(1992, 2, 2)
            );

            ResponseEntity<Map> response = restTemplate.postForEntity(BASE_URL, request, Map.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    @DisplayName("GET /api/v1/clientes")
    class ListarClientes {

        @Test
        @DisplayName("Deve listar todos os clientes")
        void deveListarTodosClientes() {
            ResponseEntity<List> response = restTemplate.getForEntity(BASE_URL, List.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody()).hasSize(1);
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há clientes")
        void deveRetornarListaVazia() {
            clienteRepository.deleteAll();

            ResponseEntity<List> response = restTemplate.getForEntity(BASE_URL, List.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEmpty();
        }
    }

    @Nested
    @DisplayName("GET /api/v1/clientes/{id}")
    class BuscarClientePorId {

        @Test
        @DisplayName("Deve buscar cliente por ID com sucesso")
        void deveBuscarClientePorIdComSucesso() {
            String url = BASE_URL + "/" + clienteExistente.getId();

            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().get("nome")).isEqualTo("João Silva");
        }

        @Test
        @DisplayName("Deve retornar 404 quando cliente não existe")
        void deveRetornar404QuandoClienteNaoExiste() {
            String url = BASE_URL + "/00000000-0000-0000-0000-000000000000";

            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("GET /api/v1/clientes/documento/{cpfCnpj}")
    class BuscarClientePorDocumento {

        @Test
        @DisplayName("Deve buscar cliente por CPF/CNPJ com sucesso")
        void deveBuscarClientePorCpfCnpjComSucesso() {
            String url = BASE_URL + "/documento/" + clienteExistente.getCpfCnpj();

            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().get("nome")).isEqualTo("João Silva");
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/clientes/{id}")
    class AtualizarCliente {

        @Test
        @DisplayName("Deve atualizar cliente com sucesso")
        void deveAtualizarClienteComSucesso() {
            String url = BASE_URL + "/" + clienteExistente.getId();
            ClienteRequest request = new ClienteRequest(
                    "João Silva Atualizado",
                    "12345678901",
                    "joao.atualizado@email.com",
                    "11777777777",
                    LocalDate.of(1991, 1, 1)
            );

            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    new HttpEntity<>(request),
                    Map.class
            );

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().get("nome")).isEqualTo("João Silva Atualizado");
            assertThat(response.getBody().get("email")).isEqualTo("joao.atualizado@email.com");
            assertThat(response.getBody().get("dtNascimento")).isEqualTo("01/01/1991");
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/clientes/{id}")
    class RemoverCliente {

        @Test
        @DisplayName("Deve remover cliente com sucesso")
        void deveRemoverClienteComSucesso() {
            String url = BASE_URL + "/" + clienteExistente.getId();

            ResponseEntity<Void> response = restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    null,
                    Void.class
            );

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            assertThat(clienteRepository.findById(clienteExistente.getId())).isEmpty();
        }
    }
}
