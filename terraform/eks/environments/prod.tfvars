aws_region         = "us-east-1"
cluster_name       = "fixit-eks-prod"
kubernetes_version = "1.31"
vpc_cidr           = "10.2.0.0/16"

node_instance_type = "t3.medium"
node_capacity_type = "ON_DEMAND"
node_desired_size  = 2
node_min_size      = 2
node_max_size      = 4

common_tags = {
  Project     = "fixit"
  Environment = "prod"
  ManagedBy   = "terraform"
}