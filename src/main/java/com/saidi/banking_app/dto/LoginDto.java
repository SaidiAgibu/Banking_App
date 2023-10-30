package com.saidi.banking_app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginDto {
    @Email
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
}
