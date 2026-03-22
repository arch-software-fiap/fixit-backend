locals {
  azs             = slice(data.aws_availability_zones.available.names, 0, 3)
  public_subnets  = [for i in [1, 2, 3] : cidrsubnet(var.vpc_cidr, 8, i)]
  private_subnets = [for i in [10, 20, 30] : cidrsubnet(var.vpc_cidr, 8, i)]
}

data "aws_availability_zones" "available" {
  state = "available"
}

# VPC
resource "aws_vpc" "fixit" {
  cidr_block           = var.vpc_cidr
  enable_dns_support   = true
  enable_dns_hostnames = true

  tags = merge(var.common_tags, {
    Name = "${var.cluster_name}-vpc"
  })
}

# Internet Gateway
resource "aws_internet_gateway" "fixit" {
  vpc_id = aws_vpc.fixit.id

  tags = merge(var.common_tags, {
    Name = "${var.cluster_name}-igw"
  })
}

# Public Subnets
resource "aws_subnet" "public" {
  count = length(local.azs)

  vpc_id                  = aws_vpc.fixit.id
  cidr_block              = local.public_subnets[count.index]
  availability_zone       = local.azs[count.index]
  map_public_ip_on_launch = true

  tags = merge(var.common_tags, {
    Name                     = "${var.cluster_name}-public-${local.azs[count.index]}"
    "kubernetes.io/role/elb" = "1"
    "kubernetes.io/cluster/${var.cluster_name}" = "shared"
  })
}

# Private Subnets
resource "aws_subnet" "private" {
  count = length(local.azs)

  vpc_id            = aws_vpc.fixit.id
  cidr_block        = local.private_subnets[count.index]
  availability_zone = local.azs[count.index]

  tags = merge(var.common_tags, {
    Name                              = "${var.cluster_name}-private-${local.azs[count.index]}"
    "kubernetes.io/role/internal-elb" = "1"
    "kubernetes.io/cluster/${var.cluster_name}" = "shared"
  })
}

# Elastic IP para NAT Gateway
resource "aws_eip" "nat" {
  domain = "vpc"

  tags = merge(var.common_tags, {
    Name = "${var.cluster_name}-nat-eip"
  })

  depends_on = [aws_internet_gateway.fixit]
}

# NAT Gateway (único, na primeira subnet pública)
resource "aws_nat_gateway" "fixit" {
  allocation_id = aws_eip.nat.id
  subnet_id     = aws_subnet.public[0].id

  tags = merge(var.common_tags, {
    Name = "${var.cluster_name}-nat"
  })

  depends_on = [aws_internet_gateway.fixit]
}

# Route Table - Público
resource "aws_route_table" "public" {
  vpc_id = aws_vpc.fixit.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.fixit.id
  }

  tags = merge(var.common_tags, {
    Name = "${var.cluster_name}-public-rt"
  })
}

resource "aws_route_table_association" "public" {
  count = length(aws_subnet.public)

  subnet_id      = aws_subnet.public[count.index].id
  route_table_id = aws_route_table.public.id
}

# Route Table - Privado
resource "aws_route_table" "private" {
  vpc_id = aws_vpc.fixit.id

  route {
    cidr_block     = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.fixit.id
  }

  tags = merge(var.common_tags, {
    Name = "${var.cluster_name}-private-rt"
  })
}

resource "aws_route_table_association" "private" {
  count = length(aws_subnet.private)

  subnet_id      = aws_subnet.private[count.index].id
  route_table_id = aws_route_table.private.id
}