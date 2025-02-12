package com.example.backendweb.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName ApiController
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/2/12
 * @Version 1.0
 */
@RestController
@RequestMapping("/api")
public class ApiController {
    @GetMapping("/")
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("API is working");
    }
}

