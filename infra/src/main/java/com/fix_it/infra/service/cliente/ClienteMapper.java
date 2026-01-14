package com.fix_it.infra.service.cliente;

import com.fix_it.core.domain.entity.Cliente;
import com.fix_it.infra.domain.ClienteEntity;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public ClienteEntity toEntity(Cliente domain) {
        if (domain == null) {
            return null;
        }

        ClienteEntity entity = new ClienteEntity();
        entity.setId(domain.getId());
        entity.setNome(domain.getNome());
        entity.setCpfCnpj(domain.getCpfCnpj());
        entity.setEmail(domain.getEmail());
        entity.setTelefone(domain.getTelefone());
        entity.setDtCadastro(domain.getDtCadastro());
        entity.setDthAtualizacao(domain.getDthAtualizacao());
        return entity;
    }

    public Cliente toDomain(ClienteEntity entity) {
        if (entity == null) {
            return null;
        }

        return Cliente.of(
                entity.getId(),
                entity.getNome(),
                entity.getCpfCnpj(),
                entity.getEmail(),
                entity.getTelefone(),
                entity.getDtCadastro(),
                entity.getDthAtualizacao()
        );
    }
}
