package com.example.backendweb.Repository;

import com.example.backendweb.entity.user.Preference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferenceRepository extends JpaRepository<Preference,Integer> {
}
