package com.mun.theatrems.exception;

/**
 * Exception thrown when validation or business rules are violated
 */
public class ValidationException extends ApplicationException {
    public ValidationException(String message) {
        super(message);
    }
}