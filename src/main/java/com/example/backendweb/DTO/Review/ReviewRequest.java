package com.example.backendweb.DTO.Review;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName ReviewRequest
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/2/7
 * @Version 1.0
 */

@Data
public class ReviewRequest {
    private Integer userId;
    private String itemType; // Attraction / Hotel
    private Integer itemId;  // Attraction ID / Hotel ID
    private Integer bookingId; // booking Id
    private BigDecimal rating;
    private String comment;
}
