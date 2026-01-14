package com.fix_it.usecase.veiculo.input;

public record AtualizarVeiculoInput(
    String nmVeiculo,
    String dsVeiculo,
    String placa,
    String marca,
    String modelo,
    Integer ano
) {}
