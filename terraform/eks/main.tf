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

  backend "s3" {
    bucket         = "fixit-terraform-state-096626747607"
    key            = "fixit-eks/terraform.tfstate"
    region         = "us-east-1"
    dynamodb_table = "fixit-terraform-locks"
    encrypt        = true
  }
}

provider "aws" {
  region = var.aws_region
}