package com.example.backendweb.Entity.Behavior;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

/**
 * @ClassName SearchHistory
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/1/25
 * @Version 1.0
 */

@Entity
@Table(name = "search_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer searchId; // Primary Key

    @Column(nullable = false)
    private Integer userId; // User ID (foreign key)

    @Column(nullable = false, length = 255)
    private String keywords; // Search terms entered by the user

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SearchType searchType; // Search Type (Hotel or Attraction)

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json", nullable = true)
    private String filters; // Search criteria (JSON format)

    @Column(nullable = false)
    private Integer resultsCount; // Search criteria (JSON format)

    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt; // Number of search results

    public enum SearchType {
        Hotel, Attraction
    }
}
