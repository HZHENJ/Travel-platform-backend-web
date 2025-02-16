package com.example.backendweb.Repository.Booking;

import com.example.backendweb.Entity.Booking.FlightBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightBookingRepository extends JpaRepository<FlightBooking, Long> {
    List<FlightBooking> findByBookingIdIn(List<Integer> bkIds);
}
