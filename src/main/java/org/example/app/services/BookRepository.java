package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

@Repository
public class BookRepository implements ProjectRepository<Book> {

    private final Logger logger = Logger.getLogger(BookRepository.class);
    private final List<Book> repo = new ArrayList<>();

    {
        repo.add(new Book(3849287, "Robert Carmen", "Spoken English: Flourish Your Language", 225));
        repo.add(new Book(3672634, "Robert Carmen", "Some interesting book", 345));
        repo.add(new Book(9238748, "Robert Carmen", "Some good and wondreful book", 500));
        repo.add(new Book(4728374, "Jonathan Matua", "The book of my son", 100));
        repo.add(new Book(5957873, "Jonathan Matua", "The book of my son 2", 225));
        repo.add(new Book(7498374, "Jonathan Matua", "Readings", 110));
        repo.add(new Book(2387463, "Jane Herberth", "Spoken English: Flourish Your Language", 231));
        repo.add(new Book(2349873, "Todeush Kostyushko", "The relevance of anthropological research", 1201));

    }

    @Override
    public List<Book> retreiveAll() {
        return new ArrayList<>(repo);
    }

    @Override
    public void store(Book book) {
        if (!book.getAuthor().isEmpty() || !book.getTitle().isEmpty() || book.getSize() != null) {
            book.setId(book.hashCode());
            logger.info("store new book: " + book);
            repo.add(book);
        }
    }

    @Override
    public boolean removeItemById(Integer bookIdToRemove) {
        for (Book book : retreiveAll()) {
            if (book.getId().equals(bookIdToRemove)) {
                logger.info("remove book completed: " + book);
                return repo.remove(book);
            }
        }
        return false;
    }

    @Override
    public boolean removeItemByFilter(String regAuthorToRemove, String regTitleToRemove, String regSizeToRemove) {

        if (regAuthorToRemove.isEmpty()) {
            regAuthorToRemove = ".*";
        }
        if (regTitleToRemove.isEmpty()) {
            regTitleToRemove = ".*";
        }
        if (regSizeToRemove.isEmpty()) {
            regSizeToRemove = ".*";
        }

        boolean removed = false;

        Iterator<Book> iterator = repo.iterator();
        Book book = iterator.next();
        while (iterator.hasNext()) {
            if (Pattern.matches(regAuthorToRemove, book.getAuthor()) &&
                    Pattern.matches(regTitleToRemove, book.getTitle()) &&
                    Pattern.matches(regSizeToRemove, book.getSize().toString())) {
                iterator.remove();
                removed = true;
            }
        }
        logger.info("filter removal book completed: " + book);

        return removed;
    }
}
