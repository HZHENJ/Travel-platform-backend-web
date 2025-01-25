package com.example.backendweb.Repository;

import com.example.backendweb.entity.user.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthenticationRepository extends JpaRepository<Authentication,Integer> {
    Optional<Authentication> findByUsername(String username);
}
