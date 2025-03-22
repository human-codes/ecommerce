package uz.pdp.salemartpro.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class DeliverUpdateRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
     String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
     String email;

    @NotBlank(message = "Phone is required")
     String phone;

     Boolean online;

     String vehicleNumber;




}
