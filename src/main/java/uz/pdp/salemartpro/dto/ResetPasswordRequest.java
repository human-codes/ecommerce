package uz.pdp.salemartpro.dto;

import lombok.Value;

@Value
public class ResetPasswordRequest {
    String email;
    String code;
    String newPassword;
}
