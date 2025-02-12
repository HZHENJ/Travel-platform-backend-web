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
public class RecommendationTests { // Separate class for Recommendation tests

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testPersonalizedRecommendation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/recommendations/personalized/1"))
                .andExpect(MockMvcResultMatchers.status().isOk()); // Check for 200 OK
    }
}