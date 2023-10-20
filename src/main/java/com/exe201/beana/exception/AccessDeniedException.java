package com.exe201.beana.exception;

public class AccessDeniedException extends RuntimeException {
    AccessDeniedException(String message) {
        super(message);
    }
}
