package com.hexalyte.salon.service;

import com.hexalyte.salon.model.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private SmsService smsService;

    public void sendAppointmentConfirmation(Appointment appointment) {
        String message = String.format(
            "Appointment confirmed for %s at %s on %s from %s to %s. Services: %s",
            appointment.getCustomer().getFullName(),
            appointment.getBranch().getName(),
            appointment.getAppointmentDate(),
            appointment.getStartTime(),
            appointment.getEndTime(),
            appointment.getAppointmentServices().stream()
                .map(appService -> appService.getService().getName())
                .reduce((a, b) -> a + ", " + b)
                .orElse("")
        );

        // Send SMS
        smsService.sendSms(appointment.getCustomer().getPhone(), message);
        
        // Send Email if available
        if (appointment.getCustomer().getEmail() != null && !appointment.getCustomer().getEmail().isEmpty()) {
            emailService.sendEmail(appointment.getCustomer().getEmail(), "Appointment Confirmation", message);
        }
    }

    public void sendAppointmentStatusUpdate(Appointment appointment) {
        String message = String.format(
            "Your appointment status has been updated to %s for %s at %s on %s",
            appointment.getStatus(),
            appointment.getBranch().getName(),
            appointment.getAppointmentDate(),
            appointment.getStartTime()
        );

        smsService.sendSms(appointment.getCustomer().getPhone(), message);
        
        if (appointment.getCustomer().getEmail() != null && !appointment.getCustomer().getEmail().isEmpty()) {
            emailService.sendEmail(appointment.getCustomer().getEmail(), "Appointment Status Update", message);
        }
    }

    public void sendAppointmentCancellation(Appointment appointment) {
        String message = String.format(
            "Your appointment for %s at %s on %s has been cancelled. We apologize for any inconvenience.",
            appointment.getBranch().getName(),
            appointment.getAppointmentDate(),
            appointment.getStartTime()
        );

        smsService.sendSms(appointment.getCustomer().getPhone(), message);
        
        if (appointment.getCustomer().getEmail() != null && !appointment.getCustomer().getEmail().isEmpty()) {
            emailService.sendEmail(appointment.getCustomer().getEmail(), "Appointment Cancelled", message);
        }
    }

    public void sendAppointmentReschedule(Appointment appointment) {
        String message = String.format(
            "Your appointment has been rescheduled to %s at %s on %s from %s to %s",
            appointment.getBranch().getName(),
            appointment.getAppointmentDate(),
            appointment.getStartTime(),
            appointment.getEndTime()
        );

        smsService.sendSms(appointment.getCustomer().getPhone(), message);
        
        if (appointment.getCustomer().getEmail() != null && !appointment.getCustomer().getEmail().isEmpty()) {
            emailService.sendEmail(appointment.getCustomer().getEmail(), "Appointment Rescheduled", message);
        }
    }

    public void sendAppointmentReminder(Appointment appointment) {
        String message = String.format(
            "Reminder: You have an appointment tomorrow at %s on %s from %s to %s. Services: %s",
            appointment.getBranch().getName(),
            appointment.getAppointmentDate(),
            appointment.getStartTime(),
            appointment.getEndTime(),
            appointment.getAppointmentServices().stream()
                .map(appService -> appService.getService().getName())
                .reduce((a, b) -> a + ", " + b)
                .orElse("")
        );

        smsService.sendSms(appointment.getCustomer().getPhone(), message);
        
        if (appointment.getCustomer().getEmail() != null && !appointment.getCustomer().getEmail().isEmpty()) {
            emailService.sendEmail(appointment.getCustomer().getEmail(), "Appointment Reminder", message);
        }
    }
}


