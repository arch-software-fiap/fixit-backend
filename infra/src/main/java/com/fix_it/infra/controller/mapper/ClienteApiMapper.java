package com.fix_it.infra.controller.mapper;

import com.fix_it.infra.controller.dto.ClienteResponse;
import com.fix_it.core.domain.entity.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteApiMapper {

    public ClienteResponse toResponse(Cliente cliente) {
        if (cliente == null) return null;

        return new ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpfCnpj(),
                cliente.getEmail(),
                cliente.getTelefone(),
                cliente.getDtCadastro(),
                cliente.getDthAtualizacao()
        );
    }
}
