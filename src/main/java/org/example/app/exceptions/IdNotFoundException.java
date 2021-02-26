package org.example.app.exceptions;

public class IdNotFoundException extends RuntimeException {
    private final String message;
    private final int idToRemove;

    public IdNotFoundException(int idToRemove, String message) {
        this.message = message;
        this.idToRemove = idToRemove;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getIdToRemove() {
        return idToRemove;
    }
}
