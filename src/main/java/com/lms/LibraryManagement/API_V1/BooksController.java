package com.lms.LibraryManagement.API_V1;

import com.lms.LibraryManagement.books.BookService;
import com.lms.LibraryManagement.books.Books;
import com.lms.LibraryManagement.exception.userNotFoundException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class BooksController {

    private final BookService bookService;

    BooksController(BookService bookService)
    {
        this.bookService= bookService;
    }

    @GetMapping("/books")
    public List<Books> retrieveAllBooks()
    {
        return bookService.findAll();
    }

    @GetMapping("/books/{id}")
    public Books retrieveBookById(@PathVariable int id)
    {
        Optional<Books> booksOptional = bookService.findById(id);
        if(booksOptional.isPresent())
            return booksOptional.get();
        else
            throw new userNotFoundException("Book with id:" +id+" not found");
    }

    @PostMapping("/books")
    public void createBook(@Valid @RequestBody Books book)
    {
        bookService.save(book);
    }
}
