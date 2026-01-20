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

### Provisionamento da Infraestrutura com Terraform


### Deploy em Kubernetes


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
