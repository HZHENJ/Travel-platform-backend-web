package com.example.backendweb.Entity.Booking;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName Booking
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/1/25
 * @Version 1.0
 */

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookingId; // Primary Key

    @Column(nullable = false)
    private Integer userId; // User ID (foreign key)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingType bookingType; // Booking Type (Flight, Hotel or Attraction)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status; // Booking Status

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount; // total amount

    // @Column(nullable = true)
    // private Integer paymentId; // Payment ID (foreign key)

    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt; // 创建时间

    @Column(nullable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt; // 更新时间

    public enum BookingType {
        Flight, Hotel, Attraction
    }

    public enum BookingStatus {
        Confirmed, Canceled, Pending
    }
}
