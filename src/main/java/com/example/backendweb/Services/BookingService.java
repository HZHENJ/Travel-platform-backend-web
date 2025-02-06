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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    private final BookingRepository bookingRepository;
    private final AttractionBookingRepository attractionBookingRepository;

    private final AttractionRepository attractionRepository;

    public BookingService(BookingRepository bookingRepository,
                          AttractionBookingRepository attractionBookingRepository,
                          AttractionRepository attractionRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.attractionBookingRepository = attractionBookingRepository;
        this.attractionRepository = attractionRepository;
    }

    public List<AttractionBooking> getAttractionBookingsByUserId(Integer userId) {
        // 1. 查询该用户的所有 Booking
        List<Booking> userBookings = bookingRepository.findByUserId(userId);

        // 2. 提取 bookingId
        List<Integer> bookingIds = userBookings.stream()
                .map(Booking::getBookingId)
                .collect(Collectors.toList());

        // 3. 查询所有景点预订
        return attractionBookingRepository.findByBookingIdIn(bookingIds);
    }

    @Transactional
    public AttractionBooking createAttractionBooking(AttractionBookingRequest request) {
        // Step 1: 验证景点信息
        Attraction attraction = attractionRepository.findByUuid(request.getUuid())
                .orElseGet(() -> {
                    Attraction newAttraction = Attraction.builder()
                            .uuid(request.getUuid())
                            .build();
                    return attractionRepository.save(newAttraction);
                });

        // Step 2: 创建通用 Booking 记录
        Booking booking = Booking.builder()
                .userId(request.getUserId())
                .bookingType(Booking.BookingType.Attraction)
                .status(Booking.BookingStatus.Pending)
                .totalAmount(new BigDecimal(request.getPrice()))
                .build();
        booking = bookingRepository.save(booking);

        // Step 3: 创建具体的 AttractionBooking 记录
        AttractionBooking attractionBooking = AttractionBooking.builder()
                .bookingId(booking.getBookingId())
                .attractionId(attraction.getAttractionId())
                .visitDate(request.getVisitDate())
                .visitTime(request.getVisitTime())
                .numberOfTickets(request.getNumberOfTickets())
                .build();
        attractionBookingRepository.save(attractionBooking);

        return attractionBooking;
    }
}
