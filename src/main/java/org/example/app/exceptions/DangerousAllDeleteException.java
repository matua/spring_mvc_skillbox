package org.example.app.exceptions;

public class DangerousAllDeleteException extends RuntimeException {
    private final String message;

    public DangerousAllDeleteException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
