aws_region   = "us-east-1"
cluster_name = "development-fixit-eks"
environment  = "dev"

common_tags = {
  Project     = "fixit"
  Environment = "dev"
  ManagedBy   = "terraform"
}
