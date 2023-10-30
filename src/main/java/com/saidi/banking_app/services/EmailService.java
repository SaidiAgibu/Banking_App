package com.saidi.banking_app.services;

public interface EmailService {

    public void sendEmailToUser(String name, String to, String token);
}
