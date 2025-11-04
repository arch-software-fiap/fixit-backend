package com.fix_it.app.it;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.net.URI;
import java.time.Duration;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class OrdemServicoFindAllIT {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:18")
                    .withDatabaseName("fixitbackend")
                    .withUsername("fixitbackend")
                    .withPassword("fixitbackend");

    @Container
    static KeycloakContainer keycloak =
            new KeycloakContainer("quay.io/keycloak/keycloak:23.0")
                    .withRealmImportFile("keycloak/realm-export.json");

    @LocalServerPort
    int port;

    static String issuer;

    @DynamicPropertySource
    static void registerProps(DynamicPropertyRegistry r) {
        // DataSource
        r.add("spring.datasource.url", postgres::getJdbcUrl);
        r.add("spring.datasource.username", postgres::getUsername);
        r.add("spring.datasource.password", postgres::getPassword);

        // JPA/Hibernate para teste
        r.add("spring.jpa.hibernate.ddl-auto", () -> "validate"); // Flyway deve criar
        r.add("spring.flyway.enabled", () -> "true");

        // Resource Server (JWT)
        r.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> {
            issuer = keycloak.getAuthServerUrl() + "/realms/fixit";
            return issuer;
        });
    }

    private String obtainToken(String username, String password) {
        var tokenUri = URI.create(issuer + "/protocol/openid-connect/token");
        WebClient client = WebClient.builder().build();

        Map<String, String> body = Map.of(
                "grant_type", "password",
                "client_id", "fixit_backend",
                "username", username,
                "password", password
        );

        var form = body.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .reduce((a, b) -> a + "&" + b).orElse("");

        var response = client.post()
                .uri(tokenUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(form)
                .retrieve()
                .bodyToMono(Map.class)
                .block(Duration.ofSeconds(10));

        Assertions.assertNotNull(response);
        return (String) response.get("access_token");
    }

    @Test
    @DisplayName("GET /api/os?page=0&size=2 → 200 com JWT do Keycloak")
    void findAll_withJwt_ok() {
        String token = obtainToken("user", "123");

        WebTestClient http = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .responseTimeout(Duration.ofSeconds(10))
                .build();

        http.get()
                .uri("/api/os?page=0&size=2")
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.content").isArray();
    }
}
