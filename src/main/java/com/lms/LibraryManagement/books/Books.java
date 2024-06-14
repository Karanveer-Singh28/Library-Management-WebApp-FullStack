package com.lms.LibraryManagement.books;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lms.LibraryManagement.users.Users;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.LocalDate;

@Entity
public class Books {

    @Id
    @GeneratedValue
    Integer id;

    @Size(min = 3, max = 30)
    @NotNull(message = "Title cannot be Null")
    String Title;

    @NotNull(message = "Author cannot be null")
    String Author;

    @PositiveOrZero(message = "Quantity cannot be Negative")
    Integer Quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    Users user;

    @PastOrPresent
    private LocalDate issuedate;

    @Enumerated(EnumType.STRING)
    private BookStatus status = BookStatus.NORMAL;


    public Books(Integer id, String title, String author, Integer quantity, LocalDate issuedate, BookStatus status) {
        this.id = id;
        Title = title;
        Author = author;
        Quantity = quantity;
        this.issuedate = issuedate;
        this.status=status;
    }

    public Books() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public Integer getQuantity() {
        return Quantity;
    }

    public void setQuantity(Integer quantity) {
        Quantity = quantity;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public LocalDate getIssuedate() {
        return issuedate;
    }

    public void setIssuedate(LocalDate issuedate) {
        this.issuedate = issuedate;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Books{" +
                "id=" + id +
                ", Title='" + Title + '\'' +
                ", Author='" + Author + '\'' +
                ", Quantity=" + Quantity +
                '}';
    }
}
