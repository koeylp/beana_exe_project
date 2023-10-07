package com.exe201.beana.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ResourceNameAlreadyExistsException extends RuntimeException {

    private String message;

    public ResourceNameAlreadyExistsException(String message) {
        super(message);
        this.message = message;
    }

}
