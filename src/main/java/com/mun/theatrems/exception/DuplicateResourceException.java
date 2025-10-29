package com.mun.theatrems.exception;

/**
 * Exception thrown when attempting to create a duplicate resource
 */
public class DuplicateResourceException extends ApplicationException {
    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String resource, String field, Object value) {
        super(String.format("%s already exists with %s: %s", resource, field, value));
    }
}