package com.example.backendweb.DTO.User;

import com.example.backendweb.Entity.User.Preference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName UserRegistrationRequest
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
public class UserRegistrationRequest {
    private String name;
    private String email;
    private String password;
    private String gender;
    private String country;
    private String dateOfBirth;
    private Preference preference;
}
