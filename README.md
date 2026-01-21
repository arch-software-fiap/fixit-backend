# FixIt Backend - Fase 2

Este é o backend da aplicação **FixIt**, desenvolvido como parte do Tech Challenge de Pós-Graduação em **Arquitetura de Software** da **FIAP**.

---

## 🎯 Descrição da Solução e Objetivos da Fase

Após a implantação do sistema inicial, a oficina mecânica busca evoluir a aplicação para garantir qualidade, resiliência e escalabilidade, incorporando práticas modernas de infraestrutura e automação.

### Objetivos

- **Reduzir riscos operacionais** por meio de infraestrutura escalável.
- **Automatizar o provisionamento e o deploy** do ambiente.
- **Melhorar a qualidade e a organização do código** com Clean Architecture.
- **Preparar a aplicação para suportar grandes volumes** de ordens de serviço com escalabilidade dinâmica.

---

## 🏗️ Desenho da Arquitetura Proposta

### Componentes da Aplicação (Clean Architecture)

A arquitetura do projeto segue os princípios da **Clean Architecture** para garantir a separação de responsabilidades e a independência de frameworks. O diagrama abaixo ilustra a relação entre os quatro módulos do projeto.

![Arquitetura Limpa - Fix-it](.docs/clean-arch.svg)


### Infraestrutura Provisionada

### Fluxo de Deploy

---

## 🚀 Instruções de Execução e Deploy

### Execução Local (Docker Compose)

Para executar todo o ecossistema da aplicação localmente (aplicação, banco de dados e Keycloak), execute o seguinte comando na raiz do projeto:

```bash
docker compose up --build --remove-orphans
```

Para rodar apenas os serviços de infraestrutura e a aplicação separadamente:

1.  **Subir o banco de dados e Keycloak:**
    ```bash
    docker compose up postgres-fixit-backend keycloak-fixit-backend --remove-orphans
    ```
2.  **Executar a aplicação (em outro terminal):**
    ```bash
    mvn clean install && mvn spring-boot:run -pl infra
    ```

# CI/CD Pipeline

Este projeto utiliza GitHub Actions para automação de CI/CD. O workflow está configurado em `.github/workflows/build.yml`.

## Visão Geral

O pipeline executa automaticamente em:

| Evento | Branch |
|--------|--------|
| Push | `main` |
| Pull Request | `main` |
| Manual | `workflow_dispatch` |

## Jobs

O pipeline possui 3 jobs que executam sequencialmente:

```
tests → package → containerize
```

### 1. Tests

Executa os testes da aplicação.

```yaml
steps:
  - uses: actions/checkout@v4

  - name: Setup jdk
    uses: actions/setup-java@v4
    with:
      java-version: '21'
      distribution: 'temurin'
      cache: 'maven'

  - name: Tests
    run: mvn test
```

### 2. Package

Empacota a aplicação e armazena o artefato.

**Depende de:** `tests`

```yaml
steps:
  - uses: actions/checkout@v4

  - uses: actions/setup-java@v4
    with:
      java-version: '21'
      distribution: 'temurin'
      cache: 'maven'

  - name: Packaging application
    run: mvn package -DskipTests

  - name: Upload artifact jar
    uses: actions/upload-artifact@v4
    with:
      name: app-fixit-backend-${{ github.run_number }}
      path: infra/target/*.jar
      retention-days: 3
```

O artefato é mantido por **3 dias** no GitHub.

### 3. Containerize

Constrói e publica a imagem Docker no GitHub Container Registry (GHCR).

**Depende de:** `package`

**Permissões necessárias:**
- `contents: read`
- `packages: write`

```yaml
steps:
  - uses: actions/checkout@v4

  - name: Download artifact jar
    uses: actions/download-artifact@v4
    with:
      name: app-fixit-backend-${{ github.run_number }}
      path: infra/target

  - name: Log in to GitHub Container Registry
    uses: docker/login-action@v3
    with:
      registry: ghcr.io
      username: ${{ github.actor }}
      password: ${{ secrets.GITHUB_TOKEN }}

  - name: Set up Docker Buildx
    uses: docker/setup-buildx-action@v3

  - name: Build and push image
    uses: docker/build-push-action@v6
    with:
      context: .
      push: true
      tags: |
        ghcr.io/${{ github.repository }}:latest
        ghcr.io/${{ github.repository }}:${{ github.sha }}
      cache-from: type=gha
      cache-to: type=gha,mode=max
```

## Imagem Docker

A imagem é publicada no GHCR com as seguintes tags:

| Tag | Descrição |
|-----|-----------|
| `latest` | Última versão da branch main |
| `<commit-sha>` | Versão específica do commit |

**URL da imagem:**
```
ghcr.io/arch-software-fiap/fixit-backend:latest
```

## Variáveis de Ambiente

| Variável | Valor | Descrição |
|----------|-------|-----------|
| `JAVA_VERSION` | `21` | Versão do JDK |
| `ARTIFACT_NAME` | `app-fixit-backend-<run_number>` | Nome do artefato |

## Secrets Utilizados

| Secret | Descrição |
|--------|-----------|
| `GITHUB_TOKEN` | Token automático para autenticação no GHCR |

## Executando Manualmente

O workflow pode ser executado manualmente através da interface do GitHub:

1. Acesse a aba **Actions** do repositório
2. Selecione o workflow **CI/CD Pipeline Java**
3. Clique em **Run workflow**
4. Selecione a branch e clique em **Run workflow**

## Cache

O pipeline utiliza cache em dois níveis:

1. **Maven**: Cache de dependências via `actions/setup-java`
2. **Docker**: Cache de layers via GitHub Actions cache (`type=gha`)


# Fluxo de Execução do Terraform

## 1. Instalar o Terraform CLI

Siga o guia oficial de instalação:
https://developer.hashicorp.com/terraform/tutorials/aws-get-started/install-cli

## 2. Configurar as Variáveis

```bash
cd terraform
cp terraform.tfvars.example terraform.tfvars
```

Edite o arquivo `terraform.tfvars` e preencha com as credenciais do GitHub disponíveis no PDF de entrega do projeto:

```hcl
ghcr_username = "seu-usuario-github"
ghcr_token    = "seu-token-github"
```

ghcr_username pode ser "bot" ou outro de sua escolha

> **Nota:** O token do GitHub deve ter permissão `read:packages` para baixar imagens do GHCR.

## 3. Inicializar o Terraform

```bash
terraform init
```

## 4. Aplicar a Configuração

```bash
terraform apply -auto-approve
```

## 5. Destruir o Ambiente

```bash
terraform destroy -auto-approve
```
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


---

## 📖 Documentação da API

A documentação completa das APIs está disponível através dos seguintes links:

- **Postman Collection:** Uma coleção completa para testes dos endpoints está disponível no diretório do projeto. Basta importar o seguinte arquivo no Postman:
  > `.postman/fixit_backend.postman_collection.json`

---

## 🛠️ Tecnologias Utilizadas

- **Java 21 (LTS)** & **Spring Boot 3**
- **PostgreSQL** & **Flyway**
- **Docker** & **Docker Compose**
- **Kubernetes** & **Terraform**
- **Keycloak** para autenticação/autorização
- **Maven** & **SonarQube**

---

## 👥 Participantes do Projeto

- Matheus Leal (rm368173)
- Laura Alves (rm368613)
- Marcio Souza (rm368671)

Feito com 💛 pela equipe FixIt!
