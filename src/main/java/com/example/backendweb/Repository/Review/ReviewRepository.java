package com.example.backendweb.Repository.Review;

import com.example.backendweb.Entity.Review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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
    boolean existsByUserIdAndItemId(Integer userId, Integer itemId);

    @Query("SELECT r FROM Review r WHERE r.itemId = :itemId AND r.itemType = 'Attraction'")
    List<Review> findByAttractionId(@Param("itemId") Integer itemId);
}
