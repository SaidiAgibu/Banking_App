package com.saidi.banking_app.services;

import com.saidi.banking_app.util.EmailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImplementation implements EmailService{


    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${VERIFY_EMAIL_HOST}")
    private String host;


    private final JavaMailSender mailSender;
    @Override
    public void sendEmailToUser(String name, String to, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("Banking App Account Verification");
            message.setFrom(fromEmail);
            message.setText(EmailUtil.getEmailMessage(name,host,token));
            message.setTo(to);

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
