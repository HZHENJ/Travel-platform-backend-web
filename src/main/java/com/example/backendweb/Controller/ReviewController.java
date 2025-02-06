package com.example.backendweb.Controller;

import com.example.backendweb.Entity.Review.Review;
import com.example.backendweb.Services.Review.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    // 获取所有评论
    @GetMapping
    public List<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }

    // 获取特定评论
    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Integer id) {
        Optional<Review> review = reviewService.getReviewById(id);
        return review.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 根据 itemId 和 itemType 查找评论
    @GetMapping("/search")
    public List<Review> getReviewsByItem(
            @RequestParam Integer itemId,
            @RequestParam Review.ItemType itemType) {
        return reviewService.getReviewsByItem(itemId, itemType);
    }



    // 创建评论
    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        Review createdReview = reviewService.createReview(review);
        return ResponseEntity.status(201).body(createdReview);
    }



    // 更新评论
    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable Integer id, @RequestBody Review review) {
        Optional<Review> updatedReview = reviewService.updateReview(id, review);
        return updatedReview.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 删除评论
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Integer id) {
        if (reviewService.deleteReview(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
