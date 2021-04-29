package br.com.unip.jogodavelha.service.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor()
public class ObjectNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ObjectNotFoundException(String message) {
        super(message);
    }
}
