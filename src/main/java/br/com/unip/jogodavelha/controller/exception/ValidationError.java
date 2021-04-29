package br.com.unip.jogodavelha.controller.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ValidationError extends StandarError {

    private List<FieldMessage> fieldMessages;

    public ValidationError(Long timeStamp, Integer status, String error, String message, String path) {
        super(timeStamp, status, error, message, path);
    }

    public void addErrors(String fieldName, String message) {
        this.getFieldMessages().add(new FieldMessage(fieldName, message));
    }

}
