package com.fix_it.usecase.gateway;

import com.fix_it.usecase.gateway.output.GatewayAuthOutput;

public interface AutenticarClienteGatewayUseCase {
    GatewayAuthOutput execute(String cpf, String dataNascimento);
}
