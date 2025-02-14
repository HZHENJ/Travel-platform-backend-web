package com.example.backendweb.Services.Review;

import com.example.backendweb.DTO.Review.ReviewRequest;
import com.example.backendweb.Entity.Review.Review;
import com.example.backendweb.Entity.Review.ReviewStats;
import com.example.backendweb.Repository.Review.ReviewRepository;
import com.example.backendweb.Repository.Review.ReviewStatsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
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

    // @Transactional
    // public Review createReview(ReviewRequest request) {
    //     // Step 1: 查找现有 Review
    //     Optional<Review> existingReview = reviewRepository.findByBookingId(request.getBookingId());
    //
    //     Review review;
    //     if (existingReview.isPresent()) {
    //         // 已存在 Review，更新数据
    //         review = existingReview.get();
    //         review.setRating(request.getRating());
    //         review.setComment(request.getComment());
    //         review.setStatus(Review.ReviewStatus.show); // 使评论可见
    //         review.setUpdatedAt(LocalDateTime.now()); // 手动更新时间
    //     } else {
    //         // 如果 `Review` 不存在，创建新 Review
    //         review = Review.builder()
    //                 .userId(request.getUserId())
    //                 .bookingId(request.getBookingId()) // 确保 `bookingId` 赋值正确
    //                 .itemType(Review.ItemType.valueOf(request.getItemType()))
    //                 .itemId(request.getItemId())
    //                 .rating(request.getRating())
    //                 .comment(request.getComment())
    //                 .status(Review.ReviewStatus.show) // 直接显示评论
    //                 .createdAt(LocalDateTime.now())
    //                 .updatedAt(LocalDateTime.now())
    //                 .build();
    //     }
    //
    //     reviewRepository.save(review); // 保存 Review
    //
    //     // Step 2: 更新 ReviewStats
    //     Optional<ReviewStats> existingStats = reviewStatsRepository.findByItemIdAndItemType(
    //             request.getItemId(), ReviewStats.ItemType.valueOf(request.getItemType())
    //     );
    //
    //     if (existingStats.isPresent()) {
    //         ReviewStats stats = existingStats.get();
    //
    //         // 计算新的总评分
    //         BigDecimal newTotalRating = stats.getAverageRating().multiply(BigDecimal.valueOf(stats.getTotalReviews()))
    //                 .add(request.getRating()) // 添加新评分
    //                 .divide(BigDecimal.valueOf(stats.getTotalReviews() + 1), 1, BigDecimal.ROUND_HALF_UP);
    //
    //         stats.setTotalReviews(stats.getTotalReviews() + 1);
    //         stats.setAverageRating(newTotalRating);
    //         reviewStatsRepository.save(stats);
    //     }
    //     else {
    //         // 如果没有 ReviewStats，创建新统计记录
    //         ReviewStats newStats = ReviewStats.builder()
    //                 .itemType(ReviewStats.ItemType.valueOf(request.getItemType()))
    //                 .itemId(request.getItemId())
    //                 .totalReviews(1)
    //                 .averageRating(request.getRating())
    //                 .build();
    //         reviewStatsRepository.save(newStats);
    //     }
    //
    //     return review;
    // }
    @Transactional
    public Review createReview(ReviewRequest request) {
        try {
            log.info("Received review request: {}", request);

            // Step 1: 查找现有 Review
            Optional<Review> existingReview = reviewRepository.findByBookingId(request.getBookingId());

            Review review;
            if (existingReview.isPresent()) {
                review = existingReview.get();
                log.info("Updating existing review: {}", review);

                review.setRating(request.getRating());
                review.setComment(request.getComment());
                review.setStatus(Review.ReviewStatus.show);
                review.setUpdatedAt(LocalDateTime.now());
            } else {
                log.info("Creating new review for bookingId: {}", request.getBookingId());

                review = Review.builder()
                        .userId(request.getUserId())
                        .bookingId(request.getBookingId())
                        .itemType(Review.ItemType.valueOf(request.getItemType()))
                        .itemId(request.getItemId())
                        .rating(request.getRating())
                        .comment(request.getComment())
                        .status(Review.ReviewStatus.show)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();
            }

            reviewRepository.save(review);
            log.info("Review saved successfully: {}", review);

            // Step 2: 查找 ReviewStats（只查找，不创建）
            Optional<ReviewStats> existingStats = reviewStatsRepository.findByItemIdAndItemType(
                    request.getItemId(), ReviewStats.ItemType.valueOf(request.getItemType()));

            if (existingStats.isPresent()) {
                // 如果存在 ReviewStats，则更新
                ReviewStats stats = existingStats.get();
                log.info("Updating ReviewStats: {}", stats);

                BigDecimal newTotalRating = stats.getAverageRating()
                        .multiply(BigDecimal.valueOf(stats.getTotalReviews()))
                        .add(request.getRating())
                        .divide(BigDecimal.valueOf(stats.getTotalReviews() + 1), 1, BigDecimal.ROUND_HALF_UP);

                stats.setTotalReviews(stats.getTotalReviews() + 1);
                stats.setAverageRating(newTotalRating);
                reviewStatsRepository.save(stats);
                log.info("ReviewStats updated successfully: {}", stats);
            } else {
                // **如果不存在 ReviewStats，则不创建**
                log.info("No existing ReviewStats found for itemId={}, skipping creation.", request.getItemId());
            }

            return review;
        } catch (Exception e) {
            log.error("Error processing review request: {}", request, e);
            throw new RuntimeException("Failed to process review request", e);
        }
    }

}
