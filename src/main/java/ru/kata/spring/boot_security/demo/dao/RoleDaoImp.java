package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.Set;

@Repository
public class RoleDaoImp implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Set<Role> findAvailableRoles() {
        String jpql = "SELECT u FROM Role u";
        TypedQuery<Role> query = entityManager.createQuery(jpql, Role.class);
        return new HashSet<>(query.getResultList());
    }

    @Override
    public Set<Role> getRolesByUserId(Long id) {
        String jpql = "SELECT r FROM User u JOIN u.roles r WHERE u.id = :id";
        TypedQuery<Role> query = entityManager.createQuery(jpql, Role.class);
        query.setParameter("id", id);
        return new HashSet<>(query.getResultList());
    }

    @Override
    public Role findRoleById(Long id) {
        return entityManager.find(Role.class, id);
    }
}
