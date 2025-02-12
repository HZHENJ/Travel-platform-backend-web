package com.example.backendweb.Controller;

import com.example.backendweb.DTO.AuthResponseDTO;
import com.example.backendweb.DTO.ErrorResponseDTO;
import com.example.backendweb.Entity.Admin.Admin;
import com.example.backendweb.Repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AdminRepository adminRepository; // Use AdminRepository

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/admin/login") // Updated URL path
    public ResponseEntity<?> adminLogin(@RequestBody Admin admin) {

        Admin existingAdmin = adminRepository.findByUsername(admin.getUsername());
        // Use AdminRepository
        if (existingAdmin != null && passwordEncoder.matches(admin.getPassword(), existingAdmin.getPassword())) {
            String token = generateJwtToken(existingAdmin); // Pass Admin object to JWT generation
            return ResponseEntity.ok(new AuthResponseDTO(token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponseDTO("Invalid username or password."));
        }
    }

    // ... (password recovery, account verification, generateJwtToken - can remain the same)
    private String generateJwtToken(Admin admin) { // Updated parameter type
        // Your JWT generation logic here (using a library like JJWT)
        // Example (replace with your actual JWT implementation):
        return "your_jwt_token_here"; // Replace with actual token
    }

}
