package br.com.xdecodex.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmptyResultDataAccessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EmptyResultDataAccessException(String message) {
        super(message);
    }
}