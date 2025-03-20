package uz.pdp.salemartpro.dto;


import lombok.Value;
import uz.pdp.salemartpro.entity.Role;
import uz.pdp.salemartpro.entity.enums.RoleName;

import java.util.List;
import java.util.Set;

@Value
public class RegisterDto {
     String username;
     String email;
     String password;
     String phone;

     String vehicleNumber;

     List<RoleName> roles;
}