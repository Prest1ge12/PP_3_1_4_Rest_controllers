package ru.kata.spring.bootSecurity.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.bootSecurity.demo.dao.RoleDao;
import ru.kata.spring.bootSecurity.demo.dao.UserDao;
import ru.kata.spring.bootSecurity.demo.model.Role;
import ru.kata.spring.bootSecurity.demo.model.User;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class UserServiceImp implements UserService {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImp(UserDao userDao, RoleDao roleDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public User getUserById(Long id) {
        return userDao.findUserById(id);
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Кодируем пароль
        System.out.println("ЗДЕСЬ БУДЕМ МЕНЯТЬ РОЛИ: " + user);
        System.out.println(user.getRoles());

        Set<Role> existingRoles = new HashSet<>();
        for (Role role : user.getRoles()) {
            Role selectRoleId = roleDao.findRoleByName(role.getRoleName());
            System.out.println(selectRoleId);
            if (selectRoleId != null) {
                existingRoles.add(selectRoleId);
            }
        }

        user.setRoles(existingRoles); // Устанавливаем роли для пользователя
        userDao.saveUser(user);
    }

    @Override
    @Transactional
    public void updateUser(Long id, User updateUser, String updatePassword, List<Long> roles) {

        User existingUser = userDao.findUserById(id);

        if (existingUser == null) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }

        // Очистка существующих ролей
        existingUser.setRoles(new HashSet<>());

        // Добавляем новые роли
        for (Long roleId : roles) {
            Role role = roleDao.findRoleById(roleId);
            if (role != null) {
                existingUser.getRoles().add(role);
            }
        }

        if (updateUser.getPassword() != null && !updateUser.getPassword().isEmpty() &&
                !updateUser.getPassword().equals(updatePassword)) {
            existingUser.setPassword(passwordEncoder.encode(updatePassword));
        }

        //TODO Удалил добавление пароля проверить

        existingUser.setUsername(updateUser.getUsername());
        existingUser.setUserSurname(updateUser.getUserSurname());
        existingUser.setUserEmail(updateUser.getUserEmail());
        existingUser.setAge(updateUser.getAge());

        // Обновляем пользователя в DAO
        userDao.updateUser(existingUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userDao.findUserById(id).setRoles(new HashSet<>()); // Назначаю пустой список ролей перед удалением пользователя
        userDao.deleteUser(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User loadUser = userDao.findByUsername(username);
        if (loadUser == null) {
            throw new UsernameNotFoundException(String.format("User '%s' не найден", username));
        }
//        Hibernate.initialize(user.getRoles());
        System.out.println("Загружен пользователь: " + loadUser.getUsername() + "с паролем :" + loadUser.getPassword() + " с ролями: " + loadUser.getRoles());
        return loadUser;
    }
}
