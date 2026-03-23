package com.fix_it.application.usecasesimpl;

import com.fix_it.usecase.gateway.AutenticarClienteGatewayUseCase;
import com.fix_it.usecase.gateway.output.GatewayAuthOutput;
import com.fix_it.usecase.port.GatewayAuthPort;

import java.util.Objects;

public class AutenticarClienteGatewayUseCaseImpl implements AutenticarClienteGatewayUseCase {

    private final GatewayAuthPort gatewayAuthPort;

    public AutenticarClienteGatewayUseCaseImpl(GatewayAuthPort gatewayAuthPort) {
        this.gatewayAuthPort = Objects.requireNonNull(gatewayAuthPort);
    }

    @Override
    public GatewayAuthOutput execute(String cpf, String dataNascimento) {
        return gatewayAuthPort.authenticate(cpf, dataNascimento);
    }
}
