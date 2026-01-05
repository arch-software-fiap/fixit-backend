package com.fix_it.app.common.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fix_it.app.model.Servico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ServicoDTO {

    private UUID id;

    @NotBlank
    @Size(max = 255)
    private String nome;

    @Size(max = 10_000)
    private String descricao;

    @PositiveOrZero
    private Long precoBase;

    private LocalDateTime atualizadoEm;

    public static ServicoDTO from(Servico s) {
        ServicoDTO dto = new ServicoDTO();
        dto.setId(s.getId());
        dto.setNome(s.getNmServico());
        dto.setDescricao(s.getDsServico());
        dto.setPrecoBase(s.getVlPrecoBase());
        dto.setAtualizadoEm(s.getDthAtualizacao());
        return dto;
    }

}
