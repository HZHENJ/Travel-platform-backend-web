package com.example.backendweb.Controller.Booking;

import com.example.backendweb.DTO.Booking.AttractionBookingDTO;
import com.example.backendweb.DTO.Booking.AttractionBookingRequest;
import com.example.backendweb.Entity.Booking.AttractionBooking;
import com.example.backendweb.Services.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName BookingAttractionController
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/2/6
 * @Version 1.0
 */

@RestController
@RequestMapping("/api/attractions")
public class AttractionBookingController {
    private final BookingService bookingService;
    public AttractionBookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Processing requests to create attraction reservations
     * @param request Booking information coming from the front end
     * @return Booking Details
     */
    @PostMapping("/booking")
    public ResponseEntity<?> createAttractionBooking(@RequestBody AttractionBookingRequest request) {
        try {
            AttractionBooking booking = bookingService.createAttractionBooking(request);
            return ResponseEntity.ok(booking); // 200 OK，Returns the created reservation information
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Booking failed: " + e.getMessage()); // 400 incorrect
        }
    }

    @GetMapping("/bookings/{userId}")
    public ResponseEntity<List<AttractionBookingDTO>> getAttractionBookingsByUser(@PathVariable Integer userId) {
        List<AttractionBookingDTO> attractionBookings = bookingService.getAttractionBookingsByUserId(userId);

        // If no booking is found, returns HTTP 204 No Content
        if (attractionBookings.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(attractionBookings);
    }

    /**
     * Cancel Attraction Reservations
     */
    @DeleteMapping("/booking/{bookingId}")
    public ResponseEntity<?> cancelAttractionBooking(@PathVariable Integer bookingId) {
        try {
            bookingService.cancelAttractionBooking(bookingId);
            return ResponseEntity.ok("Attraction booking canceled successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to cancel booking: " + e.getMessage());
        }
    }

}
