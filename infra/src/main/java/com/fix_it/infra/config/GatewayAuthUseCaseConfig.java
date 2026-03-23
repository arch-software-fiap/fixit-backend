package com.fix_it.infra.config;

import com.fix_it.application.usecasesimpl.AutenticarClienteGatewayUseCaseImpl;
import com.fix_it.usecase.gateway.AutenticarClienteGatewayUseCase;
import com.fix_it.usecase.port.GatewayAuthPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayAuthUseCaseConfig {

    @Bean
    public AutenticarClienteGatewayUseCase autenticarClienteUseCase(GatewayAuthPort gatewayAuthPort) {
        return new AutenticarClienteGatewayUseCaseImpl(gatewayAuthPort);
    }
}
