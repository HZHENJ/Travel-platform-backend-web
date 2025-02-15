package com.example.backendweb.Services;

import com.example.backendweb.DTO.Review.ReviewWithUsernameDTO;
import com.example.backendweb.Entity.Info.Attraction;
import com.example.backendweb.DTO.AttractionDTO;
import com.example.backendweb.Entity.Review.Review;
import com.example.backendweb.Entity.Review.ReviewStats;
import com.example.backendweb.Repository.AttractionRepository;
import com.example.backendweb.Repository.Review.ReviewRepository;
import com.example.backendweb.Repository.Review.ReviewStatsRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AttractionService {

    private final AttractionRepository attractionRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewStatsRepository reviewStatsRepository;

    public AttractionService(AttractionRepository attractionRepository,
                             ReviewRepository reviewRepository, ReviewStatsRepository reviewStatsRepository) {
        this.attractionRepository = attractionRepository;
        this.reviewRepository = reviewRepository;
        this.reviewStatsRepository = reviewStatsRepository;
    }

    public List<Attraction> saveAttractions(List<AttractionDTO> attractionDTOs) {
        List<Attraction> attractions = new ArrayList<>();
        List<Attraction> newAttractions = new ArrayList<>();

        for (AttractionDTO dto : attractionDTOs) {
            Attraction attraction = new Attraction();
            attraction.setAttractionName(dto.getName());
            attraction.setLocation(dto.getLocation().get("latitude") + ", " + dto.getLocation().get("longitude"));
            attraction.setDescription(dto.getDescription());
            attraction.setTicketType(dto.getPricing());
            attraction.setOpeningHours(convertBusinessHour(dto.getBusinessHour()));
            attraction.setTicketAvailability(generateRandomTicketAvailability(dto.getPricing()));
            attraction.setUuid(dto.getUuid()); // 设置uuid字段

            attractions.add(attraction);

            // 检查数据库中是否已经存在相同的记录
            if (!attractionRepository.existsByUuid(dto.getUuid())) {
                newAttractions.add(attraction);
            } else {
                System.out.println("记录已存在，跳过插入: " + dto.getUuid());
            }
        }

        if (!newAttractions.isEmpty()) {
            attractionRepository.saveAll(newAttractions);
        }

        // 返回完整的Attraction列表
        return attractions;
    }

    private Map<String, String> convertBusinessHour(List<Map<String, Object>> businessHour) {
        Map<String, String> openingHours = new HashMap<>();
        for (Map<String, Object> hour : businessHour) {
            String day = (String) hour.get("day");
            String openTime = (String) hour.get("openTime");
            String closeTime = (String) hour.get("closeTime");
            openingHours.put(day, openTime + " - " + closeTime);
        }
        return openingHours;
    }

    private Map<String, Integer> generateRandomTicketAvailability(Map<String, String> ticketType) {
        Map<String, Integer> ticketAvailability = new HashMap<>();
        Random random = new Random();
        for (String ticket : ticketType.keySet()) {
            ticketAvailability.put(ticket, random.nextInt(100) + 1); // 随机生成1到100之间的票数
        }
        return ticketAvailability;
    }

    public List<Review> getReviewsByUuid(String uuid) {
        // Step 1: 根据 UUID 查询景点
        Attraction attraction = attractionRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Attraction not found for UUID: " + uuid));

        // Step 2: 查询与该景点相关的评论
        return reviewRepository.findByAttractionId(attraction.getAttractionId(), Review.ItemType.Attraction);
    }

    public List<ReviewWithUsernameDTO> getReviewsWithUsernameByUuid(String uuid) {
        // Step 1: 根据 UUID 查询景点
        Attraction attraction = attractionRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Attraction not found for UUID: " + uuid));

        // Step 2: 查询与景点相关的评论和用户名
        List<Object[]> rawResults = reviewRepository.findReviewsWithUsernamesByAttractionId(attraction.getAttractionId());

        // Step 3: 转换为 DTO
        return rawResults.stream()
                .map(result -> {
                    Review review = (Review) result[0];
                    String username = (String) result[1];
                    return new ReviewWithUsernameDTO(
                            review.getReviewId(),
                            username,
                            review.getRating(),
                            review.getComment()
                    );
                })
                .collect(Collectors.toList());
    }

    public BigDecimal getRatingByUuid(String uuid) {
        // Step 1: 根据 UUID 查询景点
        Attraction attraction = attractionRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Attraction not found for UUID: " + uuid));

        // Step 2: 查询与该景点相关的评分
        return reviewStatsRepository.findByItemIdAndItemType(attraction.getAttractionId(), ReviewStats.ItemType.Attraction)
                .map(ReviewStats::getAverageRating)
                .orElse(BigDecimal.valueOf(0.0));
    }
}