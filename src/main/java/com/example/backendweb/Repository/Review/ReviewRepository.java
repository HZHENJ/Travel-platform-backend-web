package com.example.backendweb.Repository.Review;

import com.example.backendweb.Entity.Review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @ClassName ReviewRepository
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/2/5
 * @Version 1.0
 */
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByUserId(Integer userId);
    List<Review> findByItemIdAndItemType(Integer itemId, Review.ItemType itemType);

    /**
     * 判断某个 `Booking` 是否已被该 `User` 评论
     */
    @Query("SELECT COUNT(r) > 0 FROM Review r WHERE r.userId = :userId AND r.itemId = :itemId AND r.itemType = 'Attraction' AND r.status = 'show'")
    boolean hasUserReviewedBooking(@Param("userId") Integer userId, @Param("itemId") Integer itemId);

    @Query("SELECT r FROM Review r WHERE r.bookingId = :bookingId")
    Optional<Review> findByBookingId(@Param("bookingId") Integer bookingId);

    @Query("SELECT r FROM Review r WHERE r.itemId = :itemId AND r.itemType = :itemType")
    List<Review> findByAttractionId(@Param("itemId") Integer itemId, @Param("itemType") Review.ItemType itemType);

    @Query("SELECT r FROM Review r WHERE r.itemId = :itemId AND r.itemType = :itemType")
    List<Review> findByHotelId(@Param("itemId") Integer itemId, @Param("itemType") Review.ItemType itemType);

    @Query("SELECT r, a.username FROM Review r JOIN Authentication a ON r.userId = a.user.userId WHERE r.itemId = :itemId AND r.itemType = 'Attraction'")
    List<Object[]> findReviewsWithUsernamesByAttractionId(@Param("itemId") Integer itemId);

    Optional<Review> findByUserIdAndItemIdAndItemType(Integer userId, Integer itemId, Review.ItemType itemType);
}
