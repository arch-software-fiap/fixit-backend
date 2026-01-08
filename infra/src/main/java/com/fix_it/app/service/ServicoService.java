package com.fix_it.app.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fix_it.app.common.dto.ServicoDTO;
import com.fix_it.app.common.factory.ServicoFactory;
import com.fix_it.app.model.Servico;
import com.fix_it.app.repository.ServicoRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServicoService {

    private final ServicoRepository servicoRepository;

    @Transactional
    public ServicoDTO create(@Valid ServicoDTO dto) {
        Servico novo = ServicoFactory.from(dto);
        return ServicoDTO.from(servicoRepository.save(novo));
    }

    @Transactional(readOnly = true)
    public ServicoDTO findById(UUID id) {
        Servico s = servicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado com o ID: " + id));
        return ServicoDTO.from(s);
    }

    @Transactional(readOnly = true)
    public Page<ServicoDTO> findAll(int page, int size) {
        return servicoRepository.findAll(PageRequest.of(page, size))
                .map(ServicoDTO::from);
    }

    @Transactional
    public ServicoDTO update(UUID id, @Valid ServicoDTO dto) {
        Servico atual = servicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado com o ID: " + id));

        ServicoFactory.updateEntity(dto, atual);

        return ServicoDTO.from(servicoRepository.save(atual));
    }

    @Transactional
    public void deleteById(UUID id) {
        if (!servicoRepository.existsById(id)) {
            throw new EntityNotFoundException("Serviço não encontrado com o ID: " + id);
        }
        servicoRepository.deleteById(id);
    }
}
