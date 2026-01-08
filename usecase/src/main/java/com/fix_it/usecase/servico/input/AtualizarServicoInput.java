package com.fix_it.usecase.servico.input;

public record AtualizarServicoInput(
    String nmServico,
    String dsServico,
    Long vlPrecoBase
) {}
