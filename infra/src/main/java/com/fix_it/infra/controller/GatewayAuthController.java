package com.fix_it.infra.controller;

import com.fix_it.infra.controller.dto.GatewayAuthRequest;
import com.fix_it.infra.controller.dto.GatewayAuthResponse;
import com.fix_it.usecase.gateway.AutenticarClienteViaGatewayUseCase;
import com.fix_it.usecase.gateway.output.GatewayAuthOutput;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public/auth")
@RequiredArgsConstructor
public class GatewayAuthController {

    private final AutenticarClienteViaGatewayUseCase autenticarClienteViaGatewayUseCase;

    @PostMapping("/token")
    public ResponseEntity<GatewayAuthResponse> autenticar(@RequestBody @Valid GatewayAuthRequest request) {
        GatewayAuthOutput output = autenticarClienteViaGatewayUseCase.execute(request.cpf());
        return ResponseEntity.status(output.statusCode()).body(new GatewayAuthResponse(
                output.authenticated(),
                output.message(),
                output.token()
        ));
    }
}
