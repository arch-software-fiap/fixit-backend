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
# KC_HTTP_RELATIVE_PATH=/${var.environment} faz o Keycloak servir tudo sob /${env}/.
# O API GW stripa o stage no recebimento — a integration recompõe o prefixo.
resource "aws_apigatewayv2_integration" "keycloak" {
  api_id             = aws_apigatewayv2_api.fixit.id
  integration_type   = "HTTP_PROXY"
  integration_method = "ANY"
  integration_uri    = aws_lb_listener.keycloak.arn

  connection_type = "VPC_LINK"
  connection_id   = aws_apigatewayv2_vpc_link.fixit.id

  request_parameters = {
    # /auth/realms/... → /${env}/realms/... (reconstrói prefixo do relative path)
    "overwrite:path" = "/${var.environment}/$request.path.proxy"
  }
}

# Integração dedicada para /admin/{proxy+}
# proxy captura apenas o que vem APÓS /admin/ — recompõe /${env}/admin/$proxy
resource "aws_apigatewayv2_integration" "keycloak_admin" {
  api_id             = aws_apigatewayv2_api.fixit.id
  integration_type   = "HTTP_PROXY"
  integration_method = "ANY"
  integration_uri    = aws_lb_listener.keycloak.arn

  connection_type = "VPC_LINK"
  connection_id   = aws_apigatewayv2_vpc_link.fixit.id

  request_parameters = {
    "overwrite:path" = "/${var.environment}/admin/$request.path.proxy"
  }
}

# Integração dedicada para /resources/{proxy+}
# O admin console do Keycloak 23+ gera <base href="/${env}/resources/..."> root-relative.
# O browser resolve sem stage → /<api-gw>/resources/... → API GW roteia aqui e
# recompõe o path completo que o Keycloak com KC_HTTP_RELATIVE_PATH espera.
resource "aws_apigatewayv2_integration" "keycloak_resources" {
  api_id             = aws_apigatewayv2_api.fixit.id
  integration_type   = "HTTP_PROXY"
  integration_method = "ANY"
  integration_uri    = aws_lb_listener.keycloak.arn

  connection_type = "VPC_LINK"
  connection_id   = aws_apigatewayv2_vpc_link.fixit.id

  request_parameters = {
    "overwrite:path" = "/${var.environment}/resources/$request.path.proxy"
  }
}

# Rota: ANY /api/{proxy+} → fixit-backend
resource "aws_apigatewayv2_route" "backend" {
  api_id    = aws_apigatewayv2_api.fixit.id
  route_key = "ANY /api/{proxy+}"
  target    = "integrations/${aws_apigatewayv2_integration.backend.id}"
}

# Rota: ANY /auth/{proxy+} → keycloak (endpoints OIDC/realms do frontend)
resource "aws_apigatewayv2_route" "keycloak" {
  api_id    = aws_apigatewayv2_api.fixit.id
  route_key = "ANY /auth/{proxy+}"
  target    = "integrations/${aws_apigatewayv2_integration.keycloak.id}"
}

# Rota: ANY /admin/{proxy+} → keycloak (console admin do Keycloak 23+)
resource "aws_apigatewayv2_route" "keycloak_admin" {
  api_id    = aws_apigatewayv2_api.fixit.id
  route_key = "ANY /admin/{proxy+}"
  target    = "integrations/${aws_apigatewayv2_integration.keycloak_admin.id}"
}

# Rota: ANY /resources/{proxy+} → keycloak (assets estáticos do admin console)
resource "aws_apigatewayv2_route" "keycloak_resources" {
  api_id    = aws_apigatewayv2_api.fixit.id
  route_key = "ANY /resources/{proxy+}"
  target    = "integrations/${aws_apigatewayv2_integration.keycloak_resources.id}"
}

# Integração para /realms/{proxy+} — URLs diretas geradas pelo Keycloak via KC_HOSTNAME_URL
# Keycloak com KC_HOSTNAME_URL=https://<gw>/<env> gera links sem prefixo /auth/:
# ex: https://<gw>/<env>/realms/master/protocol/openid-connect/3p-cookies/step1.html
resource "aws_apigatewayv2_integration" "keycloak_realms" {
  api_id             = aws_apigatewayv2_api.fixit.id
  integration_type   = "HTTP_PROXY"
  integration_method = "ANY"
  integration_uri    = aws_lb_listener.keycloak.arn

  connection_type = "VPC_LINK"
  connection_id   = aws_apigatewayv2_vpc_link.fixit.id

  request_parameters = {
    "overwrite:path" = "/${var.environment}/realms/$request.path.proxy"
  }
}

# Rota: ANY /realms/{proxy+} → keycloak
resource "aws_apigatewayv2_route" "keycloak_realms" {
  api_id    = aws_apigatewayv2_api.fixit.id
  route_key = "ANY /realms/{proxy+}"
  target    = "integrations/${aws_apigatewayv2_integration.keycloak_realms.id}"
}

# Integração com Grafana via VPC Link → NLB porta 3000
# O API GW stripa o prefixo de stage (/dev/, /hmg/) antes de rotear.
# Grafana com serve_from_sub_path=true precisa receber o path COMPLETO incluindo stage.
# Solução: reconstruir o path completo com var.environment no overwrite:path.
resource "aws_apigatewayv2_integration" "grafana" {
  api_id             = aws_apigatewayv2_api.fixit.id
  integration_type   = "HTTP_PROXY"
  integration_method = "ANY"
  integration_uri    = aws_lb_listener.grafana.arn

  connection_type = "VPC_LINK"
  connection_id   = aws_apigatewayv2_vpc_link.fixit.id

  request_parameters = {
    # Reconstrói path completo: /grafana/login → /{env}/grafana/login
    # Grafana recebe o path que o root_url espera (/{env}/grafana/)
    "overwrite:path" = "/${var.environment}/grafana/$request.path.proxy"
  }
}

# Integração para raiz /grafana (sem proxy variable)
resource "aws_apigatewayv2_integration" "grafana_root" {
  api_id             = aws_apigatewayv2_api.fixit.id
  integration_type   = "HTTP_PROXY"
  integration_method = "ANY"
  integration_uri    = aws_lb_listener.grafana.arn

  connection_type = "VPC_LINK"
  connection_id   = aws_apigatewayv2_vpc_link.fixit.id

  request_parameters = {
    "overwrite:path" = "/${var.environment}/grafana/"
  }
}

# Rota: ANY /grafana/{proxy+} → Grafana (paths com conteúdo)
resource "aws_apigatewayv2_route" "grafana" {
  api_id    = aws_apigatewayv2_api.fixit.id
  route_key = "ANY /grafana/{proxy+}"
  target    = "integrations/${aws_apigatewayv2_integration.grafana.id}"
}

# Rota: ANY /grafana → Grafana (raiz, redireciona para /grafana/)
resource "aws_apigatewayv2_route" "grafana_root" {
  api_id    = aws_apigatewayv2_api.fixit.id
  route_key = "ANY /grafana"
  target    = "integrations/${aws_apigatewayv2_integration.grafana_root.id}"
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
