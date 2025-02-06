package com.example.backendweb.Repository.User;

import com.example.backendweb.Entity.User.Authentication;
import com.example.backendweb.Entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthenticationRepository extends JpaRepository<Authentication,Integer> {
    Optional<Authentication> findByUsername(String username);

    Optional<Authentication> findByUser(User user);
}
