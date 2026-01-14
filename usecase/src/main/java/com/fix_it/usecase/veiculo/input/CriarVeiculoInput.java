package com.fix_it.usecase.veiculo.input;

import java.util.UUID;

public record CriarVeiculoInput(
    String nmVeiculo,
    String dsVeiculo,
    String placa,
    String marca,
    String modelo,
    Integer ano,
    UUID clienteId
) {}
