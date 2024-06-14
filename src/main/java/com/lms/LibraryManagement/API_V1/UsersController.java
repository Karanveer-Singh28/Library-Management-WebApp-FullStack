package com.lms.LibraryManagement.API_V1;

import com.lms.LibraryManagement.books.BookService;
import com.lms.LibraryManagement.books.BookStatus;
import com.lms.LibraryManagement.books.Books;
import com.lms.LibraryManagement.exception.InsufficientBalanceException;
import com.lms.LibraryManagement.exception.userNotFoundException;
import com.lms.LibraryManagement.users.UserService;
import com.lms.LibraryManagement.users.Users;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
public class UsersController {


    /*
    1. Add new User, display all users
    2. Issue new book to a user
    3. Return a book / book lost / book damaged
    4. Retrieve user information:
        i. Name
        ii. Phone number, Email, Address
        iii. Books issued
        iv. Account Balance
    5. Change user information
        i. Phone Number, Email, Address
        iv. Account balance */

    private final UserService userService;
    private final BookService bookService;

    public UsersController(UserService userService, BookService bookService) {
        this.userService = userService;
        this.bookService = bookService;
    }

    //1. Add a new user
    @PostMapping("/users")
    public void createUser(@Valid  @RequestBody Users user)
    {
        userService.save(user);
    }


    @GetMapping("/users")
    public List<Users> displayAllUsers()
    {
        return userService.findAll();
    }

    //2. Issue new book to user
    @PostMapping("/users/{userid}/books/{bookid}")
    public void issueBook(@PathVariable int userid,@PathVariable int bookid)
    {
        Users user = retrieveUserById(userid);
        Optional<Books> booksOptional= bookService.findById(bookid);

        if(booksOptional.isPresent()) {
            Books book = booksOptional.get();
            if (user.getAccount_balance() == 0.0) {
                book.setUser(user);
                book.setIssuedate(LocalDate.now());
                bookService.save(book);
            }
            else
                throw new InsufficientBalanceException("Clear the pending userBalance before issuing a new book");
        }
        else {
            throw new userNotFoundException("Book id: "+bookid+" not found");
        }
    }

    // Update user information
    @PutMapping("/users/{id}")
    public void updateUserInformation(@Valid @RequestBody Users user, @PathVariable int id)
    {
        user.setId(id);
        userService.save(user);
    }

    //Return book
    @PutMapping("/books/{id}/{status}")
    public void returnBook(@PathVariable int id, @PathVariable String status)
    {
        Optional<Books> bookOptional= bookService.findById(id);
        if(bookOptional.isPresent() && bookOptional.get().getUser()!=null)
        {
            updateBookStatus(bookOptional.get(), status);
        }
        else if (bookOptional.isEmpty())
        {
            throw new userNotFoundException("Book id: "+id+"not found");
        }
        else
        {
            throw new RuntimeException("Book already returned");
        }
    }


    public void updateBookStatus(Books book, String status)
    {
        String prevStatus = book.getStatus().toString();

        if(prevStatus.equalsIgnoreCase(status) && LocalDate.now().isBefore(book.getIssuedate().plusMonths(2)))
        {
            removeBook(book,book.getUser());

        } else if (book.getStatus()!= BookStatus.LOST && !status.equalsIgnoreCase("Lost")) {
            Users user= book.getUser();
            double balance = user.getAccount_balance();
            balance+=20.0;
            user.setAccount_balance(balance);
            book.setStatus(BookStatus.DAMAGED);
            removeBook(book,user);

        } else if (!status.equalsIgnoreCase("Lost")) {
            Users user= book.getUser();
            double balance = user.getAccount_balance();
            balance+=100.0;
            user.setAccount_balance(balance);
            book.setStatus(BookStatus.LOST);
            removeBook(book,user);
        }
        else {
            Users user= book.getUser();
            double balance = user.getAccount_balance();
            balance+=20.0;
            user.setAccount_balance(balance);
            removeBook(book,user);
        }
    }

    public void removeBook(Books book, Users user)
    {
        List<Books> userBooks = user.getBooks();
        userBooks = userBooks.stream()
                .filter(b -> !b.getId().equals(book.getId()))
                .collect(Collectors.toList());
        user.setBooks(userBooks);;
        book.setUser(null);
        bookService.save(book);
        userService.save(user);
    }




    //4. Retrieve User information
    @GetMapping("users/{id}")
    public Users retrieveUserById(@PathVariable int id)
    {
        Optional<Users> optionalUsers = userService.findById(id);
        if (optionalUsers.isPresent())
            return optionalUsers.get();
        else
            throw new userNotFoundException("User with id:"+id+" not found");
    }


    //4. iii. Retrieve user information - books issued
    @GetMapping("users/{id}/books")
    public List<Books> retrieveAllBooksByUserId(@PathVariable int id)
    {
        Optional<Users> user = userService.findById(id);

        if(user.isEmpty())
        {
            throw new userNotFoundException("id: "+id);
        }

        return user.get().getBooks();
    }
}
