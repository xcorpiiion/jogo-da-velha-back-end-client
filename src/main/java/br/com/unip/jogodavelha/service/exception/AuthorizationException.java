package br.com.unip.jogodavelha.service.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor()
public class AuthorizationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AuthorizationException(String message) {
        super(message);
    }
}
