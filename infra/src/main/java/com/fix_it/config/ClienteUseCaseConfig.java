package com.fix_it.config;

import com.fix_it.application.usecasesimpl.BuscarClientePorDocumentoUseCaseImpl;
import com.fix_it.application.usecasesimpl.BuscarClientePorIdUseCaseImpl;
import com.fix_it.application.usecasesimpl.ListarClientesUseCaseImpl;
import com.fix_it.application.usecasesimpl.RemoverClienteUseCaseImpl;
import com.fix_it.usecase.cliente.BuscarClientePorDocumentoUseCase;
import com.fix_it.usecase.cliente.BuscarClientePorIdUseCase;
import com.fix_it.usecase.cliente.ListarClientesUseCase;
import com.fix_it.usecase.cliente.RemoverClienteUseCase;
import com.fix_it.usecase.port.ClienteRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClienteUseCaseConfig {

    @Bean
    public ListarClientesUseCase listarClientesUseCase(ClienteRepository repo) {
        return new ListarClientesUseCaseImpl(repo);
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

}
