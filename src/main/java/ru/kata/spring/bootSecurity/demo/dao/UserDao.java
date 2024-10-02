package ru.kata.spring.bootSecurity.demo.dao;


import ru.kata.spring.bootSecurity.demo.model.User;

import java.util.List;

public interface UserDao {

    List<User> getAllUsers();

    User findUserById(Long id);

    void saveUser(User user);

    void updateUser(User updateUser);

    void deleteUser(Long id);

    User findByUsername(String name);


}
