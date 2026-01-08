package com.fix_it.infra.service.veiculo;

import com.fix_it.core.domain.entity.Veiculo;
import com.fix_it.infra.service.cliente.ClienteMapper;
import com.fix_it.infra.persistence.entity.VeiculoEntity;
import org.springframework.stereotype.Component;

@Component
public class VeiculoMapper {

    private final ClienteMapper clienteMapper;

    public VeiculoMapper(ClienteMapper clienteMapper) {
        this.clienteMapper = clienteMapper;
    }

    public VeiculoEntity toEntity(Veiculo domain) {
        if (domain == null) return null;
        return new VeiculoEntity(
            domain.getId(),
            domain.getNmVeiculo(),
            domain.getDsVeiculo(),
            domain.getPlaca(),
            domain.getMarca(),
            domain.getModelo(),
            domain.getAno(),
            domain.getDthCadastro(),
            clienteMapper.toEntity(domain.getCliente())
        );
    }

    public Veiculo toDomain(VeiculoEntity entity) {
        if (entity == null) return null;
        return Veiculo.of(
            entity.getId(),
            entity.getNmVeiculo(),
            entity.getDsVeiculo(),
            entity.getPlaca(),
            entity.getMarca(),
            entity.getModelo(),
            entity.getAno(),
            entity.getDthCadastro(),
            clienteMapper.toDomain(entity.getCliente())
        );
    }
}
