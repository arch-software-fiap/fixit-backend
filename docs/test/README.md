# Testes de Integração com Testcontainers

Este módulo utiliza [Testcontainers](https://testcontainers.com/) para executar testes de integração com um banco de dados PostgreSQL real em container Docker.

## Pré-requisitos

- [Docker](https://docs.docker.com/get-docker/) instalado e em execução

## Estrutura

```
infra/src/test/java/com/fix_it/infra/
├── config/
│   ├── TestcontainersConfiguration.java  # Configuração do container PostgreSQL
│   ├── IntegrationTestBase.java          # Base para testes de controller
│   ├── RepositoryTestBase.java           # Base para testes de repositório
│   └── TestSecurityConfig.java           # Configuração de segurança para testes
├── controller/
│   ├── ClienteControllerIntegrationTest.java
│   └── ServicoControllerIntegrationTest.java
└── repository/
    ├── ClienteRepositoryIntegrationTest.java
    └── ServicoRepositoryIntegrationTest.java
```

## Configuração do Testcontainers

A classe `TestcontainersConfiguration` define o container PostgreSQL:

```java
@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:16"))
                .withDatabaseName("fixitbackend_test")
                .withUsername("test")
                .withPassword("test")
                .withReuse(true);
    }
}
```

A anotação `@ServiceConnection` configura automaticamente as propriedades de conexão do Spring Boot com o container.

## Classes Base

### `RepositoryTestBase`

Classe base para testes de repositório com contexto JPA focado.

**Anotações utilizadas:**

| Anotação | Descrição |
|----------|-----------|
| `@DataJpaTest` | Carrega apenas componentes JPA (repositories, entities) |
| `@Testcontainers` | Habilita o gerenciamento de containers |
| `@ActiveProfiles("test")` | Ativa o profile de teste |
| `@AutoConfigureTestDatabase(replace = NONE)` | Usa o banco real (container) em vez do H2 |
| `@Import(TestcontainersConfiguration.class)` | Importa a configuração do container |

**Exemplo de uso:**

```java
@DisplayName("ClienteRepository - Testes de Integração")
class ClienteRepositoryIntegrationTest extends RepositoryTestBase {

    @Autowired
    private SpringDataClienteJpaRepository clienteRepository;

    private ClienteEntity clienteExistente;

    @BeforeEach
    void setUp() {
        clienteRepository.deleteAll();

        clienteExistente = new ClienteEntity();
        clienteExistente.setNome("João Silva");
        clienteExistente.setCpfCnpj("12345678901");
        clienteExistente.setEmail("joao@email.com");
        clienteExistente.setTelefone("11999999999");
        clienteExistente = clienteRepository.save(clienteExistente);
    }

    @Nested
    @DisplayName("Operações de Persistência")
    class OperacoesPersistencia {

        @Test
        @DisplayName("Deve salvar um novo cliente com ID gerado automaticamente")
        void deveSalvarNovoCliente() {
            ClienteEntity novoCliente = new ClienteEntity();
            novoCliente.setNome("Maria Santos");
            novoCliente.setCpfCnpj("98765432100");
            novoCliente.setEmail("maria@email.com");

            ClienteEntity clienteSalvo = clienteRepository.save(novoCliente);

            assertThat(clienteSalvo.getId()).isNotNull();
            assertThat(clienteSalvo.getNome()).isEqualTo("Maria Santos");
            assertThat(clienteSalvo.getDtCadastro()).isNotNull();
        }

        @Test
        @DisplayName("Deve atualizar cliente existente")
        void deveAtualizarClienteExistente() {
            clienteExistente.setNome("João Silva Atualizado");

            ClienteEntity clienteAtualizado = clienteRepository.save(clienteExistente);

            assertThat(clienteAtualizado.getNome()).isEqualTo("João Silva Atualizado");
        }

        @Test
        @DisplayName("Deve remover cliente por ID")
        void deveRemoverClientePorId() {
            UUID idCliente = clienteExistente.getId();

            clienteRepository.deleteById(idCliente);

            Optional<ClienteEntity> clienteRemovido = clienteRepository.findById(idCliente);
            assertThat(clienteRemovido).isEmpty();
        }
    }

    @Nested
    @DisplayName("Operações de Busca")
    class OperacoesBusca {

        @Test
        @DisplayName("Deve buscar cliente por ID")
        void deveBuscarClientePorId() {
            Optional<ClienteEntity> clienteEncontrado = clienteRepository.findById(clienteExistente.getId());

            assertThat(clienteEncontrado).isPresent();
            assertThat(clienteEncontrado.get().getNome()).isEqualTo("João Silva");
        }

        @Test
        @DisplayName("Deve buscar cliente por CPF/CNPJ")
        void deveBuscarClientePorCpfCnpj() {
            Optional<ClienteEntity> clienteEncontrado = clienteRepository.findByCpfCnpj("12345678901");

            assertThat(clienteEncontrado).isPresent();
        }
    }

    @Nested
    @DisplayName("Verificações de Existência")
    class VerificacoesExistencia {

        @Test
        @DisplayName("Deve retornar true quando CPF/CNPJ existe")
        void deveRetornarTrueQuandoCpfCnpjExiste() {
            boolean existe = clienteRepository.existsByCpfCnpj("12345678901");

            assertThat(existe).isTrue();
        }
    }
}
```

### `IntegrationTestBase`

Classe base para testes de controller com contexto Spring Boot completo.

**Anotações utilizadas:**

| Anotação | Descrição |
|----------|-----------|
| `@SpringBootTest(webEnvironment = RANDOM_PORT)` | Carrega contexto completo com servidor web |
| `@Testcontainers` | Habilita o gerenciamento de containers |
| `@ActiveProfiles("test")` | Ativa o profile de teste |
| `@Import(TestcontainersConfiguration.class)` | Importa a configuração do container |
| `@TestPropertySource` | Configura propriedades específicas de teste |

**Exemplo de uso:**

```java
@DisplayName("ClienteController - Testes de Integração")
class ClienteControllerIntegrationTest extends IntegrationTestBase {

    private static final String BASE_URL = "/api/v1/clientes";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SpringDataClienteJpaRepository clienteRepository;

    private ClienteEntity clienteExistente;

    @BeforeEach
    void setUp() {
        clienteRepository.deleteAll();

        clienteExistente = new ClienteEntity();
        clienteExistente.setNome("João Silva");
        clienteExistente.setCpfCnpj("12345678901");
        clienteExistente.setEmail("joao@email.com");
        clienteExistente.setTelefone("11999999999");
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
                    "11888888888"
            );

            ResponseEntity<Map> response = restTemplate.postForEntity(BASE_URL, request, Map.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().get("nome")).isEqualTo("Maria Santos");
            assertThat(response.getBody().get("id")).isNotNull();
        }

        @Test
        @DisplayName("Deve retornar erro ao criar cliente com dados inválidos")
        void deveRetornarErroComDadosInvalidos() {
            ClienteRequest request = new ClienteRequest("", "", "email-invalido", "11888888888");

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
            assertThat(response.getBody()).hasSize(1);
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
                    "11777777777"
            );

            ResponseEntity<Map> response = restTemplate.exchange(
                    url, HttpMethod.PUT, new HttpEntity<>(request), Map.class
            );

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().get("nome")).isEqualTo("João Silva Atualizado");
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
                    url, HttpMethod.DELETE, null, Void.class
            );

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            assertThat(clienteRepository.findById(clienteExistente.getId())).isEmpty();
        }
    }
}
```

## Executando os Testes

```bash
# Executar todos os testes do módulo infra
./mvnw test -pl infra

# Executar testes de integração específicos
./mvnw test -pl infra -Dtest=*IntegrationTest

# Executar teste de um controller específico
./mvnw test -pl infra -Dtest=ClienteControllerIntegrationTest

# Executar teste de um repository específico
./mvnw test -pl infra -Dtest=ClienteRepositoryIntegrationTest
```

## Container PostgreSQL

O container é configurado automaticamente com:

| Propriedade | Valor               |
|-------------|---------------------|
| Imagem      | `postgres:16`       |
| Database    | `fixitbackend_test` |
| Username    | `test`              |
| Password    | `test`              |
| Reuse       | `true`              |

## Reuso de Container

O Testcontainers está configurado com `withReuse(true)`, permitindo reutilizar o container entre execuções de teste, o que acelera significativamente o ciclo de desenvolvimento.

Para habilitar o reuso globalmente, adicione ao arquivo `~/.testcontainers.properties`:

```properties
testcontainers.reuse.enable=true
```

## Boas Práticas

1. **Limpeza de dados**: Sempre limpe os dados no `@BeforeEach` para garantir isolamento entre testes
2. **Dados de teste**: Crie dados de teste no `@BeforeEach` que serão utilizados nos cenários
3. **Organização com `@Nested`**: Agrupe testes relacionados usando classes aninhadas
4. **Nomes descritivos**: Use `@DisplayName` para descrever claramente o que cada teste valida
5. **AssertJ**: Utilize AssertJ para asserções fluentes e legíveis
