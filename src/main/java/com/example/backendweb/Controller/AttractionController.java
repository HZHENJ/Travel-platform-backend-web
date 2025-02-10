package com.example.backendweb.Controller;

import com.example.backendweb.DTO.Review.ReviewWithUsernameDTO;
import com.example.backendweb.Entity.Review.Review;
import com.example.backendweb.Services.AttractionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName AttractionController
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/2/10
 * @Version 1.0
 */

@RestController
@RequestMapping("/api/attractions")
public class AttractionController {
    private final AttractionService attractionService;

    public AttractionController(AttractionService attractionService) {
        this.attractionService = attractionService;
    }

    @GetMapping("/{uuid}/reviews")
    public ResponseEntity<List<Review>> getReviewsByAttractionUuid(@PathVariable String uuid) {
        try {
            // List<ReviewWithUsernameDTO> reviews = attractionService.getReviewsWithUsernameByUuid(uuid);
            List<Review> reviews = attractionService.getReviewsByUuid(uuid);
            return ResponseEntity.ok(reviews);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
