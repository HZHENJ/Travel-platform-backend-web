package com.example.backendweb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
public class BookingTests {  // Separate class for Booking tests

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAttractionsBooking() throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userId", 3);
        requestBody.put("uuid", "0025954c0270ae749a3bd4118b7a020a5ea");
        requestBody.put("visitDate", "2025-02-12");
        requestBody.put("visitTime", "2025-02-12T11:30:00.000Z");
        requestBody.put("numberOfTickets", 1);
        requestBody.put("price", 0);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/attractions/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(MockMvcResultMatchers.status().isOk()); // Check for 200 OK or 201 Created
    }

    @Test
    public void testFetchingAllAttractionBookingsByUserId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/attractions/bookings/1"))
                .andExpect(MockMvcResultMatchers.status().isOk()); // Check for 200 OK
    }

    @Test
    public void testHotelBooking() throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userId", 1);
        requestBody.put("uuid", "0013526460d4164465ebd5944feb16279e4");
        requestBody.put("checkInDate", "2025-03-10");
        requestBody.put("checkOutDate", "2025-03-15");
        requestBody.put("roomType", "Deluxe Suite");
        requestBody.put("guests", 2);
        requestBody.put("price", 0.00);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/hotels/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(MockMvcResultMatchers.status().isOk()); // Check for 200 OK or 201 Created
    }

    @Test
    public void testFetchingAllHotelBooking() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/hotels/bookings/1"))
                .andExpect(MockMvcResultMatchers.status().isOk()); // Check for 200 OK
    }
}