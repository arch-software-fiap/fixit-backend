package com.fix_it.config;

import com.fix_it.application.usecasesimpl.*;
import com.fix_it.usecase.port.ServicoRepository;
import com.fix_it.usecase.servico.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicoUseCaseConfig {

    @Bean
    public CriarServicoUseCase criarServicoUseCase(ServicoRepository repository) {
        return new CriarServicoUseCaseImpl(repository);
    }

    @Bean
    public AtualizarServicoUseCase atualizarServicoUseCase(ServicoRepository repository) {
        return new AtualizarServicoUseCaseImpl(repository);
    }

    @Bean
    public BuscarServicoPorIdUseCase buscarServicoPorIdUseCase(ServicoRepository repository) {
        return new BuscarServicoPorIdUseCaseImpl(repository);
    }

    @Bean
    public ListarServicosUseCase listarServicosUseCase(ServicoRepository repository) {
        return new ListarServicosUseCaseImpl(repository);
    }

    @Bean
    public RemoverServicoUseCase removerServicoUseCase(ServicoRepository repository) {
        return new RemoverServicoUseCaseImpl(repository);
    }
}
