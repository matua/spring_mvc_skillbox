package org.example.app.exceptions;

import org.example.web.dto.Book;

public class BookNotFoundException extends RuntimeException {
    private final String message;
    private final Book book;

    public BookNotFoundException(Book book, String message) {
        this.book = book;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Book getBook() {
        return book;
    }
}
