package com.fix_it.infra.service.cliente;

import com.fix_it.usecase.port.DocumentoValidator;
import org.springframework.stereotype.Component;

@Component
public class DocumentoValidatorImpl implements DocumentoValidator {

    @Override
    public void validarCpfOuCnpj(String documento) {
        if (documento == null || documento.isEmpty()) {
            throw new IllegalArgumentException("Documento não pode ser nulo ou vazio.");
        }

        String cleanedDoc = documento.replaceAll("[^0-9]", "");

        if (cleanedDoc.length() == 11) {
            // Basic CPF validation
            if (cleanedDoc.matches("(\\d)\\1{10}")) {
                throw new IllegalArgumentException("CPF inválido.");
            }
        } else if (cleanedDoc.length() == 14) {
            // Basic CNPJ validation
            if (cleanedDoc.matches("(\\d)\\1{13}")) {
                throw new IllegalArgumentException("CNPJ inválido.");
            }
        } else {
            throw new IllegalArgumentException("Documento inválido. Deve ser um CPF com 11 dígitos ou um CNPJ com 14 dígitos.");
        }
    }
}
