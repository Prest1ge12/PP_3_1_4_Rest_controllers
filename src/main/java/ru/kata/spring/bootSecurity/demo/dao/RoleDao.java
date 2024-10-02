package ru.kata.spring.bootSecurity.demo.dao;

import ru.kata.spring.bootSecurity.demo.model.Role;

public interface RoleDao {

    Role findRoleById(Long roleId);
}
