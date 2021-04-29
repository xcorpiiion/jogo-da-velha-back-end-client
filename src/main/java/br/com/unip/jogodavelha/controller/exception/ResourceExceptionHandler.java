package br.com.unip.jogodavelha.controller.exception;

import br.com.unip.jogodavelha.service.exception.AuthorizationException;
import br.com.unip.jogodavelha.service.exception.DataIntegrityException;
import br.com.unip.jogodavelha.service.exception.FileException;
import br.com.unip.jogodavelha.service.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

import static java.lang.System.currentTimeMillis;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.status;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(DataIntegrityException.class)
    public ResponseEntity<StandarError> dateIntegrity(DataIntegrityException e, HttpServletRequest request) {
        StandarError standarError = getStandarError(request, BAD_REQUEST, "Data integraty", e.getMessage());
        return status(BAD_REQUEST).body(standarError);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandarError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request) {
        StandarError standarError = getStandarError(request, NOT_FOUND, "Not found", e.getMessage());
        return status(NOT_FOUND).body(standarError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandarError> methodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {
        ValidationError validationError = new ValidationError(currentTimeMillis(), UNPROCESSABLE_ENTITY.value(), "Validation Error", e.getMessage(), request.getRequestURI());
        validationError.setFieldMessages(new ArrayList<>());
        e.getBindingResult().getFieldErrors().
                forEach(fieldError -> validationError.addErrors(fieldError.getField(), fieldError.getDefaultMessage()));
        return status(UNPROCESSABLE_ENTITY).body(validationError);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<StandarError> authorization(AuthorizationException e, HttpServletRequest request) {
        StandarError standarError = getStandarError(request, FORBIDDEN, "Acess denied", e.getMessage());
        return status(FORBIDDEN).body(standarError);
    }

    @ExceptionHandler(FileException.class)
    public ResponseEntity<StandarError> file(FileException e, HttpServletRequest request) {
        StandarError standarError = getStandarError(request, BAD_REQUEST, "File error", e.getMessage());
        return status(BAD_REQUEST).body(standarError);
    }

    private StandarError getStandarError(HttpServletRequest request, HttpStatus badRequest, String error, String message) {
        return new StandarError(currentTimeMillis(), badRequest.value(), error, message, request.getRequestURI());
    }

}
