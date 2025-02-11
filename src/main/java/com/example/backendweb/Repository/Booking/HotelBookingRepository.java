package com.example.backendweb.Repository.Booking;

import com.example.backendweb.Entity.Booking.HotelBooking;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName HotelBookingRepository
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/2/11
 * @Version 1.0
 */
public interface HotelBookingRepository extends JpaRepository<HotelBooking, Integer> {
    long countByHotelId(Integer hotelId);
}
