package com.lms.LibraryManagement.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lms.LibraryManagement.books.Books;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.*;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.hateoas.InputType;

import java.util.List;

@Entity
public class Users {

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull(message = "Username cannot be null")
    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be empty")
    private String password;

    @PositiveOrZero(message = "Account balance cannot be negative")
    private double account_balance;

   @NotNull(message = "Phone number cannot be null")
    private int phone_number;

    @NotNull(message = "Email cannot be empty")
    @Email(message = "Enter a valid email")
    @NotBlank(message = "email cannot be empty")
    private String email;

    @NotNull(message = "Enter a valid address")
    @NotBlank(message = "address cannot be empty")
    private String address;


    @OneToMany (mappedBy = "user")
    List<Books> books;

    public Users(Integer id, String username, String password, double account_balance, int phone_number, String email, String address, List<Books> books) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.account_balance = account_balance;
        this.phone_number = phone_number;
        this.email = email;
        this.address = address;
        this.books = books;
    }

    public Users() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getAccount_balance() {
        return account_balance;
    }

    public void setAccount_balance(double account_balance) {
        this.account_balance = account_balance;
    }

    public int getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(int phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Books> getBooks() {
        return books;
    }

    public void setBooks(List<Books> books) {
        this.books = books;
    }
}