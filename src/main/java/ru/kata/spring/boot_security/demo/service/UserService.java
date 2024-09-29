package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getAllUsers();

    User getUserById(Long id);

    void saveUser(User user, List<Long> roles);

    void updateUser(Long id, User updateUser, String password, List<Long> roles);

    void deleteUser(Long id);


}
