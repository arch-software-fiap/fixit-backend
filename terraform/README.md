# Fluxo de Execução do Terraform

## 1. Instalar o Terraform CLI

Siga o guia oficial de instalação:
https://developer.hashicorp.com/terraform/tutorials/aws-get-started/install-cli

## 2. Configurar as Variáveis

```bash
cd terraform
cp terraform.tfvars.example terraform.tfvars
```

Edite o arquivo `terraform.tfvars` e preencha suas credenciais do GitHub:

```hcl
ghcr_username = "seu-usuario-github"
ghcr_token    = "seu-token-github"
```

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
