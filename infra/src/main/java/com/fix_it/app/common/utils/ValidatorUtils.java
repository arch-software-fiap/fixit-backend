package com.fix_it.app.common.utils;

public class ValidatorUtils {

    public static void validarCpfOrCnpj(String documento) {
        validarDocumentoIsNotNull(documento);
        // remove caracteres não numéricos
        String numeros = documento.replaceAll("\\D", "");
        if (!numeros.matches("\\d+")) {
            throw new IllegalArgumentException("O documento deve conter apenas números.");
        }

        if (numeros.length() == 11) {
            validarCpf(numeros);
        } else if (numeros.length() == 14) {
            validarCnpj(numeros);
        } else {
            throw new IllegalArgumentException("O documento deve ter 11 dígitos (CPF) ou 14 dígitos (CNPJ).");
        }
    }

    public static void validarDocumentoIsNotNull(String documento) {
        if (documento == null || documento.isBlank()) {
            throw new IllegalArgumentException("O documento não pode ser vazio.");
        }
    }

    public static void validarCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("O CPF não pode ser nulo.");
        }

        String numeros = cpf.replaceAll("\\D", "");

        if (numeros.length() != 11) {
            throw new IllegalArgumentException("O CPF deve conter 11 dígitos.");
        }

        if (numeros.matches("(\\d)\\1{10}")) {
            throw new IllegalArgumentException("CPF inválido (todos os dígitos iguais).");
        }

    }

    public static void validarCnpj(String cnpj) {
        if (cnpj == null || cnpj.isBlank()) {
            throw new IllegalArgumentException("O CNPJ não pode ser nulo.");
        }

        String numeros = cnpj.replaceAll("\\D", "");

        if (numeros.length() != 14) {
            throw new IllegalArgumentException("O CNPJ deve conter 14 dígitos.");
        }

        if (numeros.matches("(\\d)\\1{13}")) {
            throw new IllegalArgumentException("CNPJ inválido (todos os dígitos iguais).");
        }

    }
}
