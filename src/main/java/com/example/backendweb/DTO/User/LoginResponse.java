package com.example.backendweb.DTO.User;

import lombok.*;

/**
 * @ClassName LoginResponse
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/2/6
 * @Version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    private Integer userId;
    private String name;
    private String email;
    private String role;
}
