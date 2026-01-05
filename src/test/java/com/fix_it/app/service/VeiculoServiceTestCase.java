package com.fix_it.app.service;

import com.fix_it.app.common.dto.VeiculoDTO;
import com.fix_it.app.model.Cliente;
import com.fix_it.app.model.Veiculo;
import com.fix_it.app.repository.ClienteRepository;
import com.fix_it.app.repository.VeiculoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("VeiculoService - Testes de regras e persistência")
public class VeiculoServiceTestCase {

    @Mock
    VeiculoRepository veiculoRepository;
    @Mock
    ClienteRepository clienteRepository;
    @InjectMocks
    VeiculoService service;

    private VeiculoDTO dtoByClienteId(UUID clienteId) {
        VeiculoDTO d = new VeiculoDTO();
        d.setNome("Carro");
        d.setPlaca(" abc1234 ");
        d.setMarca("VW");
        d.setModelo("Gol");
        d.setAno(2018);
        d.setDescricao("Flex");
        d.setClienteId(clienteId);
        return d;
    }

    @Test
    @DisplayName("create_ok → deve criar veículo normalizando placa e retornando DTO com ID")
    void create_ok() {
        UUID clienteId = UUID.randomUUID();
        Cliente cliente = new Cliente(); cliente.setId(clienteId);

        when(veiculoRepository.existsByPlaca("ABC1234")).thenReturn(false);
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(veiculoRepository.save(any())).thenAnswer(i -> {
            Veiculo v = i.getArgument(0);
            v.setId(UUID.randomUUID());
            return v;
        });

        var resp = service.create(dtoByClienteId(clienteId));

        assertThat(resp.getId()).isNotNull();
        assertThat(resp.getPlaca()).isEqualTo("ABC1234");
        verify(veiculoRepository).save(any(Veiculo.class));
    }

    @Test
    @DisplayName("create_placaDuplicada → deve falhar ao criar se placa já existir")
    void create_placaDuplicada() {
        UUID clienteId = UUID.randomUUID();
        when(veiculoRepository.existsByPlaca("ABC1234")).thenReturn(true);

        assertThatThrownBy(() -> service.create(dtoByClienteId(clienteId)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("já está em uso");
    }

    @Test
    @DisplayName("findById_notFound → deve lançar 404 (EntityNotFoundException) se não existir")
    void findById_notFound() {
        UUID id = UUID.randomUUID();
        when(veiculoRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("update_ok → deve atualizar dados e normalizar placa quando alterada")
    void update_ok() {
        UUID id = UUID.randomUUID();
        UUID clienteId = UUID.randomUUID();

        Veiculo existente = new Veiculo();
        existente.setId(id);
        existente.setPlaca("OLD1234");

        Cliente cliente = new Cliente(); cliente.setId(clienteId);

        when(veiculoRepository.findById(id)).thenReturn(Optional.of(existente));
        when(veiculoRepository.existsByPlaca("ABC1234")).thenReturn(false);
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(veiculoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        var resp = service.update(id, dtoByClienteId(clienteId));

        assertThat(resp.getPlaca()).isEqualTo("ABC1234");
        verify(veiculoRepository).save(existente);
    }

    @Test
    @DisplayName("update_conflictPlaca → deve falhar ao atualizar se nova placa já existir")
    void update_conflictPlaca() {
        UUID id = UUID.randomUUID();
        UUID clienteId = UUID.randomUUID();

        Veiculo existente = new Veiculo();
        existente.setId(id);
        existente.setPlaca("OLD1234");

        when(veiculoRepository.findById(id)).thenReturn(Optional.of(existente));
        when(veiculoRepository.existsByPlaca("ABC1234")).thenReturn(true);

        assertThatThrownBy(() -> service.update(id, dtoByClienteId(clienteId)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("getAll_ok → deve paginar resultados e manter metadados de página")
    void getAll_ok() {
        when(veiculoRepository.findAll(PageRequest.of(0, 2)))
                .thenReturn(new PageImpl<>(List.of(new Veiculo(), new Veiculo())));

        var page = service.findAll(0, 2);

        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getSize()).isEqualTo(2);
    }

    @Test
    @DisplayName("delete_notFound → deve falhar ao deletar se o ID não existir")
    void delete_notFound() {
        UUID id = UUID.randomUUID();
        when(veiculoRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteById(id))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("delete_ok → deve deletar quando o ID existir")
    void delete_ok() {
        UUID id = UUID.randomUUID();
        when(veiculoRepository.existsById(id)).thenReturn(true);

        service.deleteById(id);

        verify(veiculoRepository).deleteById(id);
    }
}
