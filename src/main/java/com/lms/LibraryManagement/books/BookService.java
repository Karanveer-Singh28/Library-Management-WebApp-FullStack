package com.lms.LibraryManagement.books;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookService extends JpaRepository<Books, Integer> {

}