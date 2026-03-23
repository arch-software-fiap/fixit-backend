package com.fix_it.usecase.gateway;

import com.fix_it.usecase.gateway.output.GatewayAuthOutput;

public interface AutenticarClienteViaGatewayUseCase {
    GatewayAuthOutput execute(String cpf);
}
