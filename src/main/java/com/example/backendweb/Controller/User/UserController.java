package com.example.backendweb.Controller.User;

import com.example.backendweb.DTO.User.LoginResponse;
import com.example.backendweb.DTO.User.UserLoginRequest;
import com.example.backendweb.DTO.User.UserRegistrationRequest;
import com.example.backendweb.Entity.User.Authentication;
import com.example.backendweb.Entity.User.Preference;
import com.example.backendweb.Entity.User.User;
import com.example.backendweb.Services.User.UserService;
import com.example.backendweb.Util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
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

    @GetMapping("/{userId}/is-new-attraction-user")
    public boolean isNewAttractionUser(@PathVariable Integer userId) {
        return userService.isNewAttractionUser(userId);
    }

    /**
     * 用户注册（支持带或不带 `Preference`）
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .status(User.Status.Active)
                .build();

        Authentication auth = Authentication.builder()
                .username("user-"+user.getUserId())
                .passwordHash(request.getPassword())
                .role(Authentication.Role.User)
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
        User user = userService.login(request.getEmail(), request.getPassword());

        // 通过 `UserService` 获取 `Authentication` 记录
        Authentication auth = userService.getUserAuthentication(user);

        // 生成 JWT 令牌
        String token = jwtUtil.generateToken(user.getUserId(), auth.getRole());

        return ResponseEntity.ok(
                new LoginResponse(
                        token,
                        user.getUserId(),
                        user.getName(),
                        user.getEmail(),
                        auth.getRole().name()
                ));
    }

    // 获取用户信息
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable Integer userId) {
        Optional<User> user = userService.getUserById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found"));
        }
        return ResponseEntity.ok(user.get());
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserProfile(@PathVariable Integer userId, @RequestBody User updatedUser) {
        User user = userService.updateUser(userId, updatedUser);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}/preferences")
    public ResponseEntity<Preference> getUserPreferences(@PathVariable Integer userId) {
        return userService.getUserPreferences(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    /**
     * 更新或创建用户偏好
     */
    @PutMapping("/{userId}/preferences")
    public ResponseEntity<?> updateUserPreferences(@PathVariable Integer userId, @RequestBody Preference newPreferences) {
        Preference updatedPreferences = userService.updateOrCreatePreferences(userId, newPreferences);
        return ResponseEntity.ok(updatedPreferences);
    }

}

