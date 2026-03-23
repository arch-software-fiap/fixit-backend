package com.fix_it.infra.service.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fix_it.infra.controller.dto.GatewayAuthRequest;
import com.fix_it.usecase.gateway.output.GatewayAuthOutput;
import com.fix_it.usecase.port.GatewayAuthPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class GatewayAuthAdapter implements GatewayAuthPort {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${integration.fixit.gateway.lambda-url:}")
    private String gatewayLambdaUrl;

    @Override
    public GatewayAuthOutput authenticate(String cpf) {
        if (!StringUtils.hasText(gatewayLambdaUrl)) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "URL do API Gateway /lambda não configurada");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GatewayAuthRequest> request = new HttpEntity<>(new GatewayAuthRequest(cpf), headers);

        try {
            ResponseEntity<GatewayAuthPayload> response = restTemplate.postForEntity(
                    gatewayLambdaUrl,
                    request,
                    GatewayAuthPayload.class
            );
            return toOutput(response.getStatusCode().value(), response.getBody());
        } catch (HttpStatusCodeException exception) {
            return toOutput(exception.getStatusCode().value(), parseBody(exception.getResponseBodyAsString()));
        } catch (ResourceAccessException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Erro ao acessar o API Gateway /lambda", exception);
        }
    }

    private GatewayAuthPayload parseBody(String body) {
        if (!StringUtils.hasText(body)) {
            return new GatewayAuthPayload(false, "Resposta vazia do API Gateway /lambda", null);
        }

        try {
            return objectMapper.readValue(body, GatewayAuthPayload.class);
        } catch (Exception exception) {
            return new GatewayAuthPayload(false, "Resposta inválida do API Gateway /lambda", null);
        }
    }

    private GatewayAuthOutput toOutput(int statusCode, GatewayAuthPayload payload) {
        GatewayAuthPayload safePayload = payload == null
                ? new GatewayAuthPayload(false, "Resposta vazia do API Gateway /lambda", null)
                : payload;

        return new GatewayAuthOutput(
                statusCode,
                safePayload.authenticated(),
                safePayload.message(),
                safePayload.token()
        );
    }

    private record GatewayAuthPayload(
            boolean authenticated,
            String message,
            String token
    ) {
    }
}
