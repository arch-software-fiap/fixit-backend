package com.fix_it.app.service;

import com.fix_it.app.model.Cliente;
import com.fix_it.app.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ClienteService - Testes de regras e persistência")
public class ClienteServiceTestCase {

    @Mock
    ClienteRepository clienteRepository;

    @InjectMocks
    ClienteService service;

    private Cliente novoCliente(String cpfCnpj) {
        Cliente c = new Cliente();
        c.setNome("Cliente Teste");
        c.setCpfCnpj(cpfCnpj);
        return c;
    }

    @Test
    @DisplayName("create_ok → deve criar cliente quando CPF/CNPJ é novo e válido")
    void create_ok() {
        Cliente cliente = novoCliente("12345678901");

        when(clienteRepository.existsByCpfCnpj(cliente.getCpfCnpj())).thenReturn(false);
        when(clienteRepository.save(any())).thenAnswer(invocation -> {
            Cliente c = invocation.getArgument(0);
            c.setId(UUID.randomUUID());
            return c;
        });

        var resp = service.create(cliente);

        assertThat(resp.getId()).isNotNull();
        assertThat(resp.getCpfCnpj()).isEqualTo(cliente.getCpfCnpj());
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    @DisplayName("create_conflict → deve falhar ao criar se CPF/CNPJ já existir")
    void create_conflict() {
        Cliente cliente = novoCliente("12345678901");

        when(clienteRepository.existsByCpfCnpj(cliente.getCpfCnpj())).thenReturn(true);

        assertThatThrownBy(() -> service.create(cliente))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("já existe");

        verify(clienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("update_ok → deve atualizar cliente existente com dados válidos")
    void update_ok() {
        UUID id = UUID.randomUUID();
        Cliente existente = novoCliente("12345678901");
        existente.setId(id);

        Cliente atualizado = novoCliente("98765432100");
        atualizado.setNome("CLiente Atualizada");

        when(clienteRepository.findById(id)).thenReturn(Optional.of(existente));
        when(clienteRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Cliente resp = service.update(id, atualizado);

        assertThat(resp.getCpfCnpj()).isEqualTo("98765432100");
        assertThat(resp.getNome()).isEqualTo("CLiente Atualizada");
        verify(clienteRepository).save(existente);
    }

    @Test
    @DisplayName("update_notFound → deve lançar 404 (EntityNotFound) se não encontrar cliente por ID")
    void update_notFound() {
        UUID id = UUID.randomUUID();
        Cliente cliente = novoCliente("12345678901");

        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(id, cliente))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("não encontrado");

        verify(clienteRepository, never()).save(any());
    }


}
