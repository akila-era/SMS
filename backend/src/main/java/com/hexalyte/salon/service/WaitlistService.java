package com.hexalyte.salon.service;

import com.hexalyte.salon.dto.WaitlistDTO;
import com.hexalyte.salon.dto.AppointmentDTO;
import com.hexalyte.salon.model.*;
import com.hexalyte.salon.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class WaitlistService {

    @Autowired
    private WaitlistRepository waitlistRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private NotificationService notificationService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Add customer to waitlist
    public WaitlistDTO addToWaitlist(WaitlistDTO waitlistDTO) {
        // Validate entities
        Customer customer = customerRepository.findById(waitlistDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        Staff staff = staffRepository.findById(waitlistDTO.getStaffId())
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        
        Branch branch = branchRepository.findById(waitlistDTO.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        // Check if customer is already on waitlist for this time slot
        List<Waitlist> existingEntries = waitlistRepository.findByCustomerIdAndStatus(
                waitlistDTO.getCustomerId(), Waitlist.Status.ACTIVE);
        
        for (Waitlist entry : existingEntries) {
            if (entry.getStaff().getId().equals(waitlistDTO.getStaffId()) &&
                entry.getPreferredDate().equals(waitlistDTO.getPreferredDate())) {
                throw new RuntimeException("Customer is already on waitlist for this time slot");
            }
        }

        // Create waitlist entry
        Waitlist waitlist = new Waitlist();
        waitlist.setCustomer(customer);
        waitlist.setStaff(staff);
        waitlist.setBranch(branch);
        waitlist.setPreferredDate(waitlistDTO.getPreferredDate());
        waitlist.setPreferredStartTime(waitlistDTO.getPreferredStartTime());
        waitlist.setPreferredEndTime(waitlistDTO.getPreferredEndTime());
        waitlist.setFlexibleDays(waitlistDTO.getFlexibleDays());
        waitlist.setFlexibleHours(waitlistDTO.getFlexibleHours());
        waitlist.setNotes(waitlistDTO.getNotes());
        waitlist.setPriority(waitlistDTO.getPriority());
        waitlist.setStatus(Waitlist.Status.ACTIVE);
        waitlist.setExpiresAt(LocalDateTime.now().plusDays(7)); // Expires in 7 days

        Waitlist savedWaitlist = waitlistRepository.save(waitlist);
        return convertToDTO(savedWaitlist);
    }

    // Get waitlist entries by staff
    public List<WaitlistDTO> getWaitlistByStaff(Long staffId) {
        return waitlistRepository.findByStaffIdAndStatusOrderByPriorityDescCreatedAtAsc(
                staffId, Waitlist.Status.ACTIVE).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get waitlist entries by branch
    public List<WaitlistDTO> getWaitlistByBranch(Long branchId) {
        return waitlistRepository.findByBranchIdAndStatusOrderByPriorityDescCreatedAtAsc(
                branchId, Waitlist.Status.ACTIVE).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get waitlist entries by customer
    public List<WaitlistDTO> getWaitlistByCustomer(Long customerId) {
        return waitlistRepository.findByCustomerIdAndStatusOrderByCreatedAtDesc(
                customerId, Waitlist.Status.ACTIVE).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Convert waitlist entry to appointment
    public AppointmentDTO convertWaitlistToAppointment(Long waitlistId, LocalDate appointmentDate, 
                                                     LocalTime startTime, LocalTime endTime) {
        Waitlist waitlist = waitlistRepository.findById(waitlistId)
                .orElseThrow(() -> new RuntimeException("Waitlist entry not found"));

        if (waitlist.getStatus() != Waitlist.Status.ACTIVE) {
            throw new RuntimeException("Waitlist entry is not active");
        }

        // Create appointment DTO
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setCustomerId(waitlist.getCustomer().getId());
        appointmentDTO.setStaffId(waitlist.getStaff().getId());
        appointmentDTO.setBranchId(waitlist.getBranch().getId());
        appointmentDTO.setAppointmentDate(appointmentDate);
        appointmentDTO.setStartTime(startTime);
        appointmentDTO.setEndTime(endTime);
        appointmentDTO.setNotes(waitlist.getNotes() + " (Converted from waitlist)");

        // Create appointment
        AppointmentDTO createdAppointment = appointmentService.createAppointment(appointmentDTO);

        // Update waitlist status
        waitlist.setStatus(Waitlist.Status.CONVERTED);
        waitlistRepository.save(waitlist);

        // Send notification
        notificationService.sendWaitlistConversionNotification(waitlist, createdAppointment);

        return createdAppointment;
    }

    // Notify waitlist customers about availability
    public void notifyWaitlistCustomers(Long staffId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        List<Waitlist> matchingEntries = waitlistRepository.findMatchingWaitlistEntries(
                staffId, date, startTime, endTime);

        for (Waitlist waitlist : matchingEntries) {
            if (waitlist.getStatus() == Waitlist.Status.ACTIVE) {
                // Send notification
                notificationService.sendWaitlistAvailabilityNotification(waitlist, date, startTime, endTime);
                
                // Update status to notified
                waitlist.setStatus(Waitlist.Status.NOTIFIED);
                waitlist.setNotifiedAt(LocalDateTime.now());
                waitlistRepository.save(waitlist);
            }
        }
    }

    // Remove from waitlist
    public void removeFromWaitlist(Long waitlistId, String reason) {
        Waitlist waitlist = waitlistRepository.findById(waitlistId)
                .orElseThrow(() -> new RuntimeException("Waitlist entry not found"));

        waitlist.setStatus(Waitlist.Status.CANCELLED);
        if (reason != null && !reason.isEmpty()) {
            waitlist.setNotes((waitlist.getNotes() != null ? waitlist.getNotes() + "\n" : "") + 
                             "Removal reason: " + reason);
        }
        waitlistRepository.save(waitlist);
    }

    // Clean up expired waitlist entries
    public void cleanupExpiredEntries() {
        List<Waitlist> expiredEntries = waitlistRepository.findExpiredWaitlistEntries(LocalDateTime.now());
        
        for (Waitlist waitlist : expiredEntries) {
            waitlist.setStatus(Waitlist.Status.EXPIRED);
            waitlistRepository.save(waitlist);
        }
    }

    // Get waitlist statistics
    public WaitlistStatsDTO getWaitlistStats(Long branchId) {
        WaitlistStatsDTO stats = new WaitlistStatsDTO();
        stats.setTotalActive(waitlistRepository.countByBranchIdAndStatus(branchId, Waitlist.Status.ACTIVE));
        stats.setTotalNotified(waitlistRepository.countByBranchIdAndStatus(branchId, Waitlist.Status.NOTIFIED));
        stats.setTotalConverted(waitlistRepository.countByBranchIdAndStatus(branchId, Waitlist.Status.CONVERTED));
        stats.setTotalExpired(waitlistRepository.countByBranchIdAndStatus(branchId, Waitlist.Status.EXPIRED));
        return stats;
    }

    private WaitlistDTO convertToDTO(Waitlist waitlist) {
        WaitlistDTO dto = new WaitlistDTO();
        dto.setId(waitlist.getId());
        dto.setCustomerId(waitlist.getCustomer().getId());
        dto.setCustomerName(waitlist.getCustomer().getFullName());
        dto.setCustomerPhone(waitlist.getCustomer().getPhone());
        dto.setCustomerEmail(waitlist.getCustomer().getEmail());
        dto.setStaffId(waitlist.getStaff().getId());
        dto.setStaffName(waitlist.getStaff().getFullName());
        dto.setBranchId(waitlist.getBranch().getId());
        dto.setBranchName(waitlist.getBranch().getName());
        dto.setPreferredDate(waitlist.getPreferredDate());
        dto.setPreferredStartTime(waitlist.getPreferredStartTime());
        dto.setPreferredEndTime(waitlist.getPreferredEndTime());
        dto.setFlexibleDays(waitlist.getFlexibleDays());
        dto.setFlexibleHours(waitlist.getFlexibleHours());
        dto.setNotes(waitlist.getNotes());
        dto.setStatus(waitlist.getStatus().name());
        dto.setPriority(waitlist.getPriority());

        if (waitlist.getCreatedAt() != null) {
            dto.setCreatedAt(waitlist.getCreatedAt().format(formatter));
        }
        if (waitlist.getNotifiedAt() != null) {
            dto.setNotifiedAt(waitlist.getNotifiedAt().format(formatter));
        }
        if (waitlist.getExpiresAt() != null) {
            dto.setExpiresAt(waitlist.getExpiresAt().format(formatter));
        }

        return dto;
    }

    // Inner class for waitlist statistics
    public static class WaitlistStatsDTO {
        private Long totalActive;
        private Long totalNotified;
        private Long totalConverted;
        private Long totalExpired;

        // Getters and Setters
        public Long getTotalActive() { return totalActive; }
        public void setTotalActive(Long totalActive) { this.totalActive = totalActive; }
        public Long getTotalNotified() { return totalNotified; }
        public void setTotalNotified(Long totalNotified) { this.totalNotified = totalNotified; }
        public Long getTotalConverted() { return totalConverted; }
        public void setTotalConverted(Long totalConverted) { this.totalConverted = totalConverted; }
        public Long getTotalExpired() { return totalExpired; }
        public void setTotalExpired(Long totalExpired) { this.totalExpired = totalExpired; }
    }
}
