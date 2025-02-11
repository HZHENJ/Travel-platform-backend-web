package com.example.backendweb.Controller.Booking;

import com.example.backendweb.DTO.Booking.AttractionBookingDTO;
import com.example.backendweb.DTO.Booking.HotelBookingDTO;
import com.example.backendweb.DTO.Booking.HotelBookingRequest;
import com.example.backendweb.Entity.Booking.HotelBooking;
import com.example.backendweb.Services.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName HotelBookingController
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/2/11
 * @Version 1.0
 */

@RestController
@RequestMapping("/api/hotels")
public class HotelBookingController {
    private final BookingService bookingService;

    public HotelBookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * 处理创建酒店预订的请求
     */
    @PostMapping("/booking")
    public ResponseEntity<?> createHotelBooking(@RequestBody HotelBookingRequest request) {
        try {
            HotelBooking hotelBooking = bookingService.createHotelBooking(request);
            return ResponseEntity.ok(hotelBooking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Booking failed: " + e.getMessage());
        }
    }

    @GetMapping("/bookings/{userId}")
    public ResponseEntity<List<HotelBookingDTO>> getHotelBookingsByUser(@PathVariable Integer userId) {
        List<HotelBookingDTO> hotelBookings = bookingService.getHotelBookingsByUserId(userId);
        return ResponseEntity.ok(hotelBookings);
    }
}
