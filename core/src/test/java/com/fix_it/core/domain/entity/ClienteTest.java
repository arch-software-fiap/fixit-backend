package com.fix_it.core.domain.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Test
    void deveCriarNovoClienteComFabricaNovo() {
        // arrange
        String nome = "Matheus Leal";
        String cpfCnpj = "12345678900";
        String email = "matheus@example.com";
        String telefone = "91999999999";

        LocalDate hoje = LocalDate.now();

        // act
        Cliente cliente = Cliente.novo(nome, cpfCnpj, email, telefone);

        // assert
        assertNull(cliente.getId(), "Novo cliente não deve ter ID definido ainda");
        assertEquals(nome, cliente.getNome());
        assertEquals(cpfCnpj, cliente.getCpfCnpj());
        assertEquals(email, cliente.getEmail());
        assertEquals(telefone, cliente.getTelefone());

        assertNotNull(cliente.getDtCadastro(), "Data de cadastro não deve ser nula");
        assertEquals(hoje, cliente.getDtCadastro(), "Data de cadastro deve ser a data de hoje");

        assertNotNull(cliente.getDthAtualizacao(), "Data/hora de atualização não deve ser nula");
    }

    @Test
    void deveReconstituirClienteComFactoryOf() {
        // arrange
        UUID id = UUID.randomUUID();
        String nome = "Cliente Antigo";
        String cpfCnpj = "11122233344";
        String email = "cliente@teste.com";
        String telefone = "91000000000";
        LocalDate dtCadastro = LocalDate.of(2024, 1, 1);
        LocalDateTime dthAtualizacao = LocalDateTime.of(2024, 1, 1, 10, 0);

        // act
        Cliente cliente = Cliente.of(id, nome, cpfCnpj, email, telefone, dtCadastro, dthAtualizacao);

        // assert
        assertEquals(id, cliente.getId());
        assertEquals(nome, cliente.getNome());
        assertEquals(cpfCnpj, cliente.getCpfCnpj());
        assertEquals(email, cliente.getEmail());
        assertEquals(telefone, cliente.getTelefone());
        assertEquals(dtCadastro, cliente.getDtCadastro());
        assertEquals(dthAtualizacao, cliente.getDthAtualizacao());
    }

    @Test
    void deveAtualizarDadosEADataHoraDeAtualizacao() throws InterruptedException {
        Cliente cliente = Cliente.of(
                UUID.randomUUID(),
                "Nome Original",
                "12345678900",
                "original@teste.com",
                "91000000000",
                LocalDate.of(2024, 1, 1),
                LocalDateTime.now()
        );

        LocalDateTime dthAntes = cliente.getDthAtualizacao();

        String novoNome = "Nome Atualizado";
        String novoCpfCnpj = "98765432100";
        String novoEmail = "atualizado@teste.com";
        String novoTelefone = "91911111111";

        Thread.sleep(5);

        cliente.atualizarDados(novoNome, novoCpfCnpj, novoEmail, novoTelefone);

        assertEquals(novoNome, cliente.getNome());
        assertEquals(novoCpfCnpj, cliente.getCpfCnpj());
        assertEquals(novoEmail, cliente.getEmail());
        assertEquals(novoTelefone, cliente.getTelefone());

        assertNotNull(cliente.getDthAtualizacao());
        assertTrue(cliente.getDthAtualizacao().isAfter(dthAntes),
                "Data/hora de atualização deve ser posterior à anterior");
    }

}
