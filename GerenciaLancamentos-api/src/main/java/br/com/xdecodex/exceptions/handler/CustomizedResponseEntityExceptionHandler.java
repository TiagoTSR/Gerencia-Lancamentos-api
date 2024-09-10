package br.com.xdecodex.exceptions.handler;

import java.util.Date;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.xdecodex.exceptions.ExceptionResponse;

@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        ExceptionResponse exceptionResponse = createExceptionResponse(
                "mensagem.invalida",
                ex.getCause() != null ? ex.getCause().toString() : ex.toString());
        return handleExceptionInternal(ex, exceptionResponse, headers, HttpStatus.BAD_REQUEST, request);
    }

    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        ExceptionResponse exceptionResponse = createErrorList(ex.getBindingResult());
        return handleExceptionInternal(ex, exceptionResponse, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex,
            WebRequest request) {
        ExceptionResponse exceptionResponse = createExceptionResponse(
                "recurso.nao-encontrado",
                ex.toString());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex,
            WebRequest request) {
        ExceptionResponse exceptionResponse = createExceptionResponse(
                "recurso.operacao-nao-permitida",
                ExceptionUtils.getRootCauseMessage(ex));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    private ExceptionResponse createExceptionResponse(String userMessageKey, String developerMessage) {
        String userMessage = messageSource.getMessage(userMessageKey, null, LocaleContextHolder.getLocale());
        return new ExceptionResponse(new Date(), userMessage, developerMessage);
    }

    private ExceptionResponse createErrorList(BindingResult bindingResult) {
        String userMessage = bindingResult.getFieldErrors().stream()
                .map(fieldError -> messageSource.getMessage(fieldError, LocaleContextHolder.getLocale()))
                .findFirst()
                .orElse("Validation error");
        String developerMessage = bindingResult.getFieldErrors().stream()
                .map(FieldError::toString)
                .findFirst()
                .orElse("Validation error");

        return new ExceptionResponse(new Date(), userMessage, developerMessage);
    }
}