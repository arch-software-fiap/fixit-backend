package com.fix_it.infra.service.ordemservico;

import com.fix_it.core.domain.entity.OrdemServico;
import com.fix_it.infra.service.cliente.ClienteMapper;
import com.fix_it.infra.service.veiculo.VeiculoMapper;
import com.fix_it.infra.persistence.entity.OrdemServicoEntity;
import org.springframework.stereotype.Component;

@Component
public class OrdemServicoMapper {

    private final ClienteMapper clienteMapper;
    private final VeiculoMapper veiculoMapper;

    public OrdemServicoMapper(ClienteMapper clienteMapper, VeiculoMapper veiculoMapper) {
        this.clienteMapper = clienteMapper;
        this.veiculoMapper = veiculoMapper;
    }

    public OrdemServicoEntity toEntity(OrdemServico domain) {
        if (domain == null) return null;
        return new OrdemServicoEntity(
            domain.getId(),
            domain.getSituacao(),
            domain.getDescricao(),
            domain.getDataAberturaEm(),
            domain.getDataFechamentoEm(),
            domain.getValorOrcamentoTotal(),
            domain.getValorTotalFinal(),
            clienteMapper.toEntity(domain.getCliente()),
            veiculoMapper.toEntity(domain.getVeiculo())
        );
    }

    public OrdemServico toDomain(OrdemServicoEntity entity) {
        if (entity == null) return null;
        return OrdemServico.of(
            entity.getId(),
            entity.getSituacao(),
            entity.getDescricao(),
            entity.getDataAberturaEm(),
            entity.getDataFechamentoEm(),
            entity.getValorOrcamentoTotal(),
            entity.getValorTotalFinal(),
            clienteMapper.toDomain(entity.getCliente()),
            veiculoMapper.toDomain(entity.getVeiculo())
        );
    }
}
