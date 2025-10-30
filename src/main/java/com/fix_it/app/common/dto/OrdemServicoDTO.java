package com.fix_it.app.common.dto;

import java.util.UUID;

public record OrdemServicoDTO(UUID clienteId, UUID veiculoId, String descricao) {
}
