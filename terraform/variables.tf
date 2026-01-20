variable "cluster_name" {
  description = "Nome do cluster Kind"
  type        = string
  default     = "fixit-local"
}

variable "namespace" {
  description = "Namespace Kubernetes para a aplicação"
  type        = string
  default     = "fixit"
}

variable "app_image" {
  description = "Imagem Docker da aplicação"
  type        = string
  default     = "ghcr.io/arch-software-fiap/fixit-backend:latest"
}

variable "app_replicas" {
  description = "Número de réplicas da aplicação"
  type        = number
  default     = 1
}

variable "postgres_user" {
  description = "Usuário do PostgreSQL"
  type        = string
  default     = "fixitbackend"
}

variable "postgres_password" {
  description = "Senha do PostgreSQL"
  type        = string
  default     = "fixitbackend"
  sensitive   = true
}

variable "postgres_db" {
  description = "Nome do banco de dados PostgreSQL"
  type        = string
  default     = "fixitbackend"
}

variable "keycloak_admin_user" {
  description = "Usuário admin do Keycloak"
  type        = string
  default     = "admin"
}

variable "keycloak_admin_password" {
  description = "Senha admin do Keycloak"
  type        = string
  default     = "admin"
  sensitive   = true
}

variable "logging_level" {
  description = "Nível de log da aplicação"
  type        = string
  default     = "INFO"
}

variable "ghcr_username" {
  description = "Username do GitHub Container Registry"
  type        = string
  default     = ""
}

variable "ghcr_token" {
  description = "Token do GitHub Container Registry"
  type        = string
  default     = ""
  sensitive   = true
}
