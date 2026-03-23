aws_region   = "us-east-1"
cluster_name = "production-fixit-eks"
environment  = "prod"

common_tags = {
  Project     = "fixit"
  Environment = "prod"
  ManagedBy   = "terraform"
}
