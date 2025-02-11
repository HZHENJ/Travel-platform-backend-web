package com.example.backendweb.DTO.Booking;

import lombok.Data;

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
public class HotelBookingDTO {
    private Integer bookingId;
    private String hotelName;
    private String location;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String roomType;
    private Integer guests;
    private Double totalAmount;
}
