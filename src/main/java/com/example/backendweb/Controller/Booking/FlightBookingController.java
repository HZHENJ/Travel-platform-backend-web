package com.example.backendweb.Controller.Booking;

import com.example.backendweb.DTO.Booking.FlightBookingRequest;
import com.example.backendweb.Entity.Booking.FlightBooking;
import com.example.backendweb.Services.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightBookingController {

    private final BookingService bookingService;

    public FlightBookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/booking")
    public ResponseEntity<FlightBooking> createFlightBooking(FlightBookingRequest request) {
        return ResponseEntity.ok(bookingService.createFlightBooking(request));
    }

    @GetMapping("/bookings/{userId}")
    public ResponseEntity<List<FlightBooking>> getFlightBookingsByUser(@PathVariable Integer userId) {
        List<FlightBooking> flightBookings = bookingService.getFlightBookingsByUserId(userId);

        // 如果没有找到任何 booking，返回 HTTP 204 No Content
        if (flightBookings.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(flightBookings);
    }
}
