package com.example.backendweb.Controller;

import com.example.backendweb.Services.Review.ReviewStatsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

/**
 * @ClassName ReviewStatsController
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/2/14
 * @Version 1.0
 */

@RestController
@RequestMapping("/api/reviews")
public class ReviewStatsController {
    private final ReviewStatsService reviewStatsService;

    public ReviewStatsController(ReviewStatsService reviewStatsService) {
        this.reviewStatsService = reviewStatsService;
    }

    @GetMapping("/attraction/{uuid}")
    public ResponseEntity<?> getAttractionRating(@PathVariable String uuid) {
        Optional<Map<String, Object>> result = reviewStatsService.getAttractionReviewStats(uuid);

        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Attraction not found"));
        }
    }

    // 查询酒店评分 & 评价数
    @GetMapping("/hotel/{uuid}")
    public ResponseEntity<?> getHotelRating(@PathVariable String uuid) {
        Optional<Map<String, Object>> result = reviewStatsService.getHotelReviewStats(uuid);

        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Hotel not found"));
        }
    }
}
