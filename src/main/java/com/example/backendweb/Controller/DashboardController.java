package com.example.backendweb.Controller;

import com.example.backendweb.Repository.AttractionBookingRepository;
import com.example.backendweb.Repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.backendweb.Entity.Booking.Booking;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private AttractionBookingRepository attractionBookingRepository;

    @GetMapping("/yearlyBookings")
    public List<Map<String, Object>> getYearlyBookings() {
        List<Map<String, Object>> yearlyBookings = new ArrayList<>();

        List<Object[]> results = bookingRepository.getYearlyBookingCounts();
        for (Object[] result : results) {
            // Convert integer month to Month enum
            int monthNumber = ((Number) result[0]).intValue();
            Month month = Month.of(monthNumber);
            int count = ((Number) result[1]).intValue();

            yearlyBookings.add(createBookingData(month.toString(), count));
        }

        return yearlyBookings;
    }

    private Map<String, Object> createBookingData(String month, int count) {
        Map<String, Object> data = new HashMap<>();
        data.put("month", month);
        data.put("count", count);
        return data;
    }

    @GetMapping("/bookingsByType")
    public List<Map<String, Object>> getBookingsByType() {
        List<Map<String, Object>> bookingsByType = new ArrayList<>();

        List<Object[]> results = bookingRepository.getBookingCountsByType();
        for (Object[] result : results) {
            Booking.BookingType type = (Booking.BookingType) result[0];
            long count = ((Number) result[1]).longValue(); // Correctly get long value

            bookingsByType.add(createBookingTypeData(type.toString(), (int) count)); // Cast to int if needed
        }

        return bookingsByType;
    }

    private Map<String, Object> createBookingTypeData(String type, int count) {
        Map<String, Object> data = new HashMap<>();
        data.put("type", type);
        data.put("count", count);
        return data;
    }

    // ... (mostFrequentPlaces and visitorSatisfaction endpoints - adapt similarly)

    // Example for mostFrequentPlaces (you'll need a suitable query in the repository)
    @GetMapping("/mostFrequentPlaces")
    public List<Map<String, Object>> getMostFrequentPlaces() {
        List<Map<String, Object>> mostFrequentPlaces = new ArrayList<>();

        List<Object[]> results = attractionBookingRepository.getMostFrequentAttractions(); // Implement this
        for (Object[] result : results) {
            String place = (String) result[0];
            long visits = ((Number) result[1]).longValue(); // Correctly get long value

            mostFrequentPlaces.add(createFrequentPlaceData(place, (int) visits));
        }

        return mostFrequentPlaces;
    }

    private Map<String, Object> createFrequentPlaceData(String place, int visits) {
        Map<String, Object> data = new HashMap<>();
        data.put("place", place);
        data.put("visits", visits);
        return data;
    }


    @GetMapping("/visitorSatisfaction")
    public List<Map<String, Object>> getVisitorSatisfaction() {
        List<Map<String, Object>> visitorSatisfaction = new ArrayList<>();

        // Example data (replace with actual data from database or other source)
        visitorSatisfaction.add(createSatisfactionData("Very Satisfied", 80));
        visitorSatisfaction.add(createSatisfactionData("Satisfied", 15));
        visitorSatisfaction.add(createSatisfactionData("Neutral", 5));


        return visitorSatisfaction;
    }

    private Map<String, Object> createSatisfactionData(String satisfaction, int count) {
        Map<String, Object> data = new HashMap<>();
        data.put("satisfaction", satisfaction);
        data.put("count", count);
        return data;
    }



}
