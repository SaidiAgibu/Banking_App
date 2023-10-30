package com.saidi.banking_app.controllers;

import com.saidi.banking_app.dto.LoginDto;
import com.saidi.banking_app.dto.RegisterDto;
import com.saidi.banking_app.exceptions.AlreadyExistsException;
import com.saidi.banking_app.response.LoginResponse;
import com.saidi.banking_app.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterDto registerDto) throws AlreadyExistsException {
        return userService.registerUser(registerDto);
    }

    @GetMapping
    public ResponseEntity<String> verifyAccount(@RequestParam("token") String token) {
        return userService.verifyToken(token);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginDto loginDto) {
        return userService.loginUser(loginDto);
    }

}
