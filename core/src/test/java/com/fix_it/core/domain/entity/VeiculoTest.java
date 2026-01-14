package com.fix_it.core.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class VeiculoTest {

    @Test
    @DisplayName("Deve criar um novo veículo com valores iniciais")
    void deveCriarNovoVeiculo() {
        Cliente cliente = Cliente.novo("João", "123", "j@j.com", "456");
        String nmVeiculo = "Civic";
        String dsVeiculo = "Sedan preto";
        String placa = "ABC-1234";
        String marca = "Honda";
        String modelo = "EXL";
        Integer ano = 2022;

        Veiculo veiculo = Veiculo.novo(nmVeiculo, dsVeiculo, placa, marca, modelo, ano, cliente);

        assertThat(veiculo.getId()).isNull();
        assertThat(veiculo.getNmVeiculo()).isEqualTo(nmVeiculo);
        assertThat(veiculo.getPlaca()).isEqualTo(placa);
        assertThat(veiculo.getCliente()).isEqualTo(cliente);
        assertThat(veiculo.getDthCadastro()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    @DisplayName("Deve criar uma instância de veículo com todos os campos")
    void deveCriarVeiculoComOf() {
        UUID id = UUID.randomUUID();
        Cliente cliente = Cliente.novo("João", "123", "j@j.com", "456");
        LocalDateTime agora = LocalDateTime.now();

        Veiculo veiculo = Veiculo.of(id, "Civic", "Sedan", "ABC-1234", "Honda", "EXL", 2022, agora, cliente);

        assertThat(veiculo.getId()).isEqualTo(id);
        assertThat(veiculo.getNmVeiculo()).isEqualTo("Civic");
        assertThat(veiculo.getDthCadastro()).isEqualTo(agora);
        assertThat(veiculo.getCliente()).isEqualTo(cliente);
    }

    @Test
    @DisplayName("Deve atualizar os dados do veículo")
    void deveAtualizarVeiculo() {
        Veiculo veiculo = Veiculo.novo("Nome", "Desc", "Placa", "Marca", "Modelo", 2020, null);

        veiculo.atualizar("Novo Nome", "Nova Desc", "Nova Placa", "Nova Marca", "Novo Modelo", 2023);

        assertThat(veiculo.getNmVeiculo()).isEqualTo("Novo Nome");
        assertThat(veiculo.getPlaca()).isEqualTo("Nova Placa");
        assertThat(veiculo.getAno()).isEqualTo(2023);
    }

    @Test
    @DisplayName("Deve validar equals e hashCode baseados no ID")
    void deveValidarEqualsEHashCode() {
        UUID id = UUID.randomUUID();
        Veiculo v1 = Veiculo.of(id, "V1", "D1", "P1", "M1", "M1", 2020, LocalDateTime.now(), null);
        Veiculo v2 = Veiculo.of(id, "V2", "D2", "P2", "M2", "M2", 2021, LocalDateTime.now(), null);
        Veiculo v3 = Veiculo.of(UUID.randomUUID(), "V1", "D1", "P1", "M1", "M1", 2020, LocalDateTime.now(), null);

        assertThat(v1).isEqualTo(v2);
        assertThat(v1).isNotEqualTo(v3);
        assertThat(v1.hashCode()).isEqualTo(v2.hashCode());
        assertThat(v1.hashCode()).isNotEqualTo(v3.hashCode());
    }
}
