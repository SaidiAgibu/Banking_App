package com.saidi.banking_app.response;

import lombok.Data;

@Data
public class LoginResponse {
    private String tokenType = "Bearer ";
    private String accessToken;

    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
