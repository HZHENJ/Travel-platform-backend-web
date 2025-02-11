package com.example.backendweb.DTO.Booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @ClassName HotelBookingRequest
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/2/11
 * @Version 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelBookingRequest {
    private Integer userId;
    private String uuid; // Hotel UUID
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String roomType;
    private Integer guests;
    private Double price;
}
