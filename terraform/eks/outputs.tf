output "cluster_name" {
  description = "Nome do cluster EKS"
  value       = aws_eks_cluster.fixit.name
}

output "cluster_endpoint" {
  description = "Endpoint da API do cluster EKS"
  value       = aws_eks_cluster.fixit.endpoint
}

output "cluster_version" {
  description = "Versão do Kubernetes do cluster"
  value       = aws_eks_cluster.fixit.version
}

output "cluster_certificate_authority" {
  description = "Certificado CA do cluster EKS"
  value       = aws_eks_cluster.fixit.certificate_authority[0].data
  sensitive   = true
}

output "oidc_provider_arn" {
  description = "ARN do OIDC provider para IRSA"
  value       = aws_iam_openid_connect_provider.eks.arn
}

output "vpc_id" {
  description = "ID da VPC"
  value       = aws_vpc.fixit.id
}

output "public_subnet_ids" {
  description = "IDs das subnets públicas"
  value       = aws_subnet.public[*].id
}

output "private_subnet_ids" {
  description = "IDs das subnets privadas"
  value       = aws_subnet.private[*].id
}

output "node_group_arn" {
  description = "ARN do node group"
  value       = aws_eks_node_group.fixit.arn
}

output "kubeconfig_command" {
  description = "Comando para configurar o kubectl"
  value       = "aws eks update-kubeconfig --region ${var.aws_region} --name ${aws_eks_cluster.fixit.name}"
}