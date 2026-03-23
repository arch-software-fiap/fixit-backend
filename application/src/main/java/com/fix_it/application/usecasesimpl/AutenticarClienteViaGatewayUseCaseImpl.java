package com.fix_it.application.usecasesimpl;

import com.fix_it.usecase.gateway.AutenticarClienteViaGatewayUseCase;
import com.fix_it.usecase.gateway.output.GatewayAuthOutput;
import com.fix_it.usecase.port.GatewayAuthPort;

import java.util.Objects;

public class AutenticarClienteViaGatewayUseCaseImpl implements AutenticarClienteViaGatewayUseCase {

    private final GatewayAuthPort gatewayAuthPort;

    public AutenticarClienteViaGatewayUseCaseImpl(GatewayAuthPort gatewayAuthPort) {
        this.gatewayAuthPort = Objects.requireNonNull(gatewayAuthPort);
    }

    @Override
    public GatewayAuthOutput execute(String cpf) {
        return gatewayAuthPort.authenticate(cpf);
    }
}
