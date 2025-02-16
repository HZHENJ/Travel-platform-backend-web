package com.example.backendweb.DTO.Booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @ClassName AttractionBookingRequest
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/2/6
 * @Version 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttractionBookingRequest {
    private Integer userId;             // user ID
    private String uuid;                // Attractions UUID
    private LocalDate visitDate;        // Date of visit
    private LocalDateTime visitTime;    // Visiting time
    private Integer numberOfTickets;    // Visiting time
    private Double price;               // prices
}
