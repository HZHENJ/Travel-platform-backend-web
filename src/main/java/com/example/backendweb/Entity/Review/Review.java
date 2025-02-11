package com.example.backendweb.Entity.Review;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName Review
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/1/25
 * @Version 1.0
 */

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reviewId; // 评论唯一标识 (Primary Key)

    @Column(nullable = false)
    private Integer userId; // 用户ID (外键)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemType itemType; // 评价对象类型 (酒店或景点)

    @Column(nullable = false)
    private Integer itemId; // 评价对象ID (酒店ID、景点ID)

    @Column(nullable = false)
    private Integer bookingId; // 一个booking对应一条review

    @Column(nullable = false, precision = 2, scale = 1)
    private BigDecimal rating; // 评分

    @Column(columnDefinition = "TEXT")
    private String comment; // 评论内容

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewStatus status; // 评论状态 (显示或隐藏)

    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt; // 创建时间

    @Column(nullable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt; // 更新时间

    public enum ItemType {
        Flight, Hotel, Attraction
    }

    public enum ReviewStatus {
        hide, show
    }

}
