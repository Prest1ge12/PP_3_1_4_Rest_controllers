package ru.kata.spring.bootSecurity.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.bootSecurity.demo.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class RoleDaoImp implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Role findRoleById(Long id) {
        return entityManager.find(Role.class, id);
    }


}
