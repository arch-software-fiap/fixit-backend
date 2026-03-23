package com.fix_it.usecase.port;

import com.fix_it.usecase.gateway.output.GatewayAuthOutput;

public interface GatewayAuthPort {
    GatewayAuthOutput authenticate(String cpf, String dataNascimento);
}
