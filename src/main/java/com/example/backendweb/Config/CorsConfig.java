package com.example.backendweb.Config;

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

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // 对所有路径生效
                registry.addMapping("/**")
                        // 允许所有来源，如果你只允许特定域名，请替换为对应域名，例如 "http://example.com"
                        .allowedOrigins("http://localhost:5173")
                        // 允许的请求方法
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        // 允许的请求头
                        .allowedHeaders("*")
                        // 是否允许发送 Cookie 等凭证信息，若允许则设置为 true
                        .allowCredentials(true)
                        // 预检请求的缓存时间（秒）
                        .maxAge(3600);
            }
        };
    }
}
