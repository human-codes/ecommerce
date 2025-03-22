package uz.pdp.salemartpro.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import uz.pdp.salemartpro.dto.*;
import uz.pdp.salemartpro.entity.Inquiry;
import uz.pdp.salemartpro.entity.User;
import uz.pdp.salemartpro.entity.enums.RoleName;
import uz.pdp.salemartpro.repo.InquiryRepository;
import uz.pdp.salemartpro.repo.RoleRepository;
import uz.pdp.salemartpro.repo.UserRepository;
import uz.pdp.salemartpro.service.RedisService;
import uz.pdp.salemartpro.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final RedisService redisService;
    private final InquiryRepository inquiryRepository;

    public UserController(UserService userService, UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, RedisService redisService,
                          InquiryRepository inquiryRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.redisService = redisService;
        this.inquiryRepository = inquiryRepository;
    }

    @PostMapping("/check-existence")
    public ResponseEntity<?> checkExistence(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String phone
    ) {
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

    // Other endpoints (login, register, send-code, reset-password, etc.)
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDto loginRequest) {
        return userService.authenticateUser(loginRequest);
    }

    @PostMapping("/send-code")
    public ResponseEntity<String> sendVerificationCode(@RequestParam String email) {
        userService.sendVerificationCode(email);
        return ResponseEntity.ok("Verification code sent to your email.");
    }

    @PostMapping("/send-reset-code")
    public ResponseEntity<String> sendResetVerificationCode(@RequestParam String email) {
        if (!userRepository.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email. No such email exists.");
        }
        userService.sendVerificationCode(email);
        return ResponseEntity.ok("Verification code sent to your email.");
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDto registerRequest, @RequestParam String code) {
        // Check if the verification code is valid
        String storedCode = redisService.getVerificationCode(registerRequest.getEmail());
        if (storedCode == null || !storedCode.equals(code)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired verification code.");
        }

        // Create and save the user
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setPhone(registerRequest.getPhone());
        user.setRoles(List.of(roleRepository.findAll().get(1)));

        userRepository.save(user);
        redisService.deleteVerificationCode(registerRequest.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {

        boolean isReset = userService.resetPassword(request.getEmail(), request.getCode(), request.getNewPassword());
        if (isReset) {
            return ResponseEntity.ok("Password reset successfully!");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification code or email.");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        UserDto user = userService.findByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update/{username}")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateDto userUpdateDto, @PathVariable String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (userUpdateDto.getUsername() != null) {
            user.setUsername(userUpdateDto.getUsername());
        }

        if (userUpdateDto.getPhoneNumber() != null) {
            user.setPhone(userUpdateDto.getPhoneNumber());
        }

        userRepository.save(user);

        return ResponseEntity.ok("Profile updated successfully!");
    }


    @PostMapping("/set-inquiry")
    public ResponseEntity<String> saveInquiry(@RequestBody InquiryDto inquiryDto) {
        Inquiry inquiry = new Inquiry();
        inquiry.setFullName(inquiryDto.getFullName());
        inquiry.setEmail(inquiryDto.getEmail());
        inquiry.setPhone(inquiryDto.getPhone());
        inquiry.setMessage(inquiryDto.getMessage());
        inquiry.setReviewed(false); // Default: false
        inquiryRepository.save(inquiry);
        return ResponseEntity.ok("Inquiry submitted successfully");
    }

    @PutMapping("/inquiry/{id}")
    public ResponseEntity<String> markAsReviewed(@PathVariable Integer id, @RequestBody Map<String, String> request) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inquiry not found"));

        if (inquiry.getReviewed()) {
            return ResponseEntity.badRequest().body("Inquiry already reviewed");
        }
        String reviewedBy = request.get("reviewedBy");
        System.out.println(reviewedBy);// JSON'dan `reviewedBy` ni olamiz
        Optional<User> user = userRepository.findByUsername(reviewedBy);
        if (user.isPresent()) {
            System.out.println(user);
        } else {
            System.out.println("user yo'q");
        }
        inquiry.setReviewed(true);
        inquiryRepository.save(inquiry);
        return ResponseEntity.ok("Inquiry marked as reviewed");
    }

    @GetMapping("/inquiries")
    public ResponseEntity<List<Inquiry>> getAllInquiries() {
        return ResponseEntity.ok(inquiryRepository.findAll());
    }
}