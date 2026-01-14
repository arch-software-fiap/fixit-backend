package com.fix_it.infra.config;

import com.fix_it.application.usecasesimpl.AdicionarItemPecaUseCaseImpl;
import com.fix_it.application.usecasesimpl.AdicionarItemServicoUseCaseImpl;
import com.fix_it.application.usecasesimpl.AtualizarStatusOrdemServicoUseCaseImpl;
import com.fix_it.application.usecasesimpl.AprovarOrcamentoUseCaseImpl;
import com.fix_it.application.usecasesimpl.BuscarOrdemServicoPorIdUseCaseImpl;
import com.fix_it.application.usecasesimpl.ConsultarAcompanhamentoUseCaseImpl;
import com.fix_it.application.usecasesimpl.ConsultarStatusOrdemServicoUseCaseImpl;
import com.fix_it.application.usecasesimpl.CriarOrdemServicoUseCaseImpl;
import com.fix_it.application.usecasesimpl.ListarOrdensServicoUseCaseImpl;
import com.fix_it.usecase.ordemservico.AdicionarItemPecaUseCase;
import com.fix_it.usecase.ordemservico.AdicionarItemServicoUseCase;
import com.fix_it.usecase.ordemservico.AtualizarStatusOrdemServicoUseCase;
import com.fix_it.usecase.ordemservico.AprovarOrcamentoUseCase;
import com.fix_it.usecase.ordemservico.BuscarOrdemServicoPorIdUseCase;
import com.fix_it.usecase.ordemservico.ConsultarAcompanhamentoUseCase;
import com.fix_it.usecase.ordemservico.ConsultarStatusOrdemServicoUseCase;
import com.fix_it.usecase.ordemservico.CriarOrdemServicoUseCase;
import com.fix_it.usecase.ordemservico.ListarOrdensServicoUseCase;
import com.fix_it.usecase.port.ClienteRepository;
import com.fix_it.usecase.port.ItemEstoqueRepository;
import com.fix_it.usecase.port.ItemPecaOSRepository;
import com.fix_it.usecase.port.ItemServicoOSRepository;
import com.fix_it.usecase.port.MovimentoEstoqueRepository;
import com.fix_it.usecase.port.OrdemServicoRepository;
import com.fix_it.usecase.port.ServicoRepository;
import com.fix_it.usecase.port.VeiculoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrdemServicoUseCaseConfig {

    @Bean
    public CriarOrdemServicoUseCase criarOrdemServicoUseCase(OrdemServicoRepository osRepository, ClienteRepository clienteRepository, VeiculoRepository veiculoRepository, ServicoRepository servicoRepository, ItemServicoOSRepository itemServicoOSRepository, ItemEstoqueRepository itemEstoqueRepository, ItemPecaOSRepository itemPecaOSRepository) {
        return new CriarOrdemServicoUseCaseImpl(osRepository, clienteRepository, veiculoRepository, servicoRepository, itemServicoOSRepository, itemEstoqueRepository, itemPecaOSRepository);
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

    @Bean
    public ConsultarStatusOrdemServicoUseCase consultarStatusOrdemServicoUseCase(OrdemServicoRepository repository) {
        return new ConsultarStatusOrdemServicoUseCaseImpl(repository);
    }

    @Bean
    public AprovarOrcamentoUseCase aprovarOrcamentoUseCase(OrdemServicoRepository repository) {
        return new AprovarOrcamentoUseCaseImpl(repository);
    }

    @Bean
    public ListarOrdensServicoUseCase listarOrdensServicoUseCase(OrdemServicoRepository repository) {
        return new ListarOrdensServicoUseCaseImpl(repository);
    }

    @Bean
    public AtualizarStatusOrdemServicoUseCase atualizarStatusOrdemServicoUseCase(OrdemServicoRepository repository) {
        return new AtualizarStatusOrdemServicoUseCaseImpl(repository);
    }
}
