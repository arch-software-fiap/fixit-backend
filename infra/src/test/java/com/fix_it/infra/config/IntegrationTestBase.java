package com.fix_it.infra.config;

import com.fix_it.infra.InfraApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Classe base para testes de integração.
 *
 * Configura automaticamente:
 * - Container PostgreSQL via Testcontainers
 * - Profile "test" para configurações específicas de teste
 * - Spring Boot Test com contexto completo
 * - Configuração de segurança permissiva para testes
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {InfraApplication.class, TestSecurityConfig.class}
)
@Testcontainers
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@TestPropertySource(properties = {
        "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:8180/realms/test/protocol/openid-connect/certs"
})
public abstract class IntegrationTestBase {
}