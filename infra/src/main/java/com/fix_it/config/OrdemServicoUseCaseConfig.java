package com.fix_it.config;

import com.fix_it.application.usecasesimpl.*;
import com.fix_it.usecase.ordemservico.*;
import com.fix_it.usecase.port.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrdemServicoUseCaseConfig {

    @Bean
    public CriarOrdemServicoUseCase criarOrdemServicoUseCase(OrdemServicoRepository osRepository, ClienteRepository clienteRepository, VeiculoRepository veiculoRepository) {
        return new CriarOrdemServicoUseCaseImpl(osRepository, clienteRepository, veiculoRepository);
    }

    @Bean
    public BuscarOrdemServicoPorIdUseCase buscarOrdemServicoPorIdUseCase(OrdemServicoRepository repository) {
        return new BuscarOrdemServicoPorIdUseCaseImpl(repository);
    }

    @Bean
    public AdicionarItemServicoUseCase adicionarItemServicoUseCase(
            OrdemServicoRepository osRepository,
            ServicoRepository servicoRepository,
            ItemServicoOSRepository itemServicoRepository,
            ItemPecaOSRepository itemPecaRepository) {
        return new AdicionarItemServicoUseCaseImpl(osRepository, servicoRepository, itemServicoRepository, itemPecaRepository);
    }

    @Bean
    public AdicionarItemPecaUseCase adicionarItemPecaUseCase(
            OrdemServicoRepository osRepository,
            ItemEstoqueRepository itemEstoqueRepository,
            ItemPecaOSRepository itemPecaRepository,
            MovimentoEstoqueRepository movimentoEstoqueRepository,
            ItemServicoOSRepository itemServicoRepository) {
        return new AdicionarItemPecaUseCaseImpl(osRepository, itemEstoqueRepository, itemPecaRepository, movimentoEstoqueRepository, itemServicoRepository);
    }

    @Bean
    public ConsultarAcompanhamentoUseCase consultarAcompanhamentoUseCase(OrdemServicoRepository repository) {
        return new ConsultarAcompanhamentoUseCaseImpl(repository);
    }
}
