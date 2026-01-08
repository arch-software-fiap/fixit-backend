package com.fix_it.application.usecasesimpl;

import com.fix_it.usecase.port.VeiculoRepository;
import com.fix_it.usecase.veiculo.ListarVeiculosUseCase;
import com.fix_it.usecase.veiculo.output.VeiculoOutput;
import java.util.List;
import java.util.stream.Collectors;

public class ListarVeiculosUseCaseImpl implements ListarVeiculosUseCase {

    private final VeiculoRepository repository;

    public ListarVeiculosUseCaseImpl(VeiculoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<VeiculoOutput> executar() {
        return repository.listarTodos().stream()
            .map(v -> new VeiculoOutput(
                v.getId(),
                v.getNmVeiculo(),
                v.getDsVeiculo(),
                v.getPlaca(),
                v.getMarca(),
                v.getModelo(),
                v.getAno(),
                v.getDthCadastro(),
                v.getCliente().getId()
            ))
            .collect(Collectors.toList());
    }
}
