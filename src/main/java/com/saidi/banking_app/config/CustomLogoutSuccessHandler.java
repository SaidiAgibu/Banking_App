package com.saidi.banking_app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.io.PrintWriter;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final ObjectMapper objectMapper;

    public CustomLogoutSuccessHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }



    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");

        PrintWriter writer = response.getWriter();
        writer.println(objectMapper.writeValueAsString("Logout successful"));
        writer.flush();
    }
}

