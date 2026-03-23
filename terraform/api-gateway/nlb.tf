# Security Group para o NLB (NLB não usa SG, mas precisamos para os nodes)
# Os nodes já têm SG do EKS, mas precisamos liberar as portas NodePort

# NLB interno (acesso apenas via VPC Link)
resource "aws_lb" "fixit" {
  name               = "${var.environment}-fixit-nlb"
  internal           = true
  load_balancer_type = "network"
  subnets            = data.aws_subnets.public.ids

  enable_cross_zone_load_balancing = true

  tags = merge(var.common_tags, {
    Name        = "${var.environment}-fixit-nlb"
    Environment = var.environment
  })
}

# Target Group - fixit-backend (NodePort 30080)
resource "aws_lb_target_group" "backend" {
  name        = "${var.environment}-fixit-backend-tg"
  port        = 30080
  protocol    = "TCP"
  vpc_id      = data.aws_vpc.fixit.id
  target_type = "instance"

  health_check {
    enabled             = true
    protocol            = "HTTP"
    path                = "/fixit-backend/actuator/health"
    port                = "30080"
    healthy_threshold   = 2
    unhealthy_threshold = 2
    interval            = 30
  }

  tags = merge(var.common_tags, {
    Name        = "${var.environment}-fixit-backend-tg"
    Environment = var.environment
  })
}

# Target Group - keycloak (NodePort 30085)
resource "aws_lb_target_group" "keycloak" {
  name        = "${var.environment}-fixit-keycloak-tg"
  port        = 30085
  protocol    = "TCP"
  vpc_id      = data.aws_vpc.fixit.id
  target_type = "instance"

  health_check {
    enabled             = true
    protocol            = "HTTP"
    path                = "/realms/fixit"
    port                = "30085"
    healthy_threshold   = 2
    unhealthy_threshold = 2
    interval            = 30
  }

  tags = merge(var.common_tags, {
    Name        = "${var.environment}-fixit-keycloak-tg"
    Environment = var.environment
  })
}

# Registrar nodes EKS como targets - backend
resource "aws_lb_target_group_attachment" "backend" {
  count = length(data.aws_instances.eks_nodes.ids)

  target_group_arn = aws_lb_target_group.backend.arn
  target_id        = data.aws_instances.eks_nodes.ids[count.index]
  port             = 30080
}

# Registrar nodes EKS como targets - keycloak
resource "aws_lb_target_group_attachment" "keycloak" {
  count = length(data.aws_instances.eks_nodes.ids)

  target_group_arn = aws_lb_target_group.keycloak.arn
  target_id        = data.aws_instances.eks_nodes.ids[count.index]
  port             = 30085
}

# Listener NLB - backend na porta 8080
resource "aws_lb_listener" "backend" {
  load_balancer_arn = aws_lb.fixit.arn
  port              = 8080
  protocol          = "TCP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.backend.arn
  }
}

# Listener NLB - keycloak na porta 8085
resource "aws_lb_listener" "keycloak" {
  load_balancer_arn = aws_lb.fixit.arn
  port              = 8085
  protocol          = "TCP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.keycloak.arn
  }
}

# Target Group - Grafana (NodePort 30300)
resource "aws_lb_target_group" "grafana" {
  name        = "${var.environment}-fixit-grafana-tg"
  port        = 30300
  protocol    = "TCP"
  vpc_id      = data.aws_vpc.fixit.id
  target_type = "instance"

  health_check {
    enabled             = true
    protocol            = "HTTP"
    path                = "/grafana/api/health"
    port                = "30300"
    healthy_threshold   = 2
    unhealthy_threshold = 2
    interval            = 30
  }

  tags = merge(var.common_tags, {
    Name        = "${var.environment}-fixit-grafana-tg"
    Environment = var.environment
  })
}

# Registrar nodes EKS como targets - grafana
resource "aws_lb_target_group_attachment" "grafana" {
  count = length(data.aws_instances.eks_nodes.ids)

  target_group_arn = aws_lb_target_group.grafana.arn
  target_id        = data.aws_instances.eks_nodes.ids[count.index]
  port             = 30300
}

# Listener NLB - grafana na porta 3000
resource "aws_lb_listener" "grafana" {
  load_balancer_arn = aws_lb.fixit.arn
  port              = 3000
  protocol          = "TCP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.grafana.arn
  }
}
