package com.fix_it.infra.repository;

import com.fix_it.infra.config.RepositoryTestBase;
import com.fix_it.infra.domain.ServicoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ServicoRepository - Testes de Integração")
class ServicoRepositoryIntegrationTest extends RepositoryTestBase {

    @Autowired
    private SpringDataServicoRepository servicoRepository;

    private ServicoEntity servicoExistente;

    @BeforeEach
    void setUp() {
        servicoRepository.deleteAll();

        servicoExistente = new ServicoEntity();
        servicoExistente.setNmServico("Troca de Óleo");
        servicoExistente.setDsServico("Troca de óleo do motor com filtro incluso");
        servicoExistente.setVlPrecoBase(15000L);
        servicoExistente.setDthAtualizacao(LocalDateTime.now());
        servicoExistente = servicoRepository.save(servicoExistente);
    }

    @Nested
    @DisplayName("Operações de Persistência")
    class OperacoesPersistencia {

        @Test
        @DisplayName("Deve salvar um novo serviço com ID gerado automaticamente")
        void deveSalvarNovoServico() {
            ServicoEntity novoServico = new ServicoEntity();
            novoServico.setNmServico("Alinhamento");
            novoServico.setDsServico("Alinhamento e balanceamento das rodas");
            novoServico.setVlPrecoBase(12000L);
            novoServico.setDthAtualizacao(LocalDateTime.now());

            ServicoEntity servicoSalvo = servicoRepository.save(novoServico);

            assertThat(servicoSalvo.getId()).isNotNull();
            assertThat(servicoSalvo.getNmServico()).isEqualTo("Alinhamento");
            assertThat(servicoSalvo.getVlPrecoBase()).isEqualTo(12000L);
        }

        @Test
        @DisplayName("Deve atualizar serviço existente")
        void deveAtualizarServicoExistente() {
            servicoExistente.setNmServico("Troca de Óleo Sintético");
            servicoExistente.setVlPrecoBase(20000L);

            ServicoEntity servicoAtualizado = servicoRepository.save(servicoExistente);

            assertThat(servicoAtualizado.getNmServico()).isEqualTo("Troca de Óleo Sintético");
            assertThat(servicoAtualizado.getVlPrecoBase()).isEqualTo(20000L);
        }

        @Test
        @DisplayName("Deve remover serviço por ID")
        void deveRemoverServicoPorId() {
            UUID idServico = servicoExistente.getId();

            servicoRepository.deleteById(idServico);

            Optional<ServicoEntity> servicoRemovido = servicoRepository.findById(idServico);
            assertThat(servicoRemovido).isEmpty();
        }
    }

    @Nested
    @DisplayName("Operações de Busca")
    class OperacoesBusca {

        @Test
        @DisplayName("Deve buscar serviço por ID")
        void deveBuscarServicoPorId() {
            Optional<ServicoEntity> servicoEncontrado = servicoRepository.findById(servicoExistente.getId());

            assertThat(servicoEncontrado).isPresent();
            assertThat(servicoEncontrado.get().getNmServico()).isEqualTo("Troca de Óleo");
        }

        @Test
        @DisplayName("Deve retornar vazio ao buscar por ID inexistente")
        void deveRetornarVazioParaIdInexistente() {
            Optional<ServicoEntity> servico = servicoRepository.findById(UUID.randomUUID());

            assertThat(servico).isEmpty();
        }

        @Test
        @DisplayName("Deve listar todos os serviços")
        void deveListarTodosServicos() {
            ServicoEntity outroServico = new ServicoEntity();
            outroServico.setNmServico("Revisão Completa");
            outroServico.setDsServico("Revisão completa do veículo");
            outroServico.setVlPrecoBase(50000L);
            outroServico.setDthAtualizacao(LocalDateTime.now());
            servicoRepository.save(outroServico);

            var servicos = servicoRepository.findAll();

            assertThat(servicos).hasSize(2);
        }
    }
}
