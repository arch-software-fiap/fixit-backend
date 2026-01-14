package com.fix_it.application.usecasesimpl;

import com.fix_it.usecase.ordemservico.ListarOrdensServicoUseCase;
import com.fix_it.usecase.ordemservico.output.OrdemServicoOutput;
import com.fix_it.usecase.port.OrdemServicoRepository;
import java.util.List;
import java.util.stream.Collectors;

public class ListarOrdensServicoUseCaseImpl implements ListarOrdensServicoUseCase {

    private final OrdemServicoRepository osRepository;

    public ListarOrdensServicoUseCaseImpl(OrdemServicoRepository osRepository) {
        this.osRepository = osRepository;
    }

    @Override
    public List<OrdemServicoOutput> executar() {
        return osRepository.listarParaAcompanhamento().stream()
            .map(os -> new OrdemServicoOutput(
                os.getId(),
                os.getSituacao(),
                os.getDescricao(),
                os.getDataAberturaEm(),
                os.getDataFechamentoEm(),
                os.getValorOrcamentoTotal(),
                os.getValorTotalFinal(),
                os.getCliente().getId(),
                os.getVeiculo().getId()
            ))
            .collect(Collectors.toList());
    }
}
