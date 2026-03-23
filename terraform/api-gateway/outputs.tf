output "api_gateway_url" {
  description = "URL base do API Gateway"
  value       = "${aws_apigatewayv2_stage.fixit.invoke_url}"
}

output "api_backend_url" {
  description = "URL do fixit-backend via API Gateway"
  value       = "${aws_apigatewayv2_stage.fixit.invoke_url}/api"
}

output "api_keycloak_url" {
  description = "URL do Keycloak via API Gateway"
  value       = "${aws_apigatewayv2_stage.fixit.invoke_url}/auth"
}

output "api_grafana_url" {
  description = "URL do Grafana via API Gateway"
  value       = "${aws_apigatewayv2_stage.fixit.invoke_url}/grafana"
}

output "vpc_link_id" {
  description = "ID do VPC Link"
  value       = aws_apigatewayv2_vpc_link.fixit.id
}

output "nlb_dns" {
  description = "DNS interno do NLB"
  value       = aws_lb.fixit.dns_name
}
