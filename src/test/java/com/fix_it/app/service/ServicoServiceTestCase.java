package com.fix_it.app.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.fix_it.app.common.dto.ServicoDTO;
import com.fix_it.app.model.Servico;
import com.fix_it.app.repository.ServicoRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
@DisplayName("ServicoService - Testes de regras e persistência")
public class ServicoServiceTestCase {

    @Mock
    ServicoRepository servicoRepository;
    
    @InjectMocks
    ServicoService service;

    private ServicoDTO createDto() {
        ServicoDTO d = new ServicoDTO();
        d.setNome("Troca de óleo");
        d.setDescricao("Troca de óleo do motor");
        d.setPrecoBase(15000L); // R$ 150,00
        return d;
    }

    @Test
    @DisplayName("create_ok → deve criar serviço e retornar DTO com ID")
    void create_ok() {
        when(servicoRepository.save(any())).thenAnswer(i -> {
            Servico s = i.getArgument(0);
            s.setId(UUID.randomUUID());
            return s;
        });

        var resp = service.create(createDto());

        assertThat(resp.getId()).isNotNull();
        assertThat(resp.getNome()).isEqualTo("Troca de óleo");
        assertThat(resp.getPrecoBase()).isEqualTo(15000L);
        verify(servicoRepository).save(any(Servico.class));
    }

    @Test
    @DisplayName("findById_ok → deve retornar DTO quando serviço existir")
    void findById_ok() {
        UUID id = UUID.randomUUID();
        Servico servico = new Servico();
        servico.setId(id);
        servico.setNmServico("Troca de óleo");
        servico.setDsServico("Troca de óleo do motor");
        servico.setVlPrecoBase(15000L);

        when(servicoRepository.findById(id)).thenReturn(Optional.of(servico));

        var resp = service.findById(id);

        assertThat(resp.getId()).isEqualTo(id);
        assertThat(resp.getNome()).isEqualTo("Troca de óleo");
        assertThat(resp.getPrecoBase()).isEqualTo(15000L);
    }

    @Test
    @DisplayName("findById_notFound → deve lançar 404 (EntityNotFoundException) se não existir")
    void findById_notFound() {
        UUID id = UUID.randomUUID();
        when(servicoRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Serviço não encontrado");
    }

    @Test
    @DisplayName("update_ok → deve atualizar dados do serviço")
    void update_ok() {
        UUID id = UUID.randomUUID();

        Servico existente = new Servico();
        existente.setId(id);
        existente.setNmServico("Nome antigo");
        existente.setVlPrecoBase(10000L);

        when(servicoRepository.findById(id)).thenReturn(Optional.of(existente));
        when(servicoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        ServicoDTO dto = createDto();
        var resp = service.update(id, dto);

        assertThat(resp.getNome()).isEqualTo("Troca de óleo");
        assertThat(resp.getPrecoBase()).isEqualTo(15000L);
        verify(servicoRepository).save(existente);
    }

    @Test
    @DisplayName("update_notFound → deve falhar ao atualizar se o ID não existir")
    void update_notFound() {
        UUID id = UUID.randomUUID();
        when(servicoRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(id, createDto()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Serviço não encontrado");
    }

    @Test
    @DisplayName("getAll_ok → deve paginar resultados e manter metadados de página")
    void getAll_ok() {
        Servico s1 = new Servico();
        s1.setNmServico("Serviço 1");
        
        Servico s2 = new Servico();
        s2.setNmServico("Serviço 2");

        when(servicoRepository.findAll(PageRequest.of(0, 2)))
                .thenReturn(new PageImpl<>(List.of(s1, s2)));

        var page = service.findAll(0, 2);

        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getSize()).isEqualTo(2);
    }

    @Test
    @DisplayName("delete_notFound → deve falhar ao deletar se o ID não existir")
    void delete_notFound() {
        UUID id = UUID.randomUUID();
        when(servicoRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Serviço não encontrado");
    }

    @Test
    @DisplayName("delete_ok → deve deletar quando o ID existir")
    void delete_ok() {
        UUID id = UUID.randomUUID();
        when(servicoRepository.existsById(id)).thenReturn(true);

        service.deleteById(id);

        verify(servicoRepository).deleteById(id);
    }
}
