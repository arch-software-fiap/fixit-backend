aws_region         = "us-east-1"
cluster_name       = "fixit-eks-dev"
kubernetes_version = "1.31"
vpc_cidr           = "10.0.0.0/16"

node_instance_type = "t3.small"
node_capacity_type = "SPOT"
node_desired_size  = 1
node_min_size      = 1
node_max_size      = 2

common_tags = {
  Project     = "fixit"
  Environment = "dev"
  ManagedBy   = "terraform"
}