terraform {
  required_version = ">= 1.5.0"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
    tls = {
      source  = "hashicorp/tls"
      version = "~> 4.0"
    }
  }

  # Backend configurado dinamicamente via -backend-config no CI/CD
  # Ver .github/workflows/_terraform-eks-deploy.yml
  backend "s3" {}
}

provider "aws" {
  region = var.aws_region
}