package ru.kata.spring.bootSecurity.demo.dao;


import org.springframework.stereotype.Repository;
import ru.kata.spring.bootSecurity.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {
    @PersistenceContext
    private EntityManager entityManager;

    public UserDaoImp() {
    }

    @Override
    public List<User> getAllUsers() {
        String jpql = "SELECT u FROM User u";
        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
        return query.getResultList();
    }

    @Override
    public User findUserById(Long id) {
        User user = entityManager.find(User.class, id);
        if (user == null) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        return user;
    }

    @Override
    public void saveUser(User newUser) {
        entityManager.persist(newUser);
    }

    @Override
    public void updateUser(User updateUser) {
        entityManager.merge(updateUser);
    }


    @Override
    public void deleteUser(Long id) {
        User user = entityManager.find(User.class, id);
        if (user == null) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        entityManager.remove(user);
    }

    @Override
    public User findByUsername(String name) {
        try {
            return entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new EntityNotFoundException("User not found with username: " + name);
        }
    }
}