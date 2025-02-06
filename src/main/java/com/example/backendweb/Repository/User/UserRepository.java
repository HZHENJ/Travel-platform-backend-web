package com.example.backendweb.Repository.User;

import com.example.backendweb.Entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
