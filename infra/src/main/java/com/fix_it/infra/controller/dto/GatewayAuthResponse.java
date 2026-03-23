package com.fix_it.infra.controller.dto;

public record GatewayAuthResponse(
        boolean authenticated,
        String message,
        String token
) {
}
