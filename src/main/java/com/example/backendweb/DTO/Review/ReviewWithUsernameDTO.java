package com.example.backendweb.DTO.Review;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName ReviewWithUsernameDTO
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/2/10
 * @Version 1.0
 */

@Data
@AllArgsConstructor
public class ReviewWithUsernameDTO {
    private Integer reviewId;
    private String username;
    private BigDecimal rating;
    private String comment;
}
