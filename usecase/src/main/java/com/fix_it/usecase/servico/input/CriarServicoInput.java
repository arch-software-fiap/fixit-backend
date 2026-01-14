package com.fix_it.usecase.servico.input;

public record CriarServicoInput(
    String nmServico,
    String dsServico,
    Long vlPrecoBase
) {}
