package com.example.backendweb.Repository;

import com.example.backendweb.Entity.Info.Attraction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttractionRepository extends JpaRepository<Attraction,Integer> {
    boolean existsByUuid(String uuid);
    Optional<Attraction> findByUuid(String uuid);
}
