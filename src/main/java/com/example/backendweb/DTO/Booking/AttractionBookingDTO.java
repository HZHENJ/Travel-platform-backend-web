package com.example.backendweb.DTO.Booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @ClassName AttractionBookingDTO
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/2/7
 * @Version 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttractionBookingDTO {
    private Integer attractionBookingId;
    private Integer bookingId;
    private Integer attractionId;
    private String attractionUuid;
    private LocalDate visitDate;
    private LocalDateTime visitTime;
    private Integer numberOfTickets;
    private String status;
}
