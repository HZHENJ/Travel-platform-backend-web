package com.example.backendweb.Repository;

import com.example.backendweb.Entity.Info.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @ClassName HotelRepository
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/2/11
 * @Version 1.0
 */
public interface HotelRepository extends JpaRepository<Hotel, Integer> {
    Optional<Hotel> findByUuid(String uuid);
}
