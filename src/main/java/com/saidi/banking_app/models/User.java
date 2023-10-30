package com.saidi.banking_app.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class User {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    private Long userId;
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
    private LocalDateTime createdAt;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Account> accounts;
    private boolean enabled;
}
