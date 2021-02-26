package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.app.exceptions.IdNotFoundException;
import org.example.web.dto.Book;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepository implements ProjectRepository<Book>, ApplicationContextAware {

    private final Logger logger = Logger.getLogger(BookRepository.class);
    //    private final List<Book> repo = new ArrayList<>();
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private ApplicationContext context;

    @Autowired
    public BookRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

//    {
//        repo.add(new Book(3849287, "Robert Carmen", "Spoken English: Flourish Your Language", 225));
//        repo.add(new Book(3672634, "Robert Carmen", "Some interesting book", 345));
//        repo.add(new Book(9238748, "Robert Carmen", "Some good and wondreful book", 500));
//        repo.add(new Book(4728374, "Jonathan Matua", "The book of my son", 100));
//        repo.add(new Book(5957873, "Jonathan Matua", "The book of my son 2", 225));
//        repo.add(new Book(7498374, "Jonathan Matua", "Readings", 110));
//        repo.add(new Book(2387463, "Jane Herberth", "Spoken English: Flourish Your Language", 231));
//        repo.add(new Book(2349873, "Todeush Kostyushko", "The relevance of anthropological research", 1201));
//
//    }

    @Override
    public List<Book> retreiveAll() {
        List<Book> books = jdbcTemplate.query("SELECT * FROM books", (ResultSet rs, int rowNum) -> {
            Book book = new Book();
            book.setId((rs.getInt("id")));
            book.setAuthor((rs.getString("author")));
            book.setTitle((rs.getString("title")));
            book.setSize((rs.getInt("size")));
            return book;
        });
        return new ArrayList<>(books);
    }

    @Override
    public void store(Book book) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author", book.getAuthor());
        parameterSource.addValue("title", book.getTitle());
        parameterSource.addValue("size", book.getSize());
        jdbcTemplate.update("INSERT INTO BOOKS(author, title,size) VALUES(:author, :title, :size)", parameterSource);
        logger.info("store new book: " + book);
    }

    @Override
    public boolean removeItemById(Integer bookIdToRemove) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", bookIdToRemove);
        int update = jdbcTemplate.update("DELETE FROM BOOKS WHERE id = :id", parameterSource);
        if (update == 0) {
            logger.info("Book id does not exist");
            throw new IdNotFoundException(bookIdToRemove, "Id NOT found");
        }
        logger.info("remove book completed");
        return true;
    }

    @Override
    public boolean removeItemByFilter(String regAuthorToRemove, String regTitleToRemove, String regSizeToRemove) {

        List<Book> toRemove = new ArrayList<>();

        if (regAuthorToRemove.isEmpty()) {
            regAuthorToRemove = ".*";
        }
        if (regTitleToRemove.isEmpty()) {
            regTitleToRemove = ".*";
        }
        if (regSizeToRemove.isEmpty()) {
            regSizeToRemove = ".*";
        }

        boolean remove = false;
//        for (Book book : repo) {
//            if (Pattern.matches(regAuthorToRemove, book.getAuthor()) &&
//                    Pattern.matches(regTitleToRemove, book.getTitle()) &&
//                    Pattern.matches(regSizeToRemove, book.getSize().toString())) {
//                toRemove.add(book);
//                remove = true;
//            }
//        }

//        repo.removeAll(toRemove);
//        logger.info("filter removal book completed: " + book);
        return remove;
    }

    @Override
    public List<Book> filter(String regAuthorToRemove, String regTitleToRemove, String regSizeToRemove) {
        logger.info("filtering of books completed");
        Integer regSizeToRemoveToInt = null;

        if (regAuthorToRemove.isEmpty()) {
            regAuthorToRemove = "%";
        }
        if (regTitleToRemove.isEmpty()) {
            regTitleToRemove = "%";
        }
        if (regSizeToRemove.isEmpty()) {
        } else {
            regSizeToRemoveToInt = Integer.parseInt(regSizeToRemove);
        }

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author", regAuthorToRemove);
        parameterSource.addValue("title", regTitleToRemove);
        parameterSource.addValue("size", regSizeToRemoveToInt);

        return jdbcTemplate.query("SELECT * FROM BOOKS" +
                        " WHERE AUTHOR LIKE :author OR TITLE LIKE :title OR SIZE LIKE :size",
                parameterSource, (rs, rowNum) -> {
                    Book book = new Book();
                    book.setAuthor((rs.getString("author")));
                    book.setTitle((rs.getString("title")));
                    book.setSize((rs.getInt("size")));
                    return book;
                });
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
