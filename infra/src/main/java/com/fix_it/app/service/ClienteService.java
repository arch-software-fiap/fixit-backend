package com.fix_it.app.service;

import com.fix_it.app.model.Cliente;
import com.fix_it.app.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.fix_it.app.common.utils.ValidatorUtils.validarCpfOrCnpj;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional
    public Cliente create(@Valid Cliente cliente) {
        validarClienteNaoExiste(cliente.getCpfCnpj());
        validarCpfOrCnpj(cliente.getCpfCnpj());
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente update(@Valid UUID id, @Valid Cliente cliente) {
        var existe = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com documento {}" + cliente.getCpfCnpj()));
        if(!cliente.getCpfCnpj().equals(existe.getCpfCnpj())) {
            validarCpfOrCnpj(cliente.getCpfCnpj());
        }
        existe.updateFrom(cliente);
        return clienteRepository.save(existe);
    }

    private void validarClienteNaoExiste(String documento) {
        boolean existe = clienteRepository.existsByCpfCnpj(documento);
        if (existe) {
            throw new IllegalArgumentException("Cliente já existe com este número de documento " + documento);
        }
    }
}
