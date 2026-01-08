package com.fix_it.usecase.veiculo.output;

import java.time.LocalDateTime;
import java.util.UUID;

public record VeiculoOutput(
    UUID id,
    String nmVeiculo,
    String dsVeiculo,
    String placa,
    String marca,
    String modelo,
    Integer ano,
    LocalDateTime dthCadastro,
    UUID clienteId
) {}
