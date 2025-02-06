package com.example.backendweb.Repository;

import com.example.backendweb.Entity.Booking.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @ClassName BookingRepository
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/2/6
 * @Version 1.0
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

}
