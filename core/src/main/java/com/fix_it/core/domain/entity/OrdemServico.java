package com.fix_it.core.domain.entity;

import com.fix_it.core.domain.enums.SituacaoOrdemServico;

import java.time.LocalDateTime;
import java.util.UUID;

public class OrdemServico {

    private UUID id;

    private SituacaoOrdemServico situacao;

    private String descricao;

    private LocalDateTime dataAberturaEm;

    private LocalDateTime dataFechamentoEm;

    private Long valorOrcamentoTotal;

    private Long valorTotalFinal;

    private Cliente cliente;

    private Veiculo veiculo;

}
