package br.com.unip.jogodavelha.service.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor()
public class DataIntegrityException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DataIntegrityException(String message) {
        super(message);
    }
}
