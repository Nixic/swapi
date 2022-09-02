package com.example.swapi.exception;

// todo need add class with @ControllerAdvice to use this exception, for example ExceptionControllerAdvice.java
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
