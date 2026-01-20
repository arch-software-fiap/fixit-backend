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
