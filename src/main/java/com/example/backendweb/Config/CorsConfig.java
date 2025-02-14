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
                // 对所有路径生效
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
        String allowedOrigins = System.getenv("ALLOWED_ORIGINS"); // 读取环境变量
        if (allowedOrigins == null || allowedOrigins.isEmpty()) {
            return new String[]{
                    "http://localhost:5173",
                    "http://13.229.230.185:8081",
                    "http://13.229.230.185",
            };
        }
        return allowedOrigins.split(",");
    }
}
