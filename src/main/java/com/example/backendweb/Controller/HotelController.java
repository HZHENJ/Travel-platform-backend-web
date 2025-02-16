package com.example.backendweb.Controller;

import com.example.backendweb.Entity.Review.Review;
import com.example.backendweb.Services.HotelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName HotelController
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/2/13
 * @Version 1.0
 */

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(
            HotelService hotelService
    ) {
        this.hotelService = hotelService;
    }

    @GetMapping("/{uuid}/reviews")
    public ResponseEntity<List<Review>> getReviewsByHotelUuid(@PathVariable String uuid) {
        try {
            // List<ReviewWithUsernameDTO> reviews = attractionService.getReviewsWithUsernameByUuid(uuid);
            List<Review> reviews = hotelService.getReviewsByUuid(uuid);
            return ResponseEntity.ok(reviews);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{uuid}/rating")
    public ResponseEntity<BigDecimal> getRatingByHotelUuid(@PathVariable String uuid) {
        try {
            BigDecimal rating = hotelService.getRatingByUuid(uuid);
            return ResponseEntity.ok(rating);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
