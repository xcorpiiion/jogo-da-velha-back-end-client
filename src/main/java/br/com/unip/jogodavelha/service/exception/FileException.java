package br.com.unip.jogodavelha.service.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor()
public class FileException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FileException(String message) {
        super(message);
    }
}
