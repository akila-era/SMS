package com.hexalyte.salon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom("noreply@salon.com");
            
            mailSender.send(message);
        } catch (Exception e) {
            // Log error but don't throw exception to avoid breaking appointment flow
            System.err.println("Failed to send email to " + to + ": " + e.getMessage());
        }
    }
}


