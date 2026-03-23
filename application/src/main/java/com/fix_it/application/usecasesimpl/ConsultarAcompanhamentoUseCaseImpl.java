package com.fix_it.application.usecasesimpl;

import com.fix_it.core.domain.entity.OrdemServico;
import com.fix_it.usecase.ordemservico.ConsultarAcompanhamentoUseCase;
import com.fix_it.usecase.ordemservico.output.AcompanhamentoOSOutput;
import com.fix_it.usecase.port.OrdemServicoRepository;

import java.util.UUID;

public class ConsultarAcompanhamentoUseCaseImpl implements ConsultarAcompanhamentoUseCase {

    private final OrdemServicoRepository repository;

    public ConsultarAcompanhamentoUseCaseImpl(OrdemServicoRepository repository) {
        this.repository = repository;
    }

    @Override
    public AcompanhamentoOSOutput executar(UUID osId, String cpfCnpjClienteAutenticado) {
        OrdemServico os = repository.buscarPorId(osId)
            .orElseThrow(() -> new IllegalArgumentException("OS não encontrada"));

        if (!os.getCliente().getCpfCnpj().equals(cpfCnpjClienteAutenticado)) {
            throw new IllegalArgumentException("Cliente autenticado não possui acesso a esta OS");
        }

        return new AcompanhamentoOSOutput(
            os.getId(),
            os.getCliente().getNome(),
            os.getCliente().getCpfCnpj(),
            os.getVeiculo().getPlaca(),
            os.getSituacao(),
            os.getValorOrcamentoTotal(),
            os.getValorTotalFinal(),
            os.getDataAberturaEm(),
            os.getDataFechamentoEm()
        );
    }
}
