package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.exceptions.IdNotFoundException;
import org.example.app.services.BookService;
import org.example.web.dto.Book;
import org.example.web.dto.BookFilter;
import org.example.web.dto.BookIdToRemove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.*;
import java.util.List;

@Controller
@RequestMapping(value = "/books")
@Scope("singleton")
public class BookShelfController {
    @ModelAttribute("book")
    public Book getBook() {
        return new Book();
    }

    @ModelAttribute("bookFilter")
    public BookFilter getBookFilter() {
        return new BookFilter();
    }

    @ModelAttribute("bookIdToRemove")
    public BookIdToRemove getBookIdToRemove() {
        return new BookIdToRemove();
    }

    @ModelAttribute("bookList")
    public List<Book> getBookList() {
        return bookService.getAllBooks();
    }

    private final Logger logger = Logger.getLogger(BookShelfController.class);
    private final BookService bookService;

    @Autowired
    public BookShelfController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/shelf")
    public String books(Model model) {
        logger.info("got book shelf");
        return "book_shelf";
    }

    @PostMapping("/save")
    public String saveBook(@Valid Book book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            return "book_shelf";
        } else {
            bookService.saveBook(book);
            logger.info("current repository size: " + bookService.getAllBooks().size());
            return "redirect:/books/shelf";
        }

    }

    @PostMapping("/remove")
    public String removeBook(@Valid BookIdToRemove bookIdToRemove, BindingResult bindingResult, Model model) {
        logger.info("bookIdToRemove received: " + bookIdToRemove.getId());
        logger.info("Errors???: " + bindingResult.getAllErrors());
        if (bindingResult.hasErrors()) {
            return "book_shelf";
        } else {
            bookService.removeBookById(bookIdToRemove.getId());
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/removeByFilter")
    public String removeBookByFilter(
            @Valid BookFilter bookFilter,
            BindingResult bindingResult,
            Model model,
            @RequestParam String regAuthorToRemove,
            @RequestParam String regTitleToRemove,
            @RequestParam String regSizeToRemove) {
        model.addAttribute("bookFilter", bookFilter);
        if (!bindingResult.hasErrors()) {
            model.addAttribute("bookFilter", bookFilter);
            bookService.removeBookByFilter(regAuthorToRemove, regTitleToRemove, regSizeToRemove);
            return "redirect:/books/shelf";
        }
        return "book_shelf";
    }

    @PostMapping("/filter")
    public String filter(
            Model model,
            @RequestParam String regAuthorToFilter,
            @RequestParam String regTitleToFilter,
            @RequestParam String regSizeToFilter) {
        model.addAttribute("bookIdToRemove", new BookIdToRemove());
        model.addAttribute("bookList", bookService.filter(regAuthorToFilter, regTitleToFilter, regSizeToFilter));
        return "book_shelf";
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String name = file.getOriginalFilename();
        byte[] bytes = file.getBytes();

        String rootPath = System.getProperty("catalina.home");
        File dir = new File(rootPath + File.separator + "external_uploads");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
        try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile))) {
            stream.write(bytes);
        }

        logger.info("new file saved at: " + serverFile.getAbsolutePath());

        return "redirect:/books/shelf";
    }

    @ExceptionHandler(FileNotFoundException.class)
    public String fileUpload(Model model, FileNotFoundException exception) {
        model.addAttribute("fileNotFoundMessage", exception.getMessage());
        return "book_shelf";
    }

    @ExceptionHandler(IdNotFoundException.class)
    public String idNotFound(Model model, IdNotFoundException exception) {
        model.addAttribute("idNotFoundMessage", exception.getMessage());
        model.addAttribute("bookIdToRemoveData", exception.getIdToRemove());
        return "book_shelf";
    }
}