package com.example.backendweb.Services;

import com.example.backendweb.DTO.AttractionBookingRequest;
import com.example.backendweb.Entity.Booking.AttractionBooking;
import com.example.backendweb.Entity.Booking.Booking;
import com.example.backendweb.Entity.Info.Attraction;
import com.example.backendweb.Repository.AttractionRepository;
import com.example.backendweb.Repository.Booking.AttractionBookingRepository;
import com.example.backendweb.Repository.Booking.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @ClassName BookingService
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/2/6
 * @Version 1.0
 */

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private AttractionBookingRepository attractionBookingRepository;

    @Autowired
    private AttractionRepository attractionRepository;

    @Transactional
    public AttractionBooking createAttractionBooking(AttractionBookingRequest request) {
        // Step 1: 验证景点信息
        Attraction attraction = attractionRepository.findByUuid(request.getUuid())
                .orElseThrow(() -> new IllegalArgumentException("Invalid attraction UUID: " + request.getUuid()));

        // Step 2: 创建通用 Booking 记录
        Booking booking = Booking.builder()
                .userId(request.getUserId())
                .bookingType(Booking.BookingType.Attraction)
                .status(Booking.BookingStatus.Pending)
                .totalAmount(calculateTotalAmount(attraction.getTicketType(), request.getTicketType(), request.getNumberOfTickets()))
                .build();
        booking = bookingRepository.save(booking);

        // Step 3: 创建具体的 AttractionBooking 记录
        AttractionBooking attractionBooking = AttractionBooking.builder()
                .bookingId(booking.getBookingId())
                .attractionId(attraction.getAttractionId())
                .visitDate(request.getVisitDate())
                .ticketType(request.getTicketType())
                .numberOfTickets(request.getNumberOfTickets())
                .build();
        attractionBookingRepository.save(attractionBooking);

        return attractionBooking;
    }

    // 计算总金额
    private BigDecimal calculateTotalAmount(Map<String, String> ticketPrices, String ticketType, Integer numberOfTickets) {
        String priceString = ticketPrices.get(ticketType);
        if (priceString == null) {
            throw new IllegalArgumentException("Invalid ticket type: " + ticketType);
        }

        BigDecimal pricePerTicket = new BigDecimal(priceString);
        return pricePerTicket.multiply(new BigDecimal(numberOfTickets));
    }

}
