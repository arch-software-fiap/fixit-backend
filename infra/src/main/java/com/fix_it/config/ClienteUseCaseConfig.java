package com.fix_it.config;

import com.fix_it.application.usecasesimpl.AtualizarClienteUseCaseImpl;
import com.fix_it.application.usecasesimpl.CriarClienteUseCaseImpl;
import com.fix_it.usecase.cliente.AtualizarClienteUseCase;
import com.fix_it.usecase.cliente.CriarClienteUseCase;
import com.fix_it.usecase.port.ClienteRepository;
import com.fix_it.usecase.port.DocumentoValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClienteUseCaseConfig {

    @Bean
    public CriarClienteUseCase criarClienteUseCase(ClienteRepository clienteRepository,
                                                   DocumentoValidator documentoValidator) {
        return new CriarClienteUseCaseImpl(clienteRepository, documentoValidator);
    }

    @Bean
    public AtualizarClienteUseCase atualizarClienteUseCase(ClienteRepository clienteRepository,
                                                           DocumentoValidator documentoValidator) {
        return new AtualizarClienteUseCaseImpl(clienteRepository, documentoValidator);
    }
}
