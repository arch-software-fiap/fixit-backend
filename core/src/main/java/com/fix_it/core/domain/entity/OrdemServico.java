package com.fix_it.core.domain.entity;

import com.fix_it.core.domain.enums.SituacaoOrdemServico;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class OrdemServico {

    private final UUID id;
    private SituacaoOrdemServico situacao;
    private String descricao;
    private LocalDateTime dataAberturaEm;
    private LocalDateTime dataFechamentoEm;
    private Long valorOrcamentoTotal;
    private Long valorTotalFinal;
    private final Cliente cliente;
    private final Veiculo veiculo;

    private OrdemServico(UUID id, SituacaoOrdemServico situacao, String descricao, LocalDateTime dataAberturaEm, LocalDateTime dataFechamentoEm, Long valorOrcamentoTotal, Long valorTotalFinal, Cliente cliente, Veiculo veiculo) {
        this.id = id;
        this.situacao = situacao;
        this.descricao = descricao;
        this.dataAberturaEm = dataAberturaEm;
        this.dataFechamentoEm = dataFechamentoEm;
        this.valorOrcamentoTotal = valorOrcamentoTotal;
        this.valorTotalFinal = valorTotalFinal;
        this.cliente = cliente;
        this.veiculo = veiculo;
    }

    public static OrdemServico nova(String descricao, Cliente cliente, Veiculo veiculo) {
        return new OrdemServico(null, SituacaoOrdemServico.RECEBIDA, descricao, LocalDateTime.now(), null, 0L, 0L, cliente, veiculo);
    }

    public static OrdemServico of(UUID id, SituacaoOrdemServico situacao, String descricao, LocalDateTime dataAberturaEm, LocalDateTime dataFechamentoEm, Long valorOrcamentoTotal, Long valorTotalFinal, Cliente cliente, Veiculo veiculo) {
        return new OrdemServico(id, situacao, descricao, dataAberturaEm, dataFechamentoEm, valorOrcamentoTotal, valorTotalFinal, cliente, veiculo);
    }

    public void atualizarDescricao(String descricao) {
        if (this.situacao == SituacaoOrdemServico.FINALIZADA || this.situacao == SituacaoOrdemServico.ENTREGUE) {
            throw new IllegalArgumentException("OS finalizada/entregue não pode ser atualizada.");
        }
        this.descricao = descricao;
    }

    public void alterarSituacao(SituacaoOrdemServico novaSituacao) {
        this.situacao = novaSituacao;
        if (novaSituacao == SituacaoOrdemServico.FINALIZADA || novaSituacao == SituacaoOrdemServico.ENTREGUE) {
            this.dataFechamentoEm = LocalDateTime.now();
        }
    }

    public void atualizarValores(Long valorOrcamentoTotal, Long valorTotalFinal) {
        this.valorOrcamentoTotal = valorOrcamentoTotal;
        this.valorTotalFinal = valorTotalFinal;
    }

    public UUID getId() {
        return id;
    }

    public SituacaoOrdemServico getSituacao() {
        return situacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDateTime getDataAberturaEm() {
        return dataAberturaEm;
    }

    public LocalDateTime getDataFechamentoEm() {
        return dataFechamentoEm;
    }

    public Long getValorOrcamentoTotal() {
        return valorOrcamentoTotal;
    }

    public Long getValorTotalFinal() {
        return valorTotalFinal;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrdemServico that = (OrdemServico) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
