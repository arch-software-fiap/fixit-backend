# VPC Link para conectar API Gateway ao NLB interno
resource "aws_apigatewayv2_vpc_link" "fixit" {
  name               = "${var.environment}-fixit-vpc-link"
  security_group_ids = [aws_security_group.vpc_link.id]
  subnet_ids         = data.aws_subnets.public.ids

  tags = merge(var.common_tags, {
    Name        = "${var.environment}-fixit-vpc-link"
    Environment = var.environment
  })
}

# HTTP API Gateway
resource "aws_apigatewayv2_api" "fixit" {
  name          = "${var.environment}-fixit-api"
  protocol_type = "HTTP"

  cors_configuration {
    allow_origins = ["*"]
    allow_methods = ["GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"]
    allow_headers = ["*"]
    max_age       = 300
  }

  tags = merge(var.common_tags, {
    Name        = "${var.environment}-fixit-api"
    Environment = var.environment
  })
}

# Integração com fixit-backend via VPC Link → NLB porta 8080
resource "aws_apigatewayv2_integration" "backend" {
  api_id             = aws_apigatewayv2_api.fixit.id
  integration_type   = "HTTP_PROXY"
  integration_method = "ANY"
  integration_uri    = aws_lb_listener.backend.arn

  connection_type = "VPC_LINK"
  connection_id   = aws_apigatewayv2_vpc_link.fixit.id

  request_parameters = {
    # context-path=/fixit-backend + mantém /api/ no path
    "overwrite:path" = "/fixit-backend/api/$request.path.proxy"
  }
}

# Integração com keycloak via VPC Link → NLB porta 8085
resource "aws_apigatewayv2_integration" "keycloak" {
  api_id             = aws_apigatewayv2_api.fixit.id
  integration_type   = "HTTP_PROXY"
  integration_method = "ANY"
  integration_uri    = aws_lb_listener.keycloak.arn

  connection_type = "VPC_LINK"
  connection_id   = aws_apigatewayv2_vpc_link.fixit.id

  request_parameters = {
    # Keycloak 23+ não usa /auth prefix
    "overwrite:path" = "/$request.path.proxy"
  }
}

# Rota: ANY /api/{proxy+} → fixit-backend
resource "aws_apigatewayv2_route" "backend" {
  api_id    = aws_apigatewayv2_api.fixit.id
  route_key = "ANY /api/{proxy+}"
  target    = "integrations/${aws_apigatewayv2_integration.backend.id}"
}

# Rota: ANY /auth/{proxy+} → keycloak
resource "aws_apigatewayv2_route" "keycloak" {
  api_id    = aws_apigatewayv2_api.fixit.id
  route_key = "ANY /auth/{proxy+}"
  target    = "integrations/${aws_apigatewayv2_integration.keycloak.id}"
}

# Stage de deploy (auto-deploy ativo)
resource "aws_apigatewayv2_stage" "fixit" {
  api_id      = aws_apigatewayv2_api.fixit.id
  name        = var.environment
  auto_deploy = true

  access_log_settings {
    destination_arn = aws_cloudwatch_log_group.api_gateway.arn
    format          = "$context.requestId $context.status $context.httpMethod $context.path $context.responseLength"
  }

  tags = merge(var.common_tags, {
    Name        = "${var.environment}-fixit-stage"
    Environment = var.environment
  })
}

# CloudWatch Log Group para logs do API Gateway
resource "aws_cloudwatch_log_group" "api_gateway" {
  name              = "/aws/apigateway/${var.environment}-fixit-api"
  retention_in_days = 7

  tags = merge(var.common_tags, {
    Environment = var.environment
  })
}
