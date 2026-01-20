package com.fix_it.infra.config;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Classe base para testes de repositório.
 *
 * Configura automaticamente:
 * - Container PostgreSQL via Testcontainers
 * - Profile "test" para configurações específicas de teste
 * - DataJpaTest para contexto focado em JPA
 */
@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
public abstract class RepositoryTestBase {
}
