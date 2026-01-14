package com.fix_it.infra.service.veiculo;

import com.fix_it.application.usecasesimpl.*;
import com.fix_it.usecase.port.ClienteRepository;
import com.fix_it.usecase.port.VeiculoRepository;
import com.fix_it.usecase.veiculo.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VeiculoUseCaseConfig {

    @Bean
    public CriarVeiculoUseCase criarVeiculoUseCase(VeiculoRepository veiculoRepository, ClienteRepository clienteRepository) {
        return new CriarVeiculoUseCaseImpl(veiculoRepository, clienteRepository);
    }

    @Bean
    public AtualizarVeiculoUseCase atualizarVeiculoUseCase(VeiculoRepository repository) {
        return new AtualizarVeiculoUseCaseImpl(repository);
    }

    @Bean
    public BuscarVeiculoPorIdUseCase buscarVeiculoPorIdUseCase(VeiculoRepository repository) {
        return new BuscarVeiculoPorIdUseCaseImpl(repository);
    }

    @Bean
    public ListarVeiculosUseCase listarVeiculosUseCase(VeiculoRepository repository) {
        return new ListarVeiculosUseCaseImpl(repository);
    }

    @Bean
    public RemoverVeiculoUseCase removerVeiculoUseCase(VeiculoRepository repository) {
        return new RemoverVeiculoUseCaseImpl(repository);
    }
}
