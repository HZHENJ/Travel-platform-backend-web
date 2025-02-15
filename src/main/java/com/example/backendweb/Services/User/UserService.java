package com.example.backendweb.Services.User;


import com.example.backendweb.Entity.Booking.Booking;
import com.example.backendweb.Repository.Booking.BookingRepository;
import com.example.backendweb.Repository.User.AuthenticationRepository;
import com.example.backendweb.Repository.User.PreferenceRepository;
import com.example.backendweb.Repository.User.UserRepository;
import com.example.backendweb.Entity.Exception.CustomException;
import com.example.backendweb.Entity.User.Authentication;
import com.example.backendweb.Entity.User.Preference;
import com.example.backendweb.Entity.User.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationRepository authenticationRepository;
    private final PreferenceRepository preferenceRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final BookingRepository bookingRepository;

    public UserService(UserRepository userRepository,
                       AuthenticationRepository authenticationRepository,
                       PreferenceRepository preferenceRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       BookingRepository bookingRepository
    ) {
        this.userRepository = userRepository;
        this.authenticationRepository = authenticationRepository;
        this.preferenceRepository = preferenceRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.bookingRepository = bookingRepository;
    }

    // 判断用户是否是 Attraction 的新用户（booking < 5）
    public boolean isNewAttractionUser(Integer userId) {
        log.info("Checking if user {} is a new attraction user...", userId);

        long attractionBookingCount = bookingRepository.countByUserIdAndBookingType(userId, Booking.BookingType.Attraction);

        boolean isNewUser = attractionBookingCount < 5;
        log.info("User {} has booked attractions {} times. Is new user: {}", userId, attractionBookingCount, isNewUser);

        return isNewUser;
    }

    public User login(String email, String password) {
        // find user by email
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new CustomException("User not found", 404);
        }
        User user = userOpt.get();

        // find auth by user
        Optional<Authentication> authOpt = authenticationRepository.findByUser(user);
        if (authOpt.isEmpty()) {
            throw new CustomException("User not found", 404);
        }
        Authentication auth = authOpt.get();

        if (!bCryptPasswordEncoder.matches(password, auth.getPasswordHash())) {
            throw new CustomException("Invalid password", 401);
        }

        return auth.getUser();
    }

    @Transactional
    public boolean registerUserData(Authentication auth, User user, Preference pre){
        boolean userRegistered = registerUser(user);
        boolean authRegistered = registerAuthentication(auth, user);
        boolean preRegistered = registerPreference(pre, user);
        return userRegistered && authRegistered && preRegistered;
    }

    /**
     * 注册 `User`
     */
    public boolean registerUser(User user) {
        if (user == null) {
            throw new CustomException("Empty Data", 400);
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new CustomException("User already exists", 409);
        }
        userRepository.save(user);
        return true;
    }

    /**
     * 注册 `Authentication`
     */
    public boolean registerAuthentication(Authentication auth, User user){
        if(auth == null || user == null) {
            throw new CustomException("Empty Data",400);
        }
        if (authenticationRepository.findByUsername(auth.getUsername()).isPresent()) {
            throw new CustomException("User already exists", 409);
        }
        auth.setPasswordHash(bCryptPasswordEncoder.encode(auth.getPasswordHash()));
        auth.setUser(user);
        authenticationRepository.save(auth);
        return true;
    }

    public boolean registerPreference(Preference pre,User user){
        if(user == null) {
            throw new CustomException("Empty Data",400);
        }
        // if `pre` is null，create default `Preference`
        if(pre == null) {
            pre = Preference.builder()
                    .user(user)
                    .build();
        } else {
            pre.setUser(user);
        }
        preferenceRepository.save(pre);
        return true;
    }

    /**
     * 根据username获取 `Authentication` 记录
     */
    public Authentication getUserAuthentication(User user) {
        return authenticationRepository.findByUser(user)
                .orElseThrow(() -> new CustomException("User authentication not found", 404));
    }

    public User updateUser(Integer userId, User updatedUser) {
        return userRepository.findById(userId).map(user -> {
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setDateOfBirth(updatedUser.getDateOfBirth());
            user.setGender(updatedUser.getGender());
            user.setCountry(updatedUser.getCountry());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Optional<Preference> getUserPreferences(Integer userId) {
        return preferenceRepository.findByUser_UserId(userId);
    }

    public Preference updateOrCreatePreferences(Integer userId, Preference newPreferences) {
        return preferenceRepository.findByUser_UserId(userId).map(preference -> {
            preference.setTravelType(newPreferences.getTravelType());
            preference.setBudgetRange(newPreferences.getBudgetRange());
            preference.setLanguage(newPreferences.getLanguage());
            return preferenceRepository.save(preference);
        }).orElseGet(() -> {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            newPreferences.setUser(user);
            return preferenceRepository.save(newPreferences);
        });
    }

    public Optional<User> getUserById(Integer userId) {
        return userRepository.findById(userId);
    }
}
