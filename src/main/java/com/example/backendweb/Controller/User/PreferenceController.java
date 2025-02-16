package com.example.backendweb.Controller.User;

import com.example.backendweb.Entity.User.Preference;
import com.example.backendweb.Services.User.PreferenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName PreferenceController
 * @Description
 * @Author HUANG ZHENJIA
 * @StudentID A0298312B
 * @Date 2025/2/11
 * @Version 1.0
 */

@RestController
@RequestMapping("/api/preferences")
public class PreferenceController {
    private final PreferenceService preferenceService;

    public PreferenceController(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    /**
     * Capturing user preferences
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Preference> getUserPreference(@PathVariable Integer userId) {
        try {
            Preference preference = preferenceService.getUserPreference(userId);
            return ResponseEntity.ok(preference);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Updating user preferences
     */
    @PutMapping("/{userId}")
    public ResponseEntity<Preference> updateUserPreference(
            @PathVariable Integer userId, @RequestBody Preference updatedPreference) {
        try {
            Preference preference = preferenceService.updateUserPreference(userId, updatedPreference);
            return ResponseEntity.ok(preference);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
