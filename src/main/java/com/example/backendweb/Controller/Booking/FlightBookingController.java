package com.example.backendweb.Controller.Booking;

import com.example.backendweb.Entity.Booking.FlightBooking;
import com.example.backendweb.Services.FlightBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@RequestMapping("/api/flightBookings")
public class FlightBookingController {

    @Autowired
    private FlightBookingService flightBookingService;

    @GetMapping
    public List<FlightBooking> getAllFlightBookings() {
        return flightBookingService.getAllFlightBookings();
    }

    @DeleteMapping("/{flightBookingId}")
    public ResponseEntity<Void> deleteFlightBooking(@PathVariable Integer flightBookingId) {
        flightBookingService.deleteFlightBooking(flightBookingId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{flightBookingId}")
    public ResponseEntity<FlightBooking> updateFlightBooking(
            @PathVariable Integer flightBookingId, @RequestBody FlightBooking flightBooking) {

        FlightBooking updatedBooking = flightBookingService.updateFlightBooking(flightBookingId, flightBooking);
        return ResponseEntity.ok(updatedBooking);
    }

}
