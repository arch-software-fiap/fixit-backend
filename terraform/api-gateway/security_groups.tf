# Security Group para o VPC Link
resource "aws_security_group" "vpc_link" {
  name        = "${var.environment}-fixit-vpc-link-sg"
  description = "Security group do VPC Link do API Gateway"
  vpc_id      = data.aws_vpc.fixit.id

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
    description = "Allow all outbound to NLB"
  }

  tags = merge(var.common_tags, {
    Name        = "${var.environment}-fixit-vpc-link-sg"
    Environment = var.environment
  })
}

# Libera NodePort do backend nos nodes EKS via NLB
resource "aws_vpc_security_group_ingress_rule" "nodes_backend_nodeport" {
  security_group_id = data.aws_security_group.eks_nodes.id
  ip_protocol       = "tcp"
  from_port         = 30080
  to_port           = 30080
  cidr_ipv4         = data.aws_vpc.fixit.cidr_block
  description       = "NLB to fixit-backend NodePort 30080"
}

# Libera NodePort do keycloak nos nodes EKS via NLB
resource "aws_vpc_security_group_ingress_rule" "nodes_keycloak_nodeport" {
  security_group_id = data.aws_security_group.eks_nodes.id
  ip_protocol       = "tcp"
  from_port         = 30085
  to_port           = 30085
  cidr_ipv4         = data.aws_vpc.fixit.cidr_block
  description       = "NLB to keycloak NodePort 30085"
}
