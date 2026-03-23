package com.fix_it.infra.repository;

import com.fix_it.infra.config.RepositoryTestBase;
import com.fix_it.infra.domain.ClienteEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ClienteRepository - Testes de Integração")
class ClienteRepositoryIntegrationTest extends RepositoryTestBase {

    @Autowired
    private SpringDataClienteJpaRepository clienteRepository;

    private ClienteEntity clienteExistente;

    @BeforeEach
    void setUp() {
        clienteRepository.deleteAll();

        clienteExistente = new ClienteEntity();
        clienteExistente.setNome("João Silva");
        clienteExistente.setCpfCnpj("12345678901");
        clienteExistente.setEmail("joao@email.com");
        clienteExistente.setTelefone("11999999999");
        clienteExistente.setDtNascimento(LocalDate.of(1990, 1, 1));
        clienteExistente = clienteRepository.save(clienteExistente);
    }

    @Nested
    @DisplayName("Operações de Persistência")
    class OperacoesPersistencia {

        @Test
        @DisplayName("Deve salvar um novo cliente com ID gerado automaticamente")
        void deveSalvarNovoCliente() {
            ClienteEntity novoCliente = new ClienteEntity();
            novoCliente.setNome("Maria Santos");
            novoCliente.setCpfCnpj("98765432100");
            novoCliente.setEmail("maria@email.com");
            novoCliente.setTelefone("11888888888");
            novoCliente.setDtNascimento(LocalDate.of(1992, 2, 2));

            ClienteEntity clienteSalvo = clienteRepository.save(novoCliente);

            assertThat(clienteSalvo.getId()).isNotNull();
            assertThat(clienteSalvo.getNome()).isEqualTo("Maria Santos");
            assertThat(clienteSalvo.getCpfCnpj()).isEqualTo("98765432100");
            assertThat(clienteSalvo.getDtNascimento()).isEqualTo(LocalDate.of(1992, 2, 2));
            assertThat(clienteSalvo.getDtCadastro()).isNotNull();
            assertThat(clienteSalvo.getDthAtualizacao()).isNotNull();
        }

        @Test
        @DisplayName("Deve atualizar cliente existente")
        void deveAtualizarClienteExistente() {
            clienteExistente.setNome("João Silva Atualizado");
            clienteExistente.setEmail("joao.atualizado@email.com");
            clienteExistente.setDtNascimento(LocalDate.of(1991, 1, 1));

            ClienteEntity clienteAtualizado = clienteRepository.save(clienteExistente);

            assertThat(clienteAtualizado.getNome()).isEqualTo("João Silva Atualizado");
            assertThat(clienteAtualizado.getEmail()).isEqualTo("joao.atualizado@email.com");
            assertThat(clienteAtualizado.getDtNascimento()).isEqualTo(LocalDate.of(1991, 1, 1));
        }

        @Test
        @DisplayName("Deve remover cliente por ID")
        void deveRemoverClientePorId() {
            UUID idCliente = clienteExistente.getId();

            clienteRepository.deleteById(idCliente);

            Optional<ClienteEntity> clienteRemovido = clienteRepository.findById(idCliente);
            assertThat(clienteRemovido).isEmpty();
        }
    }

    @Nested
    @DisplayName("Operações de Busca")
    class OperacoesBusca {

        @Test
        @DisplayName("Deve buscar cliente por ID")
        void deveBuscarClientePorId() {
            Optional<ClienteEntity> clienteEncontrado = clienteRepository.findById(clienteExistente.getId());

            assertThat(clienteEncontrado).isPresent();
            assertThat(clienteEncontrado.get().getNome()).isEqualTo("João Silva");
        }

        @Test
        @DisplayName("Deve retornar vazio ao buscar por ID inexistente")
        void deveRetornarVazioParaIdInexistente() {
            Optional<ClienteEntity> cliente = clienteRepository.findById(UUID.randomUUID());

            assertThat(cliente).isEmpty();
        }

        @Test
        @DisplayName("Deve buscar cliente por CPF/CNPJ")
        void deveBuscarClientePorCpfCnpj() {
            Optional<ClienteEntity> clienteEncontrado = clienteRepository.findByCpfCnpj("12345678901");

            assertThat(clienteEncontrado).isPresent();
            assertThat(clienteEncontrado.get().getNome()).isEqualTo("João Silva");
            assertThat(clienteEncontrado.get().getDtNascimento()).isEqualTo(LocalDate.of(1990, 1, 1));
        }

        @Test
        @DisplayName("Deve retornar vazio ao buscar por CPF/CNPJ inexistente")
        void deveRetornarVazioParaCpfCnpjInexistente() {
            Optional<ClienteEntity> cliente = clienteRepository.findByCpfCnpj("00000000000");

            assertThat(cliente).isEmpty();
        }

        @Test
        @DisplayName("Deve listar todos os clientes")
        void deveListarTodosClientes() {
            ClienteEntity outroCliente = new ClienteEntity();
            outroCliente.setNome("Pedro Souza");
            outroCliente.setCpfCnpj("11122233344");
            outroCliente.setEmail("pedro@email.com");
            outroCliente.setDtNascimento(LocalDate.of(1988, 8, 8));
            clienteRepository.save(outroCliente);

            var clientes = clienteRepository.findAll();

            assertThat(clientes).hasSize(2);
        }
    }

    @Nested
    @DisplayName("Verificações de Existência")
    class VerificacoesExistencia {

        @Test
        @DisplayName("Deve retornar true quando CPF/CNPJ existe")
        void deveRetornarTrueQuandoCpfCnpjExiste() {
            boolean existe = clienteRepository.existsByCpfCnpj("12345678901");

            assertThat(existe).isTrue();
        }

        @Test
        @DisplayName("Deve retornar false quando CPF/CNPJ não existe")
        void deveRetornarFalseQuandoCpfCnpjNaoExiste() {
            boolean existe = clienteRepository.existsByCpfCnpj("00000000000");

            assertThat(existe).isFalse();
        }
    }
}
