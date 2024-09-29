package ru.kata.spring.boot_security.demo.utils;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.HashSet;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Создаем роли
        Role adminRole = new Role("ADMIN");
        Role userRole = new Role("USER");

        // Создаем первого пользователя (админ)
        User adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setUserSurname("Adminov");
        adminUser.setAge(30);
        adminUser.setUserEmail("admin@example.com");
        adminUser.setPassword(passwordEncoder.encode("admin"));
        adminUser.setRoles(new HashSet<>(List.of(userRole, adminRole)));

        // Создаем второго пользователя (обычный пользователь)
        User regularUser = new User();
        regularUser.setUsername("user");
        regularUser.setUserSurname("Userov");
        regularUser.setAge(25);
        regularUser.setUserEmail("user@example.com");
        regularUser.setPassword(passwordEncoder.encode("user"));
        regularUser.setRoles(new HashSet<>(List.of(userRole)));

        // Сохраняем пользователей в базу
        userDao.saveUser(adminUser);
        userDao.saveUser(regularUser);
    }
}

