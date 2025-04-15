package net.lab1024.sa.base.module.support.fileparser.exception;

public class ValidationException extends RuntimeException {
    private final String message;

    public ValidationException(String message) {
        this.message = message;
    }
}
