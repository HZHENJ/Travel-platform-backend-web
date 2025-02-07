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
    private String itemType; // "Attraction"
    private Integer itemId;  // AttractionBooking ID
    private BigDecimal rating;
    private String comment;
}
