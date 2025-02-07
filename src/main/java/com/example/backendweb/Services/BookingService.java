package com.example.backendweb.Services;

import com.example.backendweb.DTO.Booking.AttractionBookingDTO;
import com.example.backendweb.DTO.Booking.AttractionBookingRequest;
import com.example.backendweb.Entity.Booking.AttractionBooking;
import com.example.backendweb.Entity.Booking.Booking;
import com.example.backendweb.Entity.Info.Attraction;
import com.example.backendweb.Entity.Review.Review;
import com.example.backendweb.Entity.Review.ReviewStats;
import com.example.backendweb.Repository.AttractionRepository;
import com.example.backendweb.Repository.Booking.AttractionBookingRepository;
import com.example.backendweb.Repository.Booking.BookingRepository;
import com.example.backendweb.Repository.Review.ReviewStatsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
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
    private final ReviewStatsRepository reviewStatsRepository;

    public BookingService(BookingRepository bookingRepository,
                          AttractionBookingRepository attractionBookingRepository,
                          AttractionRepository attractionRepository,
                          ReviewStatsRepository reviewStatsRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.attractionBookingRepository = attractionBookingRepository;
        this.attractionRepository = attractionRepository;
        this.reviewStatsRepository = reviewStatsRepository;
    }

    public List<AttractionBookingDTO> getAttractionBookingsByUserId(Integer userId) {
        // **1. 查询该用户的所有 Booking**
        List<Booking> userBookings = bookingRepository.findByUserId(userId);

        // **2. 提取 bookingId**
        List<Integer> bookingIds = userBookings.stream()
                .map(Booking::getBookingId)
                .collect(Collectors.toList());

        // **3. 查询所有景点预订**
        List<AttractionBooking> attractionBookings = attractionBookingRepository.findByBookingIdIn(bookingIds);

        // **4. 通过 attractionId 查询 Attraction UUID**
        return attractionBookings.stream().map(attractionBooking -> {
            String attractionUuid = attractionRepository.findByAttractionId(attractionBooking.getAttractionId())
                    .map(Attraction::getUuid)
                    .orElse(null); // 如果找不到景点，返回 null

            return new AttractionBookingDTO(
                    attractionBooking.getAttractionBookingId(),
                    attractionBooking.getBookingId(),
                    attractionBooking.getAttractionId(),
                    attractionUuid, // ✅ 加入景点 UUID
                    attractionBooking.getVisitDate(),
                    attractionBooking.getVisitTime(),
                    attractionBooking.getNumberOfTickets()
            );
        }).collect(Collectors.toList());
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

        // Step 4: 检查是否是该景点的第一条 `AttractionBooking`
        boolean isFirstBooking = attractionBookingRepository.countByAttractionId(attraction.getAttractionId()) == 1;

        if (isFirstBooking) {
            // Step 4.1: 创建 ReviewStats
            ReviewStats reviewStats = ReviewStats.builder()
                    .itemType(ReviewStats.ItemType.Attraction)
                    .itemId(attraction.getAttractionId())
                    .totalReviews(0)
                    .averageRating(BigDecimal.ZERO) // 初始评分 0
                    .build();
            reviewStatsRepository.save(reviewStats);
        }

        return attractionBooking;
    }
}
