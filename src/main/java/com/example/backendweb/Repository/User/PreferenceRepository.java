package com.example.backendweb.Repository.User;

import com.example.backendweb.Entity.User.Preference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PreferenceRepository extends JpaRepository<Preference,Integer> {
    Optional<Preference> findByUser_UserId(Integer userId);
}
