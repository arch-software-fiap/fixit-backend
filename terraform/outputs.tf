output "cluster_name" {
  description = "Nome do cluster Kind"
  value       = kind_cluster.fixit.name
}

output "cluster_endpoint" {
  description = "Endpoint do cluster Kubernetes"
  value       = kind_cluster.fixit.endpoint
}

output "kubeconfig" {
  description = "Kubeconfig para acessar o cluster"
  value       = kind_cluster.fixit.kubeconfig
  sensitive   = true
}

output "app_url" {
  description = "URL da aplicação"
  value       = "http://localhost:8080/fixit-backend"
}

output "keycloak_url" {
  description = "URL do Keycloak"
  value       = "http://localhost:8085"
}

output "postgres_connection" {
  description = "String de conexão do PostgreSQL"
  value       = "postgresql://${var.postgres_user}:****@localhost:5432/${var.postgres_db}"
}