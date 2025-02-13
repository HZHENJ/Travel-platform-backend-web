package com.example.backendweb.Services;

import com.example.backendweb.Entity.Info.Hotel;
import com.example.backendweb.Entity.Review.Review;
import com.example.backendweb.Repository.HotelRepository;
import com.example.backendweb.Repository.Review.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public HotelService(
            HotelRepository hotelRepository,
            ReviewRepository reviewRepository
    ) {
        this.hotelRepository = hotelRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<Review> getReviewsByUuid(String uuid) {
        // 根据uuid查询景点
        Hotel hotel = hotelRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Hotel not found for UUID: " + uuid));

        // 查询与该景点相关的评论
        return reviewRepository.findByHotelId(hotel.getHotelId(), Review.ItemType.Hotel);
    }
}
