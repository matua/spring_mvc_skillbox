package org.example.app.exceptions;

public class FileTooBigToUploadException extends RuntimeException {
    private final String message;
    private final long fileSize;

    public FileTooBigToUploadException(long fileSize, String message) {
        this.message = message;
        this.fileSize = fileSize;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public long getFileSize() {
        return fileSize;
    }
}
