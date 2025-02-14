package com.example.backendweb.DTO.Booking;

import lombok.Data;

@Data
public class FlightBookingRequest {
    private Long userId;
    private String selectedSeats;
    private Integer id;
    private String type;
    private Double totalPrice;
}
