package com.fix_it.core.domain.entity;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class VeiculoTest {

    @Test
    void deveCriarVeiculoComFactoryNovo() {
        Cliente cliente = Cliente.novo("João", "123", "j@j.com", "456", null);

        Veiculo veiculo = Veiculo.novo("Carro", "Descrição", "ABC1234", "Marca", "Modelo", 2020, cliente);

        assertNull(veiculo.getId());
        assertEquals("Carro", veiculo.getNmVeiculo());
        assertEquals("Descrição", veiculo.getDsVeiculo());
        assertEquals("ABC1234", veiculo.getPlaca());
        assertEquals("Marca", veiculo.getMarca());
        assertEquals("Modelo", veiculo.getModelo());
        assertEquals(2020, veiculo.getAno());
        assertEquals(cliente, veiculo.getCliente());
        assertNotNull(veiculo.getDthCadastro());
    }

    @Test
    void deveAtualizarDadosDoVeiculo() {
        Cliente cliente = Cliente.novo("João", "123", "j@j.com", "456", null);
        Veiculo veiculo = Veiculo.novo("Carro", "Descrição", "ABC1234", "Marca", "Modelo", 2020, cliente);

        veiculo.atualizar("Moto", "Nova descrição", "XYZ9999", "Outra Marca", "Outro Modelo", 2021);

        assertEquals("Moto", veiculo.getNmVeiculo());
        assertEquals("Nova descrição", veiculo.getDsVeiculo());
        assertEquals("XYZ9999", veiculo.getPlaca());
        assertEquals("Outra Marca", veiculo.getMarca());
        assertEquals("Outro Modelo", veiculo.getModelo());
        assertEquals(2021, veiculo.getAno());
    }

    @Test
    void deveUsarEqualsEHashCodePorId() {
        UUID id = UUID.randomUUID();
        Cliente cliente = Cliente.novo("João", "123", "j@j.com", "456", null);
        Veiculo veiculo1 = Veiculo.of(id, "A", "B", "C", "D", "E", 2020, java.time.LocalDateTime.now(), cliente);
        Veiculo veiculo2 = Veiculo.of(id, "X", "Y", "Z", "M", "N", 2021, java.time.LocalDateTime.now(), cliente);

        assertEquals(veiculo1, veiculo2);
        assertEquals(veiculo1.hashCode(), veiculo2.hashCode());
    }
}
