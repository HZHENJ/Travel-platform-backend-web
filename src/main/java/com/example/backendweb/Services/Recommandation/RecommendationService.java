package com.example.backendweb.Services.Recommandation;

import com.example.backendweb.Entity.Info.Attraction;
import com.example.backendweb.Entity.Review.Review;
import com.example.backendweb.Entity.Review.ReviewStats;
import com.example.backendweb.Entity.User.User;
import com.example.backendweb.Repository.AttractionRepository;
import com.example.backendweb.Repository.Review.ReviewRepository;
import com.example.backendweb.Repository.Review.ReviewStatsRepository;
import com.example.backendweb.Repository.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName RecommendationService
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/2/5
 * @Version 1.0
 */

@Service
public class RecommendationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private AttractionRepository attractionRepository;

    @Autowired
    private ReviewStatsRepository reviewStatsRepository;

    @Autowired
    private SimilarityService similarityService;

    /**
    * Get popular attractions (based on average ratings)
    */
    public List<Attraction> getPopularAttractions() {
        List<ReviewStats> topAttractions = reviewStatsRepository.findByItemTypeOrderByAverageRatingDesc(ReviewStats.ItemType.Attraction);
        return topAttractions.stream()
                .limit(10) // Take the top 10 popular attractions
                .map(stat -> attractionRepository.findById(stat.getItemId()).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Calculate user personalized recommendations (based on similar users)
     */
    public List<Attraction> getPersonalizedRecommendations(Integer userId) {
        List<User> allUsers = userRepository.findAll();
        Map<Integer, Double> similarityScores = new HashMap<>();

        // 计算相似用户
        for (User user : allUsers) {
            if (!user.getUserId().equals(userId)) {
                double similarity = similarityService.calculateUserSimilarity(userId, user.getUserId());
                similarityScores.put(user.getUserId(), similarity);
            }
        }

        // 获取用户已评分的景点（排除 `hide` 状态）
        List<Review> userReviews = reviewRepository.findByUserId(userId)
                .stream()
                .filter(r -> r.getItemType() == Review.ItemType.Attraction && r.getStatus() == Review.ReviewStatus.show)
                .collect(Collectors.toList());

        Set<Integer> visitedAttractions = userReviews.stream()
                .map(Review::getItemId)
                .collect(Collectors.toSet());

        Map<Integer, Double> attractionScores = new HashMap<>();
        Map<Integer, Integer> attractionReviewCounts = new HashMap<>();

        // 遍历相似用户，计算推荐分数
        for (Map.Entry<Integer, Double> entry : similarityScores.entrySet()) {
            Integer otherUserId = entry.getKey();
            Double similarity = entry.getValue();

            // 获取相似用户的 `show` 状态景点评价
            List<Review> otherUserReviews = reviewRepository.findByUserId(otherUserId)
                    .stream()
                    .filter(r -> r.getItemType() == Review.ItemType.Attraction && r.getStatus() == Review.ReviewStatus.show)
                    .collect(Collectors.toList());

            // 计算推荐分数
            for (Review review : otherUserReviews) {
                int attractionId = review.getItemId();

                // 排除用户已访问的景点
                if (!visitedAttractions.contains(attractionId)) {
                    attractionScores.merge(attractionId, similarity * review.getRating().doubleValue(), Double::sum);
                    attractionReviewCounts.merge(attractionId, 1, Integer::sum);
                }
            }
        }

        // 计算平均评分，避免重复 key
        Map<Integer, Double> finalScores = attractionScores.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue() / attractionReviewCounts.getOrDefault(entry.getKey(), 1), // 避免除以 0
                        Double::sum // 处理重复 key
                ));

        // 返回推荐景点（按评分排序，取前 10 个）
        return finalScores.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(10)
                .map(entry -> attractionRepository.findById(entry.getKey()).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Comprehensive recommendations (combine popular and personalized recommendations)
     */
    public List<Attraction> getCombinedRecommendations(Integer userId) {
        List<Attraction> popular = getPopularAttractions();
        List<Attraction> personalized = getPersonalizedRecommendations(userId);

        Set<Attraction> combined = new LinkedHashSet<>();
        combined.addAll(personalized); // Add personalized recommendations first
        combined.addAll(popular); // Add popular recommendations (to avoid duplication)

        return new ArrayList<>(combined).subList(0, Math.min(combined.size(), 10));
    }

}
