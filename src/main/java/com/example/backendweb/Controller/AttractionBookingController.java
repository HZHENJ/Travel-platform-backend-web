package com.example.backendweb.Controller;

import com.example.backendweb.Entity.Booking.AttractionBooking;
import com.example.backendweb.Services.AttractionBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@RequestMapping("/api/attractionBookings")
public class AttractionBookingController {

    @Autowired
    private AttractionBookingService attractionBookingService; // Create this service

    @GetMapping
    public List<AttractionBooking> getAllAttractionBookings() {
        return attractionBookingService.getAllAttractionBookings();
    }

    @PutMapping("/{attractionBookingId}")
    public ResponseEntity<AttractionBooking> updateAttractionBooking(
            @PathVariable Integer attractionBookingId, @RequestBody AttractionBooking attractionBooking) {

        AttractionBooking updatedBooking = attractionBookingService.updateAttractionBooking(attractionBookingId, attractionBooking);
        return ResponseEntity.ok(updatedBooking);
    }

    @DeleteMapping("/{attractionBookingId}")
    public ResponseEntity<Void> deleteAttractionBooking(@PathVariable Integer attractionBookingId) {
        attractionBookingService.deleteAttractionBooking(attractionBookingId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/popular-attractions")
    public ResponseEntity<List<Map<String, Object>>> getPopularAttractions() {
        List<Map<String, Object>> popularAttractions = attractionBookingService.getAttractionsOrderedByBookingCount();
        return ResponseEntity.ok(popularAttractions);
    }
}
