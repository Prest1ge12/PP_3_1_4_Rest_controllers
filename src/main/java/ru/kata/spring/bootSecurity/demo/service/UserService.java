package ru.kata.spring.bootSecurity.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.bootSecurity.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getAllUsers();

    User getUserById(Long id);

    void saveUser(User user);

    void updateUser(Long id, User updateUser, String password, List<Long> roles);

    void deleteUser(Long id);


}
