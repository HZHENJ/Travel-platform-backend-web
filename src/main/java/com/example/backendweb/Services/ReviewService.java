package com.example.backendweb.Services;

import com.example.backendweb.Entity.Review.Review;
import com.example.backendweb.Repository.Review.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final ReviewStatsService reviewStatsService;

    /**
     * Constructor for ReviewService.
     *
     * @param reviewRepository Repository for handling Review entities
     * @param reviewStatsService Service for handling review statistics
     */
    public ReviewService(ReviewRepository reviewRepository, ReviewStatsService reviewStatsService) {
        this.reviewRepository = reviewRepository;
        this.reviewStatsService = reviewStatsService;
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
     * Creates a new review and updates the review statistics.
     *
     * @param review The Review entity to be created
     * @return The saved Review entity
     */
    public Review createReview(Review review) {
        Review savedReview = reviewRepository.save(review);
        List<Review> reviews = reviewRepository.findByItemIdAndItemType(review.getUserId(),review.getItemType());
        reviewStatsService.updateReviewStats(reviews);
        return savedReview;
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
}
