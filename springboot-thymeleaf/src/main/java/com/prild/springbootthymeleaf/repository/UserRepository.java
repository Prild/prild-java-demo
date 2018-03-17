package com.prild.springbootthymeleaf.repository;

import com.prild.springbootthymeleaf.pojo.User;

import java.util.List;

public interface UserRepository {

    User saveOrupdateUser(User user);

    void deleteUser(Integer id);

    User getUserById(Integer id);

    List<User> selectAllUsers();
}
