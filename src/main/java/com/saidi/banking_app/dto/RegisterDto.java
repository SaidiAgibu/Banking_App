package com.saidi.banking_app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterDto {
    @NotBlank(message = "First Name is required")
    private String firstName;
    @NotBlank(message = "Last Name is required")
    private String lastName;
    @Email
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
    @NotBlank(message = "Phone Number is required")
    private String phoneNumber;
    @NotBlank(message = "Address is required")
    private String address;
}
