package com.hexalyte.salon.service;

import org.springframework.stereotype.Service;

@Service
public class SmsService {

    public void sendSms(String phoneNumber, String message) {
        try {
            // TODO: Integrate with actual SMS provider (Twilio, etc.)
            // For now, just log the message
            System.out.println("SMS to " + phoneNumber + ": " + message);
            
            // In production, this would be:
            // Twilio.init(accountSid, authToken);
            // Message.creator(
            //     new PhoneNumber(phoneNumber),
            //     new PhoneNumber("+1234567890"), // Your Twilio number
            //     message
            // ).create();
            
        } catch (Exception e) {
            // Log error but don't throw exception to avoid breaking appointment flow
            System.err.println("Failed to send SMS to " + phoneNumber + ": " + e.getMessage());
        }
    }
}
