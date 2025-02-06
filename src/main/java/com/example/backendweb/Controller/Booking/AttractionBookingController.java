package com.example.backendweb.Controller.Booking;

import com.example.backendweb.DTO.AttractionBookingRequest;
import com.example.backendweb.Entity.Booking.AttractionBooking;
import com.example.backendweb.Services.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName BookingAttractionController
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/2/6
 * @Version 1.0
 */

@RestController
@RequestMapping("/api/attractions")
public class AttractionBookingController {
    private final BookingService bookingService;
    public AttractionBookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * 处理创建景点预订的请求
     * @param request 前端传来的预订信息
     * @return 预订详情
     */
    @PostMapping("/booking")
    public ResponseEntity<?> createAttractionBooking(@RequestBody AttractionBookingRequest request) {
        try {
            AttractionBooking booking = bookingService.createAttractionBooking(request);
            return ResponseEntity.ok(booking); // 200 OK，返回创建的预订信息
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Booking failed: " + e.getMessage()); // 400 错误
        }
    }

    @GetMapping("/bookings/{userId}")
    public ResponseEntity<List<AttractionBooking>> getAttractionBookingsByUser(@PathVariable Integer userId) {
        List<AttractionBooking> attractionBookings = bookingService.getAttractionBookingsByUserId(userId);
        return ResponseEntity.ok(attractionBookings);
    }

}
