package com.fix_it.usecase.ordemservico.input;

import java.util.UUID;

public record ServicoInput(
    UUID id,
    Integer quantidade
) {}
