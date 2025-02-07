package com.example.backendweb.Services.Review;

import com.example.backendweb.DTO.Review.ReviewRequest;
import com.example.backendweb.Entity.Review.Review;
import com.example.backendweb.Entity.Review.ReviewStats;
import com.example.backendweb.Repository.Review.ReviewRepository;
import com.example.backendweb.Repository.Review.ReviewStatsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewStatsService reviewStatsService;

    private final ReviewStatsRepository reviewStatsRepository;

    /**
     * Constructor for ReviewService.
     *
     * @param reviewRepository Repository for handling Review entities
     * @param reviewStatsService Service for handling review statistics
     */
    public ReviewService(ReviewRepository reviewRepository, ReviewStatsService reviewStatsService,
                         ReviewStatsRepository reviewStatsRepository) {
        this.reviewRepository = reviewRepository;
        this.reviewStatsService = reviewStatsService;
        this.reviewStatsRepository = reviewStatsRepository;
    }

    /**
     * Retrieves all reviews from the database.
     *
     * @return List of all Review entities
     */
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    /**
     * Retrieves a specific review by its ID.
     *
     * @param id The ID of the review to retrieve
     * @return Optional containing the Review if found, empty otherwise
     */
    public Optional<Review> getReviewById(Integer id) {
        return reviewRepository.findById(id);
    }

    /**
     * Retrieves reviews for a specific item based on item ID and type.
     *
     * @param itemId The ID of the item
     * @param itemType The type of the item
     * @return List of Review entities for the specified item
     */
    public List<Review> getReviewsByItem(Integer itemId, Review.ItemType itemType) {
        return reviewRepository.findByItemIdAndItemType(itemId, itemType);
    }

    /**
     * Updates an existing review and updates the review statistics.
     *
     * @param id The ID of the review to update
     * @param review The updated Review entity
     * @return Optional containing the updated Review if found and updated, empty otherwise
     */
    public Optional<Review> updateReview(Integer id, Review review) {
        return reviewRepository.findById(id).map(existingReview -> {
            existingReview.setUserId(review.getUserId());
            existingReview.setItemType(review.getItemType());
            existingReview.setItemId(review.getItemId());
            existingReview.setRating(review.getRating());
            existingReview.setComment(review.getComment());
            existingReview.setStatus(review.getStatus());
            Review updatedReview = reviewRepository.save(existingReview);
            List<Review> reviews = reviewRepository.findByItemIdAndItemType(review.getUserId(),review.getItemType());
            reviewStatsService.updateReviewStats(reviews);
            return updatedReview;
        });
    }

    /**
     * Deletes a review by its ID and updates the review statistics.
     *
     * @param id The ID of the review to delete
     * @return true if the review was found and deleted, false otherwise
     */
    public boolean deleteReview(Integer id) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        if (reviewOptional.isPresent()) {
            Review review = reviewOptional.get();
            reviewRepository.deleteById(id);
            List<Review> reviews = reviewRepository.findByItemIdAndItemType(review.getUserId(),review.getItemType());
            reviewStatsService.updateReviewStats(reviews);
            return true;
        }
        return false;
    }

    @Transactional
    public Review createReview(ReviewRequest request) {
        // Step 1: 存储 Review
        Review review = Review.builder()
                .userId(request.getUserId())
                .itemType(Review.ItemType.valueOf(request.getItemType()))
                .itemId(request.getItemId())
                .rating(request.getRating())
                .comment(request.getComment())
                .status(Review.ReviewStatus.show) // 直接显示评论
                .build();
        reviewRepository.save(review);

        // Step 2: 更新 ReviewStats
        Optional<ReviewStats> existingStats = reviewStatsRepository.findByItemIdAndItemType(
                request.getItemId(), ReviewStats.ItemType.valueOf(request.getItemType())
        );

        if (existingStats.isPresent()) {
            ReviewStats stats = existingStats.get();
            stats.setTotalReviews(stats.getTotalReviews() + 1);
            stats.setAverageRating(
                    stats.getAverageRating().multiply(BigDecimal.valueOf(stats.getTotalReviews()))
                            .add(request.getRating())
                            .divide(BigDecimal.valueOf(stats.getTotalReviews() + 1), 1, BigDecimal.ROUND_HALF_UP)
            );
            reviewStatsRepository.save(stats);
        } else {
            ReviewStats newStats = ReviewStats.builder()
                    .itemType(ReviewStats.ItemType.valueOf(request.getItemType()))
                    .itemId(request.getItemId())
                    .totalReviews(1)
                    .averageRating(request.getRating())
                    .build();
            reviewStatsRepository.save(newStats);
        }

        return review;
    }

    // 检查用户是否已评论
    public boolean hasUserReviewed(Integer userId, Integer itemId) {
        return reviewRepository.existsByUserIdAndItemId(userId, itemId);
    }
}
