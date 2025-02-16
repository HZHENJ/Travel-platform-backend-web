package com.example.backendweb.Entity.Booking;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @ClassName AttractionBooking
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/1/25
 * @Version 1.0
 */

@Entity
@Table(name = "attraction_bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttractionBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer attractionBookingId; // Primary Key for Attraction Reservations

    @Column(nullable = true)
    private Integer bookingId; // Booking ID (foreign key)

    @Column(nullable = false)
    private Integer attractionId; // Attraction ID (foreign key)

    @Column(nullable = false)
    private LocalDate visitDate; // Date of visit

    @Column(nullable = false)
    private LocalDateTime visitTime; // Visiting time

    @Column(length = 50)
    private String ticketType; // Ticket type

    @Column(nullable = false)
    private Integer numberOfTickets; // Number of tickets
}
