package uz.pdp.salemartpro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.salemartpro.dto.*;
import uz.pdp.salemartpro.entity.User;
import uz.pdp.salemartpro.repo.UserRepository;
import uz.pdp.salemartpro.security.JwtService;
import uz.pdp.salemartpro.email.CodeGenerator;
import uz.pdp.salemartpro.service.RedisService;
import uz.pdp.salemartpro.service.EmailService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final EmailService emailService;

    @Autowired
    public UserService(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository, PasswordEncoder passwordEncoder, RedisService redisService, EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.redisService = redisService;
        this.emailService = emailService;
    }

    public ResponseEntity<LoginResponse> authenticateUser(LoginDto loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }

        User user = userOptional.get();
        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new LoginResponse(token));
    }

    public void sendVerificationCode(String email) {
        String code = CodeGenerator.generateCode();
        redisService.saveVerificationCode(email, code, 5); // Store for 5 minutes

        System.out.println("✅ Code generated: " + code + " for email: " + email);

        emailService.sendVerificationEmail(email, code);
    }

    public boolean verifyCode(String email, String code) {
        String storedCode = redisService.getVerificationCode(email);
        if (storedCode != null && storedCode.equals(code)) {
            redisService.deleteVerificationCode(email);
            return true;
        }
        return false;
    }

    public boolean resetPassword(String email, String code, String newPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            System.out.println("❌ No user found with email: " + email);
            return false;
        }

        String storedCode = redisService.getVerificationCode(email);
        System.out.println("🔍 Retrieved code from Redis for " + email + ": " + storedCode);

        if (storedCode == null || !storedCode.equals(code)) {
            System.out.println("❌ Invalid code: " + code + " (Expected: " + storedCode + ")");
            return false;
        }

        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        redisService.deleteVerificationCode(email);
        System.out.println("✅ Password reset successfully for: " + email);

        return true;
    }

    public ResponseEntity<String> checkExistence(String username, String email, String phone) {
        if (userRepository.existsByUsername(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This username is already taken.");
        }
        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This email is already in use.");
        }
        if (userRepository.existsByPhone(phone)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This phone number is already in use.");
        }
        return ResponseEntity.ok("User details are unique.");
    }

    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new UserDto(user.getUsername(), user.getEmail(),user.getPhone());
    }

    public List<User> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            System.out.println("Found " + users.size() + " users in database");
            return users;
        } catch (Exception e) {
            System.err.println("Error in getAllUsers: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); // Return empty list instead of null to prevent NPE
        }
    }
}