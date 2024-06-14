package com.lms.LibraryManagement.users;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserService extends JpaRepository<Users, Integer> {

}
