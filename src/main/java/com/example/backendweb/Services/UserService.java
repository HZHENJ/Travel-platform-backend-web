package com.example.backendweb.Services;


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

    public User login(String username, String password){
        Optional<Authentication> data = authenticationRepository.findByUsername(username);
        if (data.isPresent() && bCryptPasswordEncoder.matches(password, data.get().getPasswordHash())){
            return data.get().getUser();
        }else{
            throw new CustomException("User not exist or Wrong password",400);
        }
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
}
