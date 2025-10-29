package com.fix_it.app.common.dto;

import com.fix_it.app.model.Veiculo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class VeiculoDTO {

    private UUID id;

    @NotBlank
    @Size(max = 255)
    private String nome;

    @NotBlank
    @Size(max = 10)
    private String placa;

    @Size(max = 100)
    private String marca;

    @Size(max = 100)
    private String modelo;

    private Integer ano;

    @Size(max = 10_000)
    private String descricao;

    @NotNull
    private UUID clienteId;

    private LocalDateTime cadastradoEm;

    public static VeiculoDTO from(Veiculo v) {
        VeiculoDTO dto = new VeiculoDTO();
        dto.setId(v.getId());
        dto.setNome(v.getNmVeiculo());
        dto.setPlaca(v.getPlaca());
        dto.setMarca(v.getMarca());
        dto.setModelo(v.getModelo());
        dto.setAno(v.getAno());
        dto.setDescricao(v.getDsVeiculo());
        dto.setClienteId(v.getCliente() != null ? v.getCliente().getId() : null);
        dto.setCadastradoEm(v.getDthCadastro());
        return dto;
    }

}
