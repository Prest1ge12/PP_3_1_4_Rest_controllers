package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.Set;

@Service
@Transactional(readOnly = true)
public class RoleServiceImp implements RoleService {

    private final RoleDao roleDao;

    @Autowired
    public RoleServiceImp(RoleDao rollDao, UserDao userDao) {
        this.roleDao = rollDao;
    }

    @Override
    public Set<Role> findAvailableRoles() {
        return roleDao.findAvailableRoles();
    }

    @Override
    public Set<Role> getRolesByUserId(Long userId) {
        return roleDao.getRolesByUserId(userId);
    }
}
