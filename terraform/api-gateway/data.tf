# Lookup da VPC do cluster EKS
data "aws_vpc" "fixit" {
  filter {
    name   = "tag:Name"
    values = ["${var.cluster_name}-vpc"]
  }
}

# Subnets públicas da VPC (onde o NLB ficará)
data "aws_subnets" "public" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.fixit.id]
  }
  filter {
    name   = "tag:kubernetes.io/role/elb"
    values = ["1"]
  }
}

# Cluster EKS (para obter o cluster_security_group_id correto)
data "aws_eks_cluster" "fixit" {
  name = var.cluster_name
}

# Security Group do cluster EKS (o que EKS auto-cria e anexa aos nodes)
data "aws_security_group" "eks_nodes" {
  id = data.aws_eks_cluster.fixit.vpc_config[0].cluster_security_group_id
}

# Nodes do EKS (instâncias EC2 do node group)
data "aws_instances" "eks_nodes" {
  filter {
    name   = "tag:kubernetes.io/cluster/${var.cluster_name}"
    values = ["owned"]
  }
  filter {
    name   = "instance-state-name"
    values = ["running"]
  }
}
