variable "aws_region" {
  description = "Região AWS"
  type        = string
  default     = "us-east-1"
}

variable "cluster_name" {
  description = "Nome do cluster EKS (para lookup dos nodes e VPC)"
  type        = string
}

variable "environment" {
  description = "Ambiente (dev, hmg, prod)"
  type        = string
}

variable "common_tags" {
  description = "Tags comuns a todos os recursos"
  type        = map(string)
  default = {
    Project   = "fixit"
    ManagedBy = "terraform"
  }
}
