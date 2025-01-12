package dev.luanfernandes.domain.exception;

public class AnagramException extends RuntimeException {
    public AnagramException(String message, Throwable cause) {
        super(message, cause);
    }
}
