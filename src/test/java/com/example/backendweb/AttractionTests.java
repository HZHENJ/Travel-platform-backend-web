package com.example.backendweb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class AttractionTests { // Separate class for Attraction tests

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllReviewsByUuid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/attractions/002bc44f6ab83c44c0e978e38435fe929ea/reviews"))
                .andExpect(MockMvcResultMatchers.status().isOk()); // Check for 200 OK
    }
}