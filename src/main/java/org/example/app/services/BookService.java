package org.example.app.services;

import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final ProjectRepository<Book> bookRepo;

    @Autowired
    public BookService(ProjectRepository<Book> bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<Book> getAllBooks() {
        return bookRepo.retreiveAll();
    }

    public void saveBook(Book book) {
        bookRepo.store(book);
    }

    public boolean removeBookById(String bookIdToRemove) {
        return bookRepo.removeItemById(bookIdToRemove);
    }

    public boolean removeBookByFilter(String regAuthorToRemove, String regTitleToRemove, String regSizeToRemove) {
        return bookRepo.removeItemByFilter(regAuthorToRemove, regTitleToRemove, regSizeToRemove);
    }

    public List<Book> filter(String regAuthorToFilter, String regTitleToFilter, String regSizeToFilter) {
        return bookRepo.filter(regAuthorToFilter, regTitleToFilter, regSizeToFilter);
    }
}
