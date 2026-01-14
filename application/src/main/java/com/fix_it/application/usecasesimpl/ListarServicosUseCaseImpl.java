package com.fix_it.application.usecasesimpl;

import com.fix_it.usecase.port.ServicoRepository;
import com.fix_it.usecase.servico.ListarServicosUseCase;
import com.fix_it.usecase.servico.output.ServicoOutput;
import java.util.List;
import java.util.stream.Collectors;

public class ListarServicosUseCaseImpl implements ListarServicosUseCase {

    private final ServicoRepository repository;

    public ListarServicosUseCaseImpl(ServicoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ServicoOutput> executar() {
        return repository.listarTodos().stream()
            .map(s -> new ServicoOutput(
                s.getId(),
                s.getNome(),
                s.getDescricao(),
                s.getValorPrecoBase(),
                s.getDthAtualizacao()
            ))
            .collect(Collectors.toList());
    }
}
