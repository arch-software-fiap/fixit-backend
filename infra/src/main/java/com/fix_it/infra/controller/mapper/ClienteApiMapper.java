package com.fix_it.infra.controller.mapper;

import com.fix_it.core.domain.entity.Cliente;
import com.fix_it.infra.controller.dto.ClienteResponse;
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
                cliente.getDtNascimento(),
                cliente.getDtCadastro(),
                cliente.getDthAtualizacao()
        );
    }
}
