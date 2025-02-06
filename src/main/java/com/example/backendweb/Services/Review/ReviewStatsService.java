package com.example.backendweb.Services.Review;

import com.example.backendweb.Entity.Review.Review;
import com.example.backendweb.Entity.Review.ReviewStats;
import com.example.backendweb.Repository.Review.ReviewStatsRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewStatsService {

    private final ReviewStatsRepository reviewStatsRepository;

    public ReviewStatsService( ReviewStatsRepository reviewStatsRepository) {
        this.reviewStatsRepository = reviewStatsRepository;
    }



    /**
     * Updates the review statistics for a given list of reviews
     *
     * @param reviews The list of reviews to update statistics for
     */
    public void updateReviewStats(List<Review> reviews) {
        // Get the item ID and type from the first review in the list
        Integer itemId = reviews.get(0).getItemId();
        Review.ItemType itemType = reviews.get(0).getItemType();

        // Calculate the total number of reviews
        int totalReviews = reviews.size();
        // Calculate the average rating for all reviews
        BigDecimal averageRating = calculateAverageRating(reviews);

        // Try to find existing review stats for the item
        Optional<ReviewStats> optionalStats = reviewStatsRepository.findByItemIdAndItemType(itemId, convertItemType(itemType));

        if (optionalStats.isPresent()) {
            // If stats exist, update them
            ReviewStats stats = optionalStats.get();
            stats.setTotalReviews(totalReviews);
            stats.setAverageRating(averageRating);
            reviewStatsRepository.save(stats);
        } else {
            // If stats don't exist, create new ones
            ReviewStats newStats = ReviewStats.builder()
                    .itemId(itemId)
                    .itemType(convertItemType(itemType))
                    .totalReviews(totalReviews)
                    .averageRating(averageRating)
                    .build();
            reviewStatsRepository.save(newStats);
        }
    }


    /**
     * Calculates the average rating from a list of reviews.
     *
     * @param reviews The list of reviews to calculate the average rating from
     * @return The average rating as a BigDecimal, rounded to one decimal place
     */
    private BigDecimal calculateAverageRating(List<Review> reviews) {
        // If there are no reviews, return zero
        if (reviews.isEmpty()) {
            return BigDecimal.ZERO;
        }

        // Sum up all the ratings
        BigDecimal totalRating = reviews.stream()
                .map(Review::getRating)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate the average by dividing the total by the number of reviews
        // Round to one decimal place using HALF_UP rounding mode
        return totalRating.divide(BigDecimal.valueOf(reviews.size()), 1, RoundingMode.HALF_UP);
    }


    // 将 Review.ItemType 转换为 ReviewStats.ItemType
    /**
     * Converts Review.ItemType to ReviewStats.ItemType
     *
     * @param itemType The Review.ItemType to be converted
     * @return The corresponding ReviewStats.ItemType
     * @throws IllegalArgumentException if the itemType is not supported
     */
    private ReviewStats.ItemType convertItemType(Review.ItemType itemType) {
        return switch (itemType) {
            case Hotel -> ReviewStats.ItemType.Hotel;
            case Attraction -> ReviewStats.ItemType.Attraction;
            default -> throw new IllegalArgumentException("Unsupported item type: " + itemType);
        };
    }

}
