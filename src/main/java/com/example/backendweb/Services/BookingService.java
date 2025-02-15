package com.example.backendweb.Services;

import com.example.backendweb.DTO.Booking.AttractionBookingDTO;
import com.example.backendweb.DTO.Booking.AttractionBookingRequest;
import com.example.backendweb.DTO.Booking.FlightBookingRequest;
import com.example.backendweb.DTO.Booking.HotelBookingDTO;
import com.example.backendweb.DTO.Booking.HotelBookingRequest;
import com.example.backendweb.Entity.Booking.AttractionBooking;
import com.example.backendweb.Entity.Booking.Booking;
import com.example.backendweb.Entity.Booking.FlightBooking;
import com.example.backendweb.Entity.Booking.HotelBooking;
import com.example.backendweb.Entity.Info.Attraction;
import com.example.backendweb.Entity.Info.Hotel;
import com.example.backendweb.Entity.Review.Review;
import com.example.backendweb.Entity.Review.ReviewStats;
import com.example.backendweb.Repository.AttractionRepository;
import com.example.backendweb.Repository.Booking.AttractionBookingRepository;
import com.example.backendweb.Repository.Booking.BookingRepository;
import com.example.backendweb.Repository.Booking.FlightBookingRepository;
import com.example.backendweb.Repository.Booking.HotelBookingRepository;
import com.example.backendweb.Repository.HotelRepository;
import com.example.backendweb.Repository.Review.ReviewRepository;
import com.example.backendweb.Repository.Review.ReviewStatsRepository;
import com.example.backendweb.Repository.User.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @ClassName BookingService
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/2/6
 * @Version 1.0
 */

@Slf4j
@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final AttractionBookingRepository attractionBookingRepository;
    private final AttractionRepository attractionRepository;
    private final HotelBookingRepository hotelBookingRepository;
    private final HotelRepository hotelRepository;
    private final ReviewStatsRepository reviewStatsRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final FlightBookingRepository flightBookingRepository;

    public BookingService(BookingRepository bookingRepository,
                          AttractionBookingRepository attractionBookingRepository,
                          AttractionRepository attractionRepository,
                          HotelBookingRepository hotelBookingRepository,
                          HotelRepository hotelRepository,
                          ReviewStatsRepository reviewStatsRepository,
                          ReviewRepository reviewRepository, UserRepository userRepository, FlightBookingRepository flightBookingRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.attractionBookingRepository = attractionBookingRepository;
        this.attractionRepository = attractionRepository;
        this.hotelBookingRepository = hotelBookingRepository;
        this.hotelRepository = hotelRepository;
        this.reviewStatsRepository = reviewStatsRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.flightBookingRepository = flightBookingRepository;
    }

    public List<AttractionBookingDTO> getAttractionBookingsByUserId(Integer userId) {
        // 1. 查询该用户的所有 Booking
        List<Booking> userBookings = bookingRepository.findByUserId(userId);

        // 2. 如果用户没有任何 Booking，直接返回空列表
        if (userBookings.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. 提取 bookingId
        List<Integer> bookingIds = userBookings.stream()
                .map(Booking::getBookingId)
                .collect(Collectors.toList());

        // 3. 查询所有景点预订
        List<AttractionBooking> attractionBookings = attractionBookingRepository.findByBookingIdIn(bookingIds);

        // 4. 通过 attractionId 查询 Attraction UUID
        return attractionBookings.stream()
                .map(attractionBooking -> {
                    String attractionUuid = attractionRepository.findByAttractionId(attractionBooking.getAttractionId())
                            .map(Attraction::getUuid)
                            .orElse(null); // 如果找不到景点，返回 null

                    Booking booking = userBookings.stream()
                            .filter(b->b.getBookingId().equals(attractionBooking.getBookingId()))
                            .findFirst()
                            .orElse(null);

            return new AttractionBookingDTO(
                    attractionBooking.getAttractionBookingId(),
                    attractionBooking.getBookingId(),
                    attractionBooking.getAttractionId(),
                    attractionUuid,
                    attractionBooking.getVisitDate(),
                    attractionBooking.getVisitTime(),
                    attractionBooking.getNumberOfTickets(),
                    booking != null ? booking.getStatus().name() : "Unknown"
            );
        }).collect(Collectors.toList());
    }

    public List<HotelBookingDTO> getHotelBookingsByUserId(Integer userId) {
        // 1. 查询该用户的所有 Booking
        List<Booking> userBookings = bookingRepository.findByUserIdAndBookingType(userId, Booking.BookingType.Hotel);

        // 2. 如果用户没有任何 Booking，直接返回空列表
        if (userBookings.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. 提取 bookingId
        List<Integer> bookingIds = userBookings.stream()
                .map(Booking::getBookingId)
                .collect(Collectors.toList());

        // 3. 查询所有酒店预订
        List<HotelBooking> hotelBookings = hotelBookingRepository.findByBookingIdIn(bookingIds);

        // 4. 通过 hotelId 查询 Hotel UUID
        return hotelBookings.stream()
                .map(hotelBooking -> {
                    String bookingStatus = userBookings.stream()
                            .filter(b -> b.getBookingId().equals(hotelBooking.getBookingId()))
                            .findFirst()
                            .map(b -> b.getStatus().name())  // 获取 `Booking.status`
                            .orElse("Unknown");

                    String hotelUuid = hotelRepository.findById(hotelBooking.getHotelId())
                            .map(Hotel::getUuid)
                            .orElse(null); // 如果找不到酒店，返回 null

            // 5. 获取对应的 Booking 记录
            Booking booking = userBookings.stream()
                    .filter(b -> b.getBookingId().equals(hotelBooking.getBookingId()))
                    .findFirst()
                    .orElse(null);

            return new HotelBookingDTO(
                    hotelBooking.getHotelBookingId(),
                    hotelBooking.getBookingId(),
                    hotelBooking.getHotelId(),
                    hotelUuid,
                    hotelBooking.getCheckInDate(),
                    hotelBooking.getCheckOutDate(),
                    hotelBooking.getRoomType(),
                    hotelBooking.getGuests(),
                    booking != null ? booking.getTotalAmount().doubleValue() : null, // 确保转换为 Double
                    bookingStatus
            );
        }).collect(Collectors.toList());
    }

    @Transactional
    public AttractionBooking createAttractionBooking(AttractionBookingRequest request) {
        log.info("Starting Attraction Booking process for UUID: {}", request.getUuid());

        // Step 1: 通过 UUID 查找 Attraction，若有多个则取第一个
        Optional<Attraction> attractionOpt = attractionRepository.findByUuid(request.getUuid());

        Attraction attraction = attractionOpt.orElseGet(() -> {
            log.info("Attraction not found, creating new attraction with UUID: {}", request.getUuid());
            Attraction newAttraction = Attraction.builder()
                    .uuid(request.getUuid())
                    .build();
            return attractionRepository.save(newAttraction);
        });
        log.info("Attraction found/created. Attraction ID: {}", attraction.getAttractionId());

        // Step 2: 创建通用 Booking 记录
        Booking booking = Booking.builder()
                .userId(request.getUserId())
                .bookingType(Booking.BookingType.Attraction)
                .status(Booking.BookingStatus.Pending)
                .totalAmount(BigDecimal.valueOf(request.getPrice()))
                .build();
        booking = bookingRepository.save(booking);
        log.info("Booking record created with ID: {}, Amount: {}", booking.getBookingId(), booking.getTotalAmount());

        // Step 3: 创建具体的 AttractionBooking 记录
        AttractionBooking attractionBooking = AttractionBooking.builder()
                .bookingId(booking.getBookingId())
                .attractionId(attraction.getAttractionId())
                .visitDate(request.getVisitDate())
                .visitTime(request.getVisitTime())
                .numberOfTickets(request.getNumberOfTickets())
                .build();
        attractionBookingRepository.save(attractionBooking);
        log.info("AttractionBooking record created for Attraction ID: {}, Booking ID: {}",
                attraction.getAttractionId(), booking.getBookingId());

        // Step 4: 检查是否已存在 ReviewStats 记录
        Optional<ReviewStats> reviewStatsOpt = reviewStatsRepository.findByItemTypeAndItemId(
                ReviewStats.ItemType.Attraction, attraction.getAttractionId()
        );

        if (reviewStatsOpt.isEmpty()) {
            // 如果没有 ReviewStats 记录，则创建新记录
            ReviewStats reviewStats = ReviewStats.builder()
                    .itemType(ReviewStats.ItemType.Attraction)
                    .itemId(attraction.getAttractionId())
                    .totalReviews(0)
                    .averageRating(BigDecimal.ZERO) // 初始评分 0
                    .build();
            reviewStatsRepository.save(reviewStats);
            log.info("ReviewStats record created for Attraction ID: {}", attraction.getAttractionId());
        } else {
            log.info("ReviewStats record already exists for Attraction ID: {}, skipping creation.", attraction.getAttractionId());
        }

        // Step 5: 创建 Review 记录
        Review review = Review.builder()
                .bookingId(booking.getBookingId())
                .userId(request.getUserId())
                .itemType(Review.ItemType.Attraction)
                .itemId(attraction.getAttractionId())
                .rating(BigDecimal.ZERO) // 初始评分 0
                .comment("Default review. No comment provided.") // 默认评论内容
                .status(Review.ReviewStatus.hide) // 初始状态为隐藏
                .build();
        reviewRepository.save(review);
        log.info("Review record created for Booking ID: {}, Attraction ID: {}",
                booking.getBookingId(), attraction.getAttractionId());

        log.info("Attraction Booking process completed successfully for UUID: {}", request.getUuid());
        return attractionBooking;
    }


    @Transactional
    public HotelBooking createHotelBooking(HotelBookingRequest request) {
        log.info("Starting Hotel Booking process for UUID: {}", request.getUuid());

        // Step 1: 通过 UUID 查找 Hotel，若有多个则取第一个
        Optional<Hotel> hotelOpt = hotelRepository.findByUuid(request.getUuid());

        Hotel hotel = hotelOpt.orElseGet(() -> {
            log.info("Hotel not found, creating new Hotel with UUID: {}", request.getUuid());
            Hotel newHotel = Hotel.builder()
                    .uuid(request.getUuid())
                    .build();
            return hotelRepository.save(newHotel);
        });
        log.info("Hotel found/created. Hotel ID: {}", hotel.getHotelId());

        // Step 2: 创建 `Booking` 记录
        Booking booking = Booking.builder()
                .userId(request.getUserId())
                .bookingType(Booking.BookingType.Hotel)
                .status(Booking.BookingStatus.Pending)
                .totalAmount(BigDecimal.valueOf(request.getPrice()))
                .build();
        booking = bookingRepository.save(booking);
        log.info("Booking record created with ID: {}, Amount: {}", booking.getBookingId(), booking.getTotalAmount());

        // Step 3: 创建 `HotelBooking` 记录
        HotelBooking hotelBooking = HotelBooking.builder()
                .bookingId(booking.getBookingId())
                .hotelId(hotel.getHotelId())
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .roomType(request.getRoomType())
                .guests(request.getGuests())
                .build();
        hotelBookingRepository.save(hotelBooking);
        log.info("HotelBooking record created for Hotel ID: {}, Booking ID: {}", hotel.getHotelId(), booking.getBookingId());

        // Step 4: 检查是否已存在 ReviewStats 记录
        Optional<ReviewStats> reviewStatsOpt = reviewStatsRepository.findByItemTypeAndItemId(
                ReviewStats.ItemType.Hotel, hotel.getHotelId()
        );

        if (reviewStatsOpt.isEmpty()) {
            // 如果没有 ReviewStats 记录，则创建新记录
            ReviewStats reviewStats = ReviewStats.builder()
                    .itemType(ReviewStats.ItemType.Hotel)
                    .itemId(hotel.getHotelId())
                    .totalReviews(0)
                    .averageRating(BigDecimal.ZERO) // 初始评分 0
                    .build();
            reviewStatsRepository.save(reviewStats);
            log.info("ReviewStats record created for Hotel ID: {}", hotel.getHotelId());
        } else {
            log.info("ReviewStats record already exists for Hotel ID: {}, skipping creation.", hotel.getHotelId());
        }

        // Step 5: 创建 Review 记录
        Review review = Review.builder()
                .bookingId(booking.getBookingId())
                .userId(request.getUserId())
                .itemType(Review.ItemType.Hotel)
                .itemId(hotel.getHotelId())
                .rating(BigDecimal.ZERO) // 初始评分 0
                .comment("Default review. No comment provided.") // 默认评论内容
                .status(Review.ReviewStatus.hide) // 初始状态为隐藏
                .build();
        reviewRepository.save(review);
        log.info("Review record created for Booking ID: {}, Hotel ID: {}", booking.getBookingId(), hotel.getHotelId());

        log.info("Hotel Booking process completed successfully for UUID: {}", request.getUuid());
        return hotelBooking;
    }


    public FlightBooking createFlightBooking(FlightBookingRequest request) {
        // 创建通用 Booking 记录
        Booking booking = Booking.builder()
                .userId(Math.toIntExact(request.getUserId()))
                .bookingType(Booking.BookingType.Flight)
                .status(Booking.BookingStatus.Pending)
                .totalAmount(BigDecimal.valueOf(request.getTotalPrice()))
                .build();
        bookingRepository.save(booking);

        // 创建具体的 FlightBooking 记录
        FlightBooking flightBooking = FlightBooking.builder()
                .bookingId(booking.getBookingId())
                .flightId(request.getId())
                .seatClass(FlightBooking.SeatClass.valueOf(request.getSelectedSeats()))
                .passengerId(request.getUserId().toString())
                .passengerName(userRepository.findById(Math.toIntExact(request.getUserId())).get().getName())
                .build();

        flightBookingRepository.save(flightBooking);

        return flightBooking;
    }

    @Transactional
    public void cancelAttractionBooking(Integer bookingId) {
        // 查找 AttractionBooking
        AttractionBooking attractionBooking = attractionBookingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        // 查找通用 Booking 记录
        Booking booking = bookingRepository.findById(attractionBooking.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking record not found"));

        // 更新 Booking 状态为 Canceled
        booking.setStatus(Booking.BookingStatus.Canceled);
        bookingRepository.save(booking);

        // 删除 AttractionBooking 记录
        // attractionBookingRepository.delete(attractionBooking);
    }

    @Transactional
    public void cancelHotelBooking(Integer bookingId) {
        // 使用 `findByBookingId` 查找酒店预订
        HotelBooking hotelBooking = hotelBookingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Hotel booking not found for bookingId: " + bookingId));

        // 查找对应的 Booking 记录
        Booking booking = bookingRepository.findById(hotelBooking.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking record not found for ID: " + hotelBooking.getBookingId()));

        // 更新 Booking 状态为 Canceled
        booking.setStatus(Booking.BookingStatus.Canceled);
        bookingRepository.save(booking);
    }

    public List<FlightBooking> getFlightBookingsByUserId(Integer userId) {
           List<Integer> bkIds = bookingRepository.findByUserIdAndBookingType(userId, Booking.BookingType.Flight)
                   .stream()
                   .map(Booking::getBookingId)
                   .toList();
        return flightBookingRepository.findByBookingIdIn(bkIds);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public void deleteBooking(Integer bookingId) {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();

            // 1. Delete the booking
            bookingRepository.delete(booking);

        } else {
            throw new EntityNotFoundException("Booking not found with ID: " + bookingId);
        }
    }

    public Booking updateBooking(Integer bookingId, Booking booking) {
        Booking existingBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        // Update the fields you want to allow updating.  Don't just set the whole object!
        existingBooking.setUserId(booking.getUserId());
        existingBooking.setBookingType(booking.getBookingType());
        existingBooking.setStatus(booking.getStatus());
        existingBooking.setTotalAmount(booking.getTotalAmount());
        // ... update other fields ...

        return bookingRepository.save(existingBooking);
    }
}


