package uz.pdp.salemartpro.controller;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.salemartpro.entity.User;
import uz.pdp.salemartpro.service.UserService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final UserService userService;

    @GetMapping("/user")
    public ResponseEntity<?> getUsers(){
        System.out.println("User endpoint called");
        try {
            List<User> allUsers = userService.getAllUsers();
            System.out.println("Retrieved " + allUsers.size() + " users from database");

            // Convert users to simplified format with only needed fields
            @NotNull List<Map<String, ? extends Serializable>> simplifiedUsers = allUsers.stream()
                    .map(user -> Map.of(
                            "id", user.getId(),
                            "username", user.getUsername() != null ? user.getUsername() : "",
                            "phone", user.getPhone() != null ? user.getPhone() : "",
                            "email", user.getEmail() != null ? user.getEmail() : "",
                            "status", user.getStep() != null ? user.getStep().toString() : "ACTIVE"
                    ))
                    .collect(Collectors.toList());

            System.out.println("Returning " + simplifiedUsers.size() + " users");
            return ResponseEntity.ok(simplifiedUsers);
        } catch (Exception e) {
            System.err.println("Error retrieving users: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Failed to retrieve users: " + e.getMessage()));
        }
    }
}
