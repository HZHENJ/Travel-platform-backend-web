package com.example.backendweb.Repository.Booking;

import com.example.backendweb.Entity.Booking.AttractionBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName AttractionBookingRepository
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/2/6
 * @Version 1.0
 */

@Repository
public interface AttractionBookingRepository extends JpaRepository<AttractionBooking, Integer> {
    List<AttractionBooking> findByBookingIdIn(List<Integer> bookingIds);
    int countByAttractionId(Integer attractionId);
}
