package com.fix_it.usecase.gateway.output;

public record GatewayAuthOutput(
        int statusCode,
        boolean authenticated,
        String message,
        String token
) {
}
