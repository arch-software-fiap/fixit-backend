package com.fix_it.app.common.factory;

import com.fix_it.app.common.dto.VeiculoDTO;
import com.fix_it.app.model.Cliente;
import com.fix_it.app.model.Veiculo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VeiculoFactory {

    /**
     * Cria entidade nova a partir do DTO (regra de normalização aplicada).
     */
    public static Veiculo from(VeiculoDTO dto, Cliente cliente) {
        Veiculo v = new Veiculo();
        from(dto, v, cliente);
        return v;
    }

    /**
     * Aplica atualizações do DTO sobre a entidade existente.
     */
    public static void updateEntity(VeiculoDTO dto, Veiculo target, Cliente cliente) {
        from(dto, target, cliente);
    }

    private static void from(VeiculoDTO dto, Veiculo v, Cliente cliente) {
        v.setNmVeiculo(dto.getNome());
        v.setDsVeiculo(dto.getDescricao());
        v.setPlaca(normalizePlaca(dto.getPlaca()));
        v.setMarca(dto.getMarca());
        v.setModelo(dto.getModelo());
        v.setAno(dto.getAno());
        v.setCliente(cliente);
    }

    private static String normalizePlaca(String placa) {
        return placa == null ? null : placa.replaceAll("\\s+", "").toUpperCase();
    }
}
