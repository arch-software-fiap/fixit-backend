package com.fix_it.infra.config;

import com.fix_it.application.usecasesimpl.*;
import com.fix_it.infra.service.cliente.DocumentoValidatorImpl;
import com.fix_it.usecase.cliente.*;
import com.fix_it.usecase.port.ClienteRepository;
import com.fix_it.usecase.port.DocumentoValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClienteUseCaseConfig {

    @Bean
    public ListarClientesUseCase listarClientesUseCase(ClienteRepository repo) {
        return new ListarClientesUseCaseImpl(repo);
    }

    @Bean
    public CriarClienteUseCase criarClienteUseCase(ClienteRepository repo, DocumentoValidator documentoValidator) {
        return new CriarClienteUseCaseImpl(repo, documentoValidator);
    }

    @Bean
    AtualizarClienteUseCase atualizarClienteUseCase(ClienteRepository repo, DocumentoValidator documentoValidator) {
        return new AtualizarClienteUseCaseImpl(repo, documentoValidator);
    }

    @Bean
    public BuscarClientePorIdUseCase buscarClientePorIdUseCase(ClienteRepository repo) {
        return new BuscarClientePorIdUseCaseImpl(repo);
    }

    @Bean
    public BuscarClientePorDocumentoUseCase buscarClientePorDocumentoUseCase(ClienteRepository repo) {
        return new BuscarClientePorDocumentoUseCaseImpl(repo);
    }

    @Bean
    public RemoverClienteUseCase removerClienteUseCase(ClienteRepository repo) {
        return new RemoverClienteUseCaseImpl(repo);
    }

    @Bean
    public DocumentoValidator documentoValidator() {
        return new DocumentoValidatorImpl();
    }
}
