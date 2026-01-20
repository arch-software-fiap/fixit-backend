# Terraform - Ambiente Local com Kind

Este diretório contém a configuração Terraform para executar o fixit-backend localmente usando Kind (Kubernetes in Docker).

## Pré-requisitos

- [Docker](https://docs.docker.com/get-docker/)
- [Kind](https://kind.sigs.k8s.io/docs/user/quick-start/#installation)
- [Terraform](https://developer.hashicorp.com/terraform/downloads) >= 1.0.0
- [kubectl](https://kubernetes.io/docs/tasks/tools/)

## Estrutura

```
terraform/
├── main.tf              # Cluster Kind e providers
├── variables.tf         # Variáveis de configuração
├── postgres.tf          # Deployment do PostgreSQL
├── keycloak.tf          # Deployment do Keycloak
├── application.tf       # Deployment da aplicação
├── ingress.tf           # NGINX Ingress Controller
├── outputs.tf           # Outputs do Terraform
└── terraform.tfvars.example  # Exemplo de variáveis
```

## Como usar

1. **Copie o arquivo de exemplo de variáveis:**

```bash
cp terraform.tfvars.example terraform.tfvars
```

2. **Edite o `terraform.tfvars` com suas configurações:**

```bash
# Especialmente as credenciais do GHCR se precisar baixar a imagem privada
ghcr_username = "seu-usuario-github"
ghcr_token    = "seu-token-github"
```

3. **Inicialize o Terraform:**

```bash
terraform init
```

4. **Visualize o plano de execução:**

```bash
terraform plan
```

5. **Aplique a configuração:**

```bash
terraform apply
```

6. **Aguarde a criação do cluster e dos recursos.**

## Acessando os serviços

Após a criação, os serviços estarão disponíveis em:

| Serviço      | URL                                  |
|--------------|--------------------------------------|
| Aplicação    | http://localhost:8080/fixit-backend  |
| Keycloak     | http://localhost:8085                |
| PostgreSQL   | localhost:5432                       |

## Comandos úteis

```bash
# Ver outputs
terraform output

# Ver kubeconfig
terraform output -raw kubeconfig > ~/.kube/config-fixit

# Usar kubectl com o cluster
export KUBECONFIG=~/.kube/config-fixit
kubectl get pods -n fixit

# Ver logs da aplicação
kubectl logs -f deployment/fixit-backend -n fixit

# Destruir o ambiente
terraform destroy
```

## Troubleshooting

### Imagem não baixa do GHCR

Verifique se o token do GitHub tem permissão `read:packages` e se as credenciais no `terraform.tfvars` estão corretas.

### Pods não iniciam

```bash
kubectl describe pod <nome-do-pod> -n fixit
kubectl logs <nome-do-pod> -n fixit
```

### Ingress não funciona

```bash
kubectl get pods -n ingress-nginx
kubectl logs -f deployment/ingress-nginx-controller -n ingress-nginx
```
