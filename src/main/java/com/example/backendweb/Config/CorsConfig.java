package com.example.backendweb.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @ClassName CorsConfig
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/2/2
 * @Version 1.0
 */

@Configuration
public class CorsConfig {

    private final Logger logger = LoggerFactory.getLogger(CorsConfig.class);

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Effective for all paths
                registry.addMapping("/**")
                        .allowedOrigins(getAllowedOrigins())
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(false)
                        .maxAge(3600);
            }
        };
    }

    private String[] getAllowedOrigins() {
        String allowedOrigins = System.getenv("ALLOWED_ORIGINS"); // Reading environment variables
        if (allowedOrigins == null || allowedOrigins.isEmpty()) {
            return new String[]{
                    "http://localhost:5173",
                    "http://52.77.169.8:8081",
                    "http://52.77.169.8",
            };
        }
        return allowedOrigins.split(",");
    }
}
