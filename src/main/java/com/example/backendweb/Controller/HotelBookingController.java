package com.example.backendweb.Controller;

import com.example.backendweb.Entity.Booking.HotelBooking;
import com.example.backendweb.Services.HotelBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@RequestMapping("/api/hotelBookings")
public class HotelBookingController {

    @Autowired
    private HotelBookingService hotelBookingService;

    @GetMapping
    public List<HotelBooking> getAllHotelBookings() {
        return hotelBookingService.getAllHotelBookings();
    }

    @PutMapping("/{hotelBookingId}")
    public ResponseEntity<HotelBooking> updateHotelBooking(
            @PathVariable Integer hotelBookingId, @RequestBody HotelBooking hotelBooking) {

        HotelBooking updatedBooking = hotelBookingService.updateHotelBooking(hotelBookingId, hotelBooking);
        return ResponseEntity.ok(updatedBooking);
    }

    @DeleteMapping("/{hotelBookingId}")
    public ResponseEntity<Void> deleteHotelBooking(@PathVariable Integer hotelBookingId) {
        hotelBookingService.deleteHotelBooking(hotelBookingId);
        return ResponseEntity.noContent().build();
    }

}
