package com.example.backendweb.Controller;

import com.example.backendweb.DTO.User.LoginResponse;
import com.example.backendweb.DTO.User.UserLoginRequest;
import com.example.backendweb.DTO.User.UserRegistrationRequest;
import com.example.backendweb.Entity.User.Authentication;
import com.example.backendweb.Entity.User.Preference;
import com.example.backendweb.Entity.User.User;
import com.example.backendweb.Services.UserService;
import com.example.backendweb.Util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @ClassName UserController
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/2/6
 * @Version 1.0
 */

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 用户注册（支持带或不带 `Preference`）
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .build();

        Authentication auth = Authentication.builder()
                .username(request.getEmail())
                .passwordHash(request.getPassword())
                .build();

        Preference preference = request.getPreference(); // `Preference` 可选
        boolean success = userService.registerUserData(auth, user, preference);

        return success ? ResponseEntity.ok("User registered successfully!")
                : ResponseEntity.status(500).body("User registration failed.");
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody UserLoginRequest request) {
        User user = userService.login(request.getUsername(), request.getPassword());

        // 通过 `UserService` 获取 `Authentication` 记录
        Authentication auth = userService.getUserAuthentication(request.getUsername());

        // 生成 JWT 令牌
        String token = jwtUtil.generateToken(user.getUserId(), auth.getRole());

        return ResponseEntity.ok(new LoginResponse(token, user.getUserId(), user.getName(), user.getEmail(), auth.getRole().name()));
    }

}

