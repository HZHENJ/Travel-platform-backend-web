package com.example.backendweb.Services.User;

import com.example.backendweb.Entity.User.Preference;
import com.example.backendweb.Entity.User.User;
import com.example.backendweb.Repository.User.PreferenceRepository;
import com.example.backendweb.Repository.User.UserRepository;
import org.springframework.stereotype.Service;

/**
 * @ClassName PreferenceService
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/2/11
 * @Version 1.0
 */

@Service
public class PreferenceService {

    private final PreferenceRepository preferenceRepository;
    private final UserRepository userRepository;

    public PreferenceService(PreferenceRepository preferenceRepository, UserRepository userRepository) {
        this.preferenceRepository = preferenceRepository;
        this.userRepository = userRepository;
    }

    /**
     * 获取用户偏好
     */
    public Preference getUserPreference(Integer userId) {
        return preferenceRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User preference not found"));
    }

    /**
     * 更新用户偏好
     */
    public Preference updateUserPreference(Integer userId, Preference updatedPreference) {
        Preference preference = preferenceRepository.findByUser_UserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new IllegalArgumentException("User not found"));
                    return Preference.builder().user(user).build();
                });

        preference.setTravelType(updatedPreference.getTravelType());
        preference.setBudgetRange(updatedPreference.getBudgetRange());
        preference.setLanguage(updatedPreference.getLanguage());

        return preferenceRepository.save(preference);
    }
}
