package com.example.backendweb.DTO.Booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @ClassName HotelBookingDTO
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/2/11
 * @Version 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelBookingDTO {
    private Integer hotelBookingId;
    private Integer bookingId;
    private Integer hotelId;
    private String hotelUuid;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String roomType;
    private Integer guests;
    private Double totalAmount;
}
