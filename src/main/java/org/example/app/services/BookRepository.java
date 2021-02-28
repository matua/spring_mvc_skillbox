package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.app.exceptions.BookNotFoundException;
import org.example.app.exceptions.DangerousAllDeleteException;
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
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private ApplicationContext context;

    @Autowired
    public BookRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


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
        int updateResult = jdbcTemplate.update("DELETE FROM BOOKS WHERE id = :id", parameterSource);
        if (updateResult == 0) {
            logger.info("Book id does not exist");
            throw new IdNotFoundException(bookIdToRemove, "Id NOT found");
        }
        logger.info("remove book completed");
        return true;
    }

    @Override
    public Integer removeItemByFilter(String regAuthorToRemove, String regTitleToRemove, String regSizeToRemove) {
        if (regAuthorToRemove.isEmpty() && regTitleToRemove.isEmpty() && regSizeToRemove.isEmpty()){
            throw new DangerousAllDeleteException("If we permitted this, you would delete all books from the library.");
        }

        MapSqlParameterSource parameterSource = setParameters(regAuthorToRemove, regTitleToRemove, regSizeToRemove);

        int updateResult = jdbcTemplate.update("DELETE FROM BOOKS" +
                        " WHERE AUTHOR LIKE :author AND TITLE LIKE :title AND SIZE LIKE :size",
                parameterSource);
        if (updateResult == 0) {
            logger.info("Such book(s) do(es) not exist(s)");
            int regSizeToRemoveInt = 0;
            if (!regSizeToRemove.isEmpty()) {
                regSizeToRemoveInt = Integer.parseInt(regSizeToRemove);
            }
            throw new BookNotFoundException(
                    new Book(null, regAuthorToRemove, regTitleToRemove, regSizeToRemoveInt),
                    "Id NOT found");
        }

        logger.info("removing of books completed");

        return updateResult;
    }

    @Override
    public List<Book> filter(String regAuthorToRemove, String regTitleToRemove, String regSizeToRemove) {
        logger.info("filtering of books completed");

        MapSqlParameterSource parameterSource = setParameters(regAuthorToRemove, regTitleToRemove, regSizeToRemove);

        return jdbcTemplate.query("SELECT * FROM BOOKS" +
                        " WHERE AUTHOR LIKE :author AND TITLE LIKE :title AND SIZE LIKE :size",
                parameterSource, (rs, rowNum) -> {
                    Book book = new Book();
                    book.setId((rs.getInt("id")));
                    book.setAuthor((rs.getString("author")));
                    book.setTitle((rs.getString("title")));
                    book.setSize((rs.getInt("size")));
                    return book;
                });
    }

    private MapSqlParameterSource setParameters(String regAuthorToRemove, String regTitleToRemove, String regSizeToRemove) {
        if (regAuthorToRemove.isEmpty()) {
            regAuthorToRemove = "%";
        }
        if (regTitleToRemove.isEmpty()) {
            regTitleToRemove = "%";
        }

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author", regAuthorToRemove);
        parameterSource.addValue("title", regTitleToRemove);

        if (regSizeToRemove.isEmpty()) {
            parameterSource.addValue("size", "%");
        } else {
            parameterSource.addValue("size", Integer.parseInt(regSizeToRemove));
        }
        return parameterSource;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}