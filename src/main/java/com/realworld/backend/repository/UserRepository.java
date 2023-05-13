package com.realworld.backend.repository;

import com.realworld.backend.domain.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final EntityManager em;

    public Long save(User user) {
        em.persist(user);
        return user.getId();
    }

    public User find(Long id) {
        return em.find(User.class, id);
    }

    public Optional<User> findByName(String name) {
        return em.createQuery("select user from User user where user.name=:name", User.class)
                .setParameter("name", name)
                .getResultList()
                .stream()
                .findFirst();
    }
}
