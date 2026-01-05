package com.fix_it.app.common.factory;

import com.fix_it.app.common.dto.ServicoDTO;
import com.fix_it.app.model.Servico;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ServicoFactory {

    /**
     * Cria entidade nova a partir do DTO.
     */
    public static Servico from(ServicoDTO dto) {
        Servico s = new Servico();
        from(dto, s);
        return s;
    }

    /**
     * Aplica atualizações do DTO sobre a entidade existente.
     */
    public static void updateEntity(ServicoDTO dto, Servico target) {
        from(dto, target);
    }

    private static void from(ServicoDTO dto, Servico s) {
        s.setNmServico(dto.getNome());
        s.setDsServico(dto.getDescricao());
        s.setVlPrecoBase(dto.getPrecoBase());
    }
}
