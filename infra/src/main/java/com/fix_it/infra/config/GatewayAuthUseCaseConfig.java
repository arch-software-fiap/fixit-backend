package com.fix_it.infra.config;

import com.fix_it.application.usecasesimpl.AutenticarClienteViaGatewayUseCaseImpl;
import com.fix_it.usecase.gateway.AutenticarClienteViaGatewayUseCase;
import com.fix_it.usecase.port.GatewayAuthPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayAuthUseCaseConfig {

    @Bean
    public AutenticarClienteViaGatewayUseCase autenticarClienteViaGatewayUseCase(GatewayAuthPort gatewayAuthPort) {
        return new AutenticarClienteViaGatewayUseCaseImpl(gatewayAuthPort);
    }
}
