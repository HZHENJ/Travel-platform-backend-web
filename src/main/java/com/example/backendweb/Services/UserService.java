package com.example.backendweb.Services;


import com.example.backendweb.Repository.AuthenticationRepository;
import com.example.backendweb.Repository.PreferenceRepository;
import com.example.backendweb.Repository.UserRepository;
import com.example.backendweb.Entity.Exception.CustomException;
import com.example.backendweb.Entity.User.Authentication;
import com.example.backendweb.Entity.User.Preference;
import com.example.backendweb.Entity.User.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    public void RetriveData(Authentication auth, User user, Preference pre){
        registerUser(user);
        registerAuthentication(auth,user);
        registerPreference(pre,user);
    }

    public void registerUser(User user){
        if(user == null) throw new CustomException("Empty Data",400);
        Optional<User> existingUser = userRepository.findById(user.getUserId());
        if(existingUser.isPresent()) throw new CustomException("User already exits",409);
        userRepository.save(user);
    }

    public void registerAuthentication(Authentication auth,User user){
        if(auth == null && user == null) throw new CustomException("Empty Data",400);
        Optional<Authentication> existingAuth = authenticationRepository.findById(auth.getAuthId());
        if(existingAuth.isPresent()) throw new CustomException("User already exits",409);
        auth.setPasswordHash(bCryptPasswordEncoder.encode(auth.getPasswordHash()));
        auth.setUser(user);
        authenticationRepository.save(auth);
    }

    public void registerPreference(Preference pre,User user){
        if(pre == null && user == null) throw new CustomException("Empty Data",400);
        Optional<Authentication> existingPre = authenticationRepository.findById(pre.getPreferenceId());
        if(existingPre.isPresent()) throw new CustomException("User already exits",409);
        pre.setUser(user);
        preferenceRepository.save(pre);
    }
}
