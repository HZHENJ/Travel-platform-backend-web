package com.example.backendweb.Services.User;


import com.example.backendweb.Repository.User.AuthenticationRepository;
import com.example.backendweb.Repository.User.PreferenceRepository;
import com.example.backendweb.Repository.User.UserRepository;
import com.example.backendweb.Entity.Exception.CustomException;
import com.example.backendweb.Entity.User.Authentication;
import com.example.backendweb.Entity.User.Preference;
import com.example.backendweb.Entity.User.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationRepository authenticationRepository;
    private final PreferenceRepository preferenceRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, AuthenticationRepository authenticationRepository, PreferenceRepository preferenceRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.authenticationRepository = authenticationRepository;
        this.preferenceRepository = preferenceRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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
}
