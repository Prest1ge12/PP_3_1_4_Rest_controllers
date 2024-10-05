package ru.kata.spring.bootSecurity.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.bootSecurity.demo.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
public class RoleDaoImp implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Role findRoleById(Long id) {
        Query query = entityManager.createQuery("SELECT r FROM Role r WHERE r.id = :id");
        query.setParameter("id", id);
        return (Role) query.getSingleResult();
    }

    @Override
    public Role findRoleByName(String roleName) {
        Query query = entityManager.createQuery("SELECT r FROM Role r WHERE r.roleName = :roleName");
        query.setParameter("roleName", roleName);
        return (Role) query.getSingleResult();
    }
}
