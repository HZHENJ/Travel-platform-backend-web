package com.example.backendweb.Repository;

import com.example.backendweb.entity.user.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationRepository extends JpaRepository<Authentication,Integer> {
}
