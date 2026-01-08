package com.fix_it.usecase.servico.output;

import java.time.LocalDateTime;
import java.util.UUID;

public record ServicoOutput(
    UUID id,
    String nmServico,
    String dsServico,
    Long vlPrecoBase,
    LocalDateTime dthAtualizacao
) {}
