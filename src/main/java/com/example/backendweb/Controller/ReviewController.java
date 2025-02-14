package com.example.backendweb.Controller;

import com.example.backendweb.Entity.Review.Review;
import com.example.backendweb.Services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public List<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<Review> updateReview(@PathVariable Integer reviewId, @RequestBody Review review) {
        Review updatedReview = reviewService.updateReview(reviewId, review);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Integer reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{reviewId}/reply")
    public ResponseEntity<Review> replyToReview(
            @PathVariable Integer reviewId,
            @RequestBody Map<String, String> requestBody) {
        String reply = requestBody.get("reply");
        Review updatedReview = reviewService.addReplyToReview(reviewId, reply);
        return ResponseEntity.ok(updatedReview);
    }

    @GetMapping("/satisfaction")
    public Map<String, Long> getReviewSatisfaction() {
        return reviewService.getReviewSatisfaction();
    }
}
