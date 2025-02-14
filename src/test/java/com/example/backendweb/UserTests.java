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
public class UserTests { // Separate class for User tests

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRegister() throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", "john@example.com");
        requestBody.put("password", "securePassword123");
        Map<String, Object> preference = new HashMap<>();
        preference.put("travelType", "Single");
        preference.put("budgetRange", 5000);
        preference.put("language", "en");
        requestBody.put("preference", preference);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(MockMvcResultMatchers.status().isOk()); // Check for 200 OK
    }

    @Test
    public void testRegisterNoPreference() throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", "x@qq.com");
        requestBody.put("password", "123");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(MockMvcResultMatchers.status().isOk()); // Check for 200 OK
    }

    @Test
    public void testLogin() throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", "a@qq.com");
        requestBody.put("password", "123");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(MockMvcResultMatchers.status().isOk()); // Check for 200 OK
    }

    @Test
    public void testFetchPreferences() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/preferences/1"))
                .andExpect(MockMvcResultMatchers.status().isOk()); // Check for 200 OK
    }

    @Test
    public void testModifyPreferences() throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("travelType", "Family");
        requestBody.put("budgetRange", 5000.00);
        requestBody.put("language", "en");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/preferences/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(MockMvcResultMatchers.status().isOk()); // Check for 200 OK
    }
}