package com.example.backendweb.Services;

import com.example.backendweb.Entity.Info.Hotel;
import com.example.backendweb.Entity.Review.Review;
import com.example.backendweb.Entity.Review.ReviewStats;
import com.example.backendweb.Repository.HotelRepository;
import com.example.backendweb.Repository.Review.ReviewRepository;
import com.example.backendweb.Repository.Review.ReviewStatsRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @ClassName HotelService
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/2/13
 * @Version 1.0
 */

@Service
public class HotelService {
    private final HotelRepository hotelRepository;

    private final ReviewRepository reviewRepository;

    private final ReviewStatsRepository reviewStatsRepository;

    public HotelService(
            HotelRepository hotelRepository,
            ReviewRepository reviewRepository,
            ReviewStatsRepository reviewStatsRepository
    ) {
        this.hotelRepository = hotelRepository;
        this.reviewRepository = reviewRepository;
        this.reviewStatsRepository = reviewStatsRepository;
    }

    public List<Review> getReviewsByUuid(String uuid) {
        // 根据uuid查询景点
        Hotel hotel = hotelRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Hotel not found for UUID: " + uuid));

        // 查询与该景点相关的评论
        return reviewRepository.findByHotelId(hotel.getHotelId(), Review.ItemType.Hotel);
    }

    public Optional<Map<String, Object>> getHotelReviewStats(String uuid) {
        Optional<Hotel> hotelOpt = hotelRepository.findByUuid(uuid);
        if (hotelOpt.isEmpty()) {
            return Optional.empty(); // Hotel 不存在
        }

        Hotel hotel = hotelOpt.get();
        Optional<ReviewStats> statsOpt = reviewStatsRepository.findByItemTypeAndItemId(ReviewStats.ItemType.Hotel, hotel.getHotelId());

        if (statsOpt.isPresent()) {
            ReviewStats stats = statsOpt.get();
            Map<String, Object> result = new HashMap<>();
            result.put("totalReviews", stats.getTotalReviews());
            result.put("averageRating", stats.getAverageRating());
            return Optional.of(result);
        }

        return Optional.of(Map.of("totalReviews", 0, "averageRating", BigDecimal.ZERO));
    }


    public BigDecimal getRatingByUuid(String uuid) {
        // 根据uuid查询景点
        Hotel hotel = hotelRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Hotel not found for UUID: " + uuid));

        // 查询与该景点相关的评分
        Optional<ReviewStats> reviewStats = reviewStatsRepository.findByItemIdAndItemType( hotel.getHotelId(), ReviewStats.ItemType.Hotel);

        if (reviewStats.isPresent()) {
            return reviewStats.get().getAverageRating();
        } else {
            return BigDecimal.ZERO;
        }
    }
}
