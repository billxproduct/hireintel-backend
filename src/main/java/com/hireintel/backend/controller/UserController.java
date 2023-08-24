package com.hireintel.backend.controller;

import com.hireintel.backend.beans.SignUpRequest;
import com.hireintel.backend.beans.SimpleMailMessage;
import com.hireintel.backend.models.User;
import com.hireintel.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(@RequestBody SignUpRequest request) {
        try {
            String email = request.getEmail();

            // Generate JWT token
            String token = JwtUtils.generateToken(email);

            // Read and encode image file
            Path imageFilePath = Paths.get("path/to/hireverse_logo.png");
            byte[] imageData = Files.readAllBytes(imageFilePath);
            String imageBase64 = Base64.getEncoder().encodeToString(imageData);

            // Create email message
            SimpleMailMessage mailMessage = SimpleMailMessage.builder()
                    .from("mailadmin@hireverse.com")
                    .to(email)
                    .subject("Verify Your Email")
                    .text(createAdminVerificationEmail(token))
                    .build();

            // Attach image
            mailMessage.addAttachment("hireverse_logo.png", new ByteArrayResource(imageData));

            // Send email
            javaMailSender.send(mailMessage);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Verification email sent successfully.");
            response.put("userExists", false);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to send verification email.");
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/setUserDetails")
    public ResponseEntity<Map<String, Object>> setUserDetails(@RequestBody Map<String, Object> requestBody) {
        try {
            String token = (String) requestBody.get("token");
            String name = (String) requestBody.get("name");
            String password = (String) requestBody.get("password");
            String mobileNo = (String) requestBody.get("mobileNo");

            // Verify JWT token
            String email = JwtUtils.verifyToken(token);

            Optional<User> existingUserOptional = userRepository.findByEmail(email);
            if (existingUserOptional.isPresent()) {
                User existingUser = existingUserOptional.get();
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "User saved successfully");
                response.put("token", "shivam");
                response.put("user", existingUser);
                response.put("organizations", new ArrayList<>());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401).body(Map.of("success", false, "message", "The account has already been created. Please login to continue."));
            }

            // Create and save the user
            User user = User.builder()
                    .email(email)
                    .name(name)
                    .mobileNo(mobileNo)
                    .password(password)
                    .status(User.UserStatus.VERIFIED)
                    .build();
            User savedUser = userRepository.save(user);
            String userIdToken = JwtUtils.generateToken(savedUser.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User saved successfully");
            response.put("token", userIdToken);
            response.put("user", user);
            response.put("organizations", new ArrayList<>());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error while setting user details");
            return ResponseEntity.status(500).body(response);
        }
    }

    // Define other controller methods (login, getAllUserOrganizations, getStarted, getUserDetails) here
}
