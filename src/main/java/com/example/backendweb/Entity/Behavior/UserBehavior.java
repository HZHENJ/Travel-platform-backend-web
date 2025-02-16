package com.example.backendweb.Entity.Behavior;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @ClassName UserBehavior
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/1/25
 * @Version 1.0
 */

@Entity
@Table(name = "user_behaviors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBehavior {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer behaviorId; // Primary Key for behavioral records

    @Column(nullable = false)
    private Integer userId; // User ID (foreign key)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionType actionType; // Type of behavior (Click, View, Book, Search)

    @Column(nullable = false)
    private Integer actionTarget; // Behavioral Target ID (e.g. Recommended Content)

    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime actionTime; // Behavioral Target ID (e.g. Recommended Content)

    @Column(nullable = true)
    private Integer duration; // User Dwell Time (in seconds)

    public enum ActionType {
        Click, View, Book, Search
    }
}
