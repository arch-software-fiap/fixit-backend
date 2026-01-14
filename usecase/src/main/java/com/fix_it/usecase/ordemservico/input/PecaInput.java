package com.fix_it.usecase.ordemservico.input;

import java.util.UUID;

public record PecaInput(
    UUID id,
    Integer quantidade
) {}
