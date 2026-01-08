package com.fix_it.app.service;

import com.fix_it.app.common.dto.VeiculoDTO;
import com.fix_it.app.common.factory.VeiculoFactory;
import com.fix_it.app.model.Cliente;
import com.fix_it.app.model.Veiculo;
import com.fix_it.app.repository.ClienteRepository;
import com.fix_it.app.repository.VeiculoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;
    private final ClienteRepository clienteRepository;

    @Transactional
    public VeiculoDTO create(@Valid VeiculoDTO dto) {
        String placaNorm = normalize(dto.getPlaca());
        if (veiculoRepository.existsByPlaca(placaNorm)) {
            throw new IllegalArgumentException("A placa " + placaNorm + " já está em uso.");
        }
        Cliente cliente = findClienteById(dto.getClienteId());
        Veiculo novo = VeiculoFactory.from(dto, cliente);
        return VeiculoDTO.from(veiculoRepository.save(novo));
    }

    @Transactional(readOnly = true)
    public VeiculoDTO findById(UUID id) {
        Veiculo v = veiculoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado com o ID: " + id));
        return VeiculoDTO.from(v);
    }

    @Transactional(readOnly = true)
    public Page<VeiculoDTO> findAll(int page, int size) {
        return veiculoRepository.findAll(PageRequest.of(page, size))
                .map(VeiculoDTO::from);
    }

    @Transactional
    public VeiculoDTO update(UUID id, @Valid VeiculoDTO dto) {
        Veiculo atual = veiculoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado com o ID: " + id));

        String novaPlaca = normalize(dto.getPlaca());
        if (!atual.getPlaca().equals(novaPlaca) && veiculoRepository.existsByPlaca(novaPlaca)) {
            throw new IllegalArgumentException("A placa " + novaPlaca + " já está em uso por outro veículo.");
        }

        Cliente cliente = findClienteById(dto.getClienteId());
        VeiculoFactory.updateEntity(dto, atual, cliente);

        return VeiculoDTO.from(veiculoRepository.save(atual));
    }

    @Transactional
    public void deleteById(UUID id) {
        if (!veiculoRepository.existsById(id)) {
            throw new EntityNotFoundException("Veículo não encontrado com o ID: " + id);
        }
        veiculoRepository.deleteById(id);
    }

    private Cliente findClienteById(UUID id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado: " + id));
    }

    private static String normalize(String placa) {
        return placa == null ? null : placa.replaceAll("\\s+", "").toUpperCase();
    }
}
