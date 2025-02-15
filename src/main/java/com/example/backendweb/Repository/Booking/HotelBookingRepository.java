package com.example.backendweb.Repository.Booking;

import com.example.backendweb.Entity.Booking.HotelBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelBookingRepository extends JpaRepository<HotelBooking, Integer> {
    long countByHotelId(Integer hotelId);
    List<HotelBooking> findByBookingIdIn(List<Integer> bookingIds);
    Optional<HotelBooking> findByBookingId(Integer bookingId);
}