package com.example.backendweb.Services.Recommandation;

import com.example.backendweb.Entity.Review.Review;
import com.example.backendweb.Repository.Review.ReviewRepository;
import com.example.backendweb.Util.CosineSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName SimilarityService
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/2/5
 * @Version 1.0
 */

@Service
public class SimilarityService {

    @Autowired
    private ReviewRepository reviewRepository;


    // Calculate the similarity between two users (based on cosine similarity)
    public double calculateUserSimilarity(Integer userId1, Integer userId2) {

        // 获取两个用户的评分数据
        List<Review> user1Reviews = reviewRepository.findByUserId(userId1);
        List<Review> user2Reviews = reviewRepository.findByUserId(userId2);

        // 只计算景点 (Attraction) 评分相似度
        Map<Long, Double> user1Ratings = user1Reviews.stream()
                .filter(r -> r.getItemType() == Review.ItemType.Attraction)
                .collect(Collectors.toMap(
                        r -> r.getItemId().longValue(),
                        r -> r.getRating().doubleValue(),
                        (oldValue, newValue) -> (oldValue + newValue) / 2 // 取平均值
                ));

        Map<Long, Double> user2Ratings = user2Reviews.stream()
                .filter(r -> r.getItemType() == Review.ItemType.Attraction)
                .collect(Collectors.toMap(
                        r -> r.getItemId().longValue(),
                        r -> r.getRating().doubleValue(),
                        (oldValue, newValue) -> (oldValue + newValue) / 2 // 取平均值
                ));

        // 调用余弦相似度计算
        return CosineSimilarity.calculate(user1Ratings, user2Ratings);
    }
}
