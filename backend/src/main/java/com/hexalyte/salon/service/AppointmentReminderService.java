package com.hexalyte.salon.service;

import com.hexalyte.salon.model.Appointment;
import com.hexalyte.salon.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class AppointmentReminderService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private NotificationService notificationService;

    // Send 24-hour reminders (runs every hour)
    @Scheduled(fixedRate = 3600000) // 1 hour in milliseconds
    public void send24HourReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Appointment> tomorrowAppointments = appointmentRepository.findByAppointmentDate(tomorrow);
        
        for (Appointment appointment : tomorrowAppointments) {
            if (appointment.getStatus() == Appointment.Status.BOOKED) {
                notificationService.sendAppointmentReminder(appointment);
            }
        }
    }

    // Send 2-hour reminders (runs every 15 minutes)
    @Scheduled(fixedRate = 900000) // 15 minutes in milliseconds
    public void send2HourReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twoHoursFromNow = now.plusHours(2);
        
        // Find appointments starting in approximately 2 hours
        List<Appointment> upcomingAppointments = appointmentRepository.findUpcomingAppointments(
                LocalDate.now().plusDays(1));
        
        for (Appointment appointment : upcomingAppointments) {
            if (appointment.getStatus() == Appointment.Status.BOOKED) {
                LocalDateTime appointmentDateTime = LocalDateTime.of(
                        appointment.getAppointmentDate(), appointment.getStartTime());
                
                // Check if appointment is within 2-3 hours from now
                if (appointmentDateTime.isAfter(now.plusHours(2)) && 
                    appointmentDateTime.isBefore(now.plusHours(3))) {
                    send2HourReminder(appointment);
                }
            }
        }
    }

    // Send 30-minute reminders (runs every 5 minutes)
    @Scheduled(fixedRate = 300000) // 5 minutes in milliseconds
    public void send30MinuteReminders() {
        LocalDateTime now = LocalDateTime.now();
        
        // Find today's appointments
        List<Appointment> todaysAppointments = appointmentRepository.findTodaysAppointments();
        
        for (Appointment appointment : todaysAppointments) {
            if (appointment.getStatus() == Appointment.Status.BOOKED) {
                LocalDateTime appointmentDateTime = LocalDateTime.of(
                        appointment.getAppointmentDate(), appointment.getStartTime());
                
                // Check if appointment is within 30-45 minutes from now
                if (appointmentDateTime.isAfter(now.plusMinutes(30)) && 
                    appointmentDateTime.isBefore(now.plusMinutes(45))) {
                    send30MinuteReminder(appointment);
                }
            }
        }
    }

    // Send no-show follow-ups (runs daily at 6 PM)
    @Scheduled(cron = "0 0 18 * * ?") // 6 PM daily
    public void sendNoShowFollowUps() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<Appointment> yesterdayAppointments = appointmentRepository.findByAppointmentDate(yesterday);
        
        for (Appointment appointment : yesterdayAppointments) {
            if (appointment.getStatus() == Appointment.Status.NO_SHOW) {
                sendNoShowFollowUp(appointment);
            }
        }
    }

    // Clean up old appointments (runs daily at midnight)
    @Scheduled(cron = "0 0 0 * * ?") // Midnight daily
    public void cleanupOldAppointments() {
        // This could include archiving old completed appointments
        // or updating statuses based on business rules
        System.out.println("Running daily cleanup for old appointments...");
    }

    private void send2HourReminder(Appointment appointment) {
        String message = String.format(
            "2-Hour Reminder: You have an appointment at %s on %s from %s to %s. " +
            "Please arrive 10 minutes early. Services: %s",
            appointment.getBranch().getName(),
            appointment.getAppointmentDate(),
            appointment.getStartTime(),
            appointment.getEndTime(),
            appointment.getAppointmentServices().stream()
                .map(appService -> appService.getService().getName())
                .reduce((a, b) -> a + ", " + b)
                .orElse("")
        );

        notificationService.sendSms(appointment.getCustomer().getPhone(), message);
        
        if (appointment.getCustomer().getEmail() != null && !appointment.getCustomer().getEmail().isEmpty()) {
            notificationService.sendEmail(appointment.getCustomer().getEmail(), "2-Hour Appointment Reminder", message);
        }
    }

    private void send30MinuteReminder(Appointment appointment) {
        String message = String.format(
            "30-Minute Reminder: Your appointment at %s starts at %s. " +
            "Please arrive soon! Services: %s",
            appointment.getBranch().getName(),
            appointment.getStartTime(),
            appointment.getAppointmentServices().stream()
                .map(appService -> appService.getService().getName())
                .reduce((a, b) -> a + ", " + b)
                .orElse("")
        );

        notificationService.sendSms(appointment.getCustomer().getPhone(), message);
        
        if (appointment.getCustomer().getEmail() != null && !appointment.getCustomer().getEmail().isEmpty()) {
            notificationService.sendEmail(appointment.getCustomer().getEmail(), "30-Minute Appointment Reminder", message);
        }
    }

    private void sendNoShowFollowUp(Appointment appointment) {
        String message = String.format(
            "We missed you at your appointment yesterday at %s. " +
            "We hope everything is okay. Please call us to reschedule if needed.",
            appointment.getBranch().getName()
        );

        notificationService.sendSms(appointment.getCustomer().getPhone(), message);
        
        if (appointment.getCustomer().getEmail() != null && !appointment.getCustomer().getEmail().isEmpty()) {
            notificationService.sendEmail(appointment.getCustomer().getEmail(), "We Missed You", message);
        }
    }
}
