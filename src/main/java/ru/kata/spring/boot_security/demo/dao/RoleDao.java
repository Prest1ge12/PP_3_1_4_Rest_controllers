package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.Set;

public interface RoleDao {
    Set<Role> findAvailableRoles();

    Set<Role> getRolesByUserId(Long id);

    Role findRoleById(Long roleId);

}
