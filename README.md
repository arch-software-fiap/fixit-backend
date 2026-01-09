# FixIt Backend

Este é o backend da aplicação **FixIt**, desenvolvido em **Java 21**, autenticação e autorização via **Keycloak** e persistência de dados usando **PostgreSQL**.

Este projeto foi desenvolvido como parte do Tech Challenge de Pós-Graduação em **Arquitetura de Software** da **FIAP**.

---

## 🛠️ Tecnologias utilizadas

- **Java 21 (LTS)**
- **Spring Boot 3.5.6**
- **PostgreSQL 18**
- **Keycloak 23**
- **Docker & Docker Compose**
- **Maven 3.x**
- **Flyway** (migrações automáticas de banco de dados)
- **SonarQube** (para análise estática de código)

---

## 🏗️ Arquitetura (Clean Architecture)

O projeto segue os princípios da **Clean Architecture**, visando a separação de preocupações, independência de frameworks e testabilidade. A estrutura está dividida nos seguintes módulos:

- **`core` (Enterprise Business Rules):** Contém as entidades de domínio e exceções de negócio. É o núcleo do sistema, livre de dependências externas.
- **`usecase` (Application Business Rules):** Define as interfaces (ports) de entrada e saída e os contratos dos casos de uso da aplicação.
- **`application` (Use Case Implementations):** Contém a implementação concreta dos casos de uso definidos no módulo `usecase`.
- **`infra` (Interface Adapters & Frameworks):** Camada mais externa que contém as implementações de infraestrutura, como adaptadores de banco de dados (Spring Data JPA), controladores REST, configurações do Spring Boot e integrações externas (Keycloak).

---

## 🚀 Execução totalmente via Docker

Para executar todo o ecossistema da aplicação usando Docker (aplicação, banco de dados e Keycloak), execute o seguinte comando na raiz do projeto:

```bash
  docker compose up --build --remove-orphans
```
### Este comando irá:

- Subir o back-end Java

- Provisionar o PostgreSQL com dados inicializados

- Iniciar o Keycloak com realm e clientes importados

## 🧩 Execução parcial via Docker

Caso prefira rodar apenas os serviços de infraestrutura via Docker:

Suba o banco de dados e Keycloak

```bash
  docker compose up postgres-fixit-backend  keycloak-fixit-backend --remove-orphans
```
Em outro terminal, se necessário, configure a versão do Java com sdkman:
```bash
  sdk env
```
Rode o backend
```bash
  mvn clean spring-boot:run
```

## 🧪 Testando os Endpoints com Postman

Para testar os endpoints da API, utilize o Postman importando a collection disponível no diretório:

> .postman/

Basta importar o arquivo .json no Postman e ajustar os endpoints conforme necessário

##  📖 Documentação da API (Swagger) 

Para acessar o Swagger da aplicação, acesse a url:

> http://localhost:8080/fixit-backend/swagger-ui/index.html

⚠️ Certifique-se de que a aplicação está rodando e que você está utilizando a porta e o contexto corretos (configurados no application.properties).

👥 Participantes do projeto

Este projeto foi desenvolvido pelos alunos da Pós-Graduação em Arquitetura de Software - FIAP:

 - Matheus Leal (rm368173)

 - Laura Alves (rm368613)

 - Marcio Souza (rm368671)

Feito com 💛 pela equipe FixIt!

