package com.hexalyte.salon.service;

import com.hexalyte.salon.dto.*;
import com.hexalyte.salon.model.*;
import com.hexalyte.salon.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@Transactional
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private CommissionService commissionService;

    @Autowired
    private NotificationService notificationService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Basic CRUD Operations
    public List<AppointmentDTO> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AppointmentDTO> getAppointmentsByBranch(Long branchId) {
        return appointmentRepository.findByBranchId(branchId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AppointmentDTO> getAppointmentsByStaff(Long staffId) {
        return appointmentRepository.findByStaffId(staffId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AppointmentDTO> getAppointmentsByCustomer(Long customerId) {
        return appointmentRepository.findByCustomerId(customerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AppointmentDTO> getAppointmentsByDate(LocalDate date) {
        return appointmentRepository.findByAppointmentDate(date).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AppointmentDTO> getTodaysAppointments() {
        return appointmentRepository.findTodaysAppointments().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AppointmentDTO> getTodaysAppointmentsByBranch(Long branchId) {
        return appointmentRepository.findTodaysAppointmentsByBranch(branchId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AppointmentDTO> getUpcomingAppointments(int days) {
        LocalDate endDate = LocalDate.now().plusDays(days);
        return appointmentRepository.findUpcomingAppointments(endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AppointmentDTO getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
        return convertToDTO(appointment);
    }

    // Create Appointment with Validation
    public AppointmentDTO createAppointment(AppointmentDTO appointmentDTO) {
        // Validate time conflicts
        validateTimeSlot(appointmentDTO.getStaffId(), appointmentDTO.getAppointmentDate(), 
                        appointmentDTO.getStartTime(), appointmentDTO.getEndTime());

        // Get entities
        Customer customer = customerRepository.findById(appointmentDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        Staff staff = staffRepository.findById(appointmentDTO.getStaffId())
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        
        Branch branch = branchRepository.findById(appointmentDTO.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        // Create appointment
        Appointment appointment = new Appointment();
        appointment.setCustomer(customer);
        appointment.setStaff(staff);
        appointment.setBranch(branch);
        appointment.setAppointmentDate(appointmentDTO.getAppointmentDate());
        appointment.setStartTime(appointmentDTO.getStartTime());
        appointment.setEndTime(appointmentDTO.getEndTime());
        appointment.setStatus(Appointment.Status.BOOKED);
        appointment.setNotes(appointmentDTO.getNotes());

        // Calculate total amount and create appointment services
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (AppointmentServiceDTO serviceDTO : appointmentDTO.getServices()) {
            com.hexalyte.salon.model.Service service = serviceRepository.findById(serviceDTO.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service not found: " + serviceDTO.getServiceId()));
            
            com.hexalyte.salon.model.AppointmentService appointmentService = new com.hexalyte.salon.model.AppointmentService();
            appointmentService.setAppointment(appointment);
            appointmentService.setService(service);
            appointmentService.setPrice(serviceDTO.getPrice());
            appointmentService.setCommissionRate(serviceDTO.getCommissionRate());
            
            appointment.getAppointmentServices().add(appointmentService);
            totalAmount = totalAmount.add(serviceDTO.getPrice());
        }
        
        appointment.setTotalAmount(totalAmount);
        Appointment savedAppointment = appointmentRepository.save(appointment);

        // Send confirmation notification
        notificationService.sendAppointmentConfirmation(savedAppointment);

        return convertToDTO(savedAppointment);
    }

    // Update Appointment
    public AppointmentDTO updateAppointment(Long id, AppointmentDTO appointmentDTO) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));

        // Validate time conflicts (excluding current appointment)
        validateTimeSlotForUpdate(id, appointmentDTO.getStaffId(), appointmentDTO.getAppointmentDate(), 
                                 appointmentDTO.getStartTime(), appointmentDTO.getEndTime());

        // Update basic fields
        Customer customer = customerRepository.findById(appointmentDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        appointment.setCustomer(customer);

        Staff staff = staffRepository.findById(appointmentDTO.getStaffId())
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        appointment.setStaff(staff);

        Branch branch = branchRepository.findById(appointmentDTO.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found"));
        appointment.setBranch(branch);

        appointment.setAppointmentDate(appointmentDTO.getAppointmentDate());
        appointment.setStartTime(appointmentDTO.getStartTime());
        appointment.setEndTime(appointmentDTO.getEndTime());
        appointment.setNotes(appointmentDTO.getNotes());

        // Update services
        appointment.getAppointmentServices().clear();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (AppointmentServiceDTO serviceDTO : appointmentDTO.getServices()) {
            com.hexalyte.salon.model.Service service = serviceRepository.findById(serviceDTO.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service not found: " + serviceDTO.getServiceId()));
            
            com.hexalyte.salon.model.AppointmentService appointmentService = new com.hexalyte.salon.model.AppointmentService();
            appointmentService.setAppointment(appointment);
            appointmentService.setService(service);
            appointmentService.setPrice(serviceDTO.getPrice());
            appointmentService.setCommissionRate(serviceDTO.getCommissionRate());
            
            appointment.getAppointmentServices().add(appointmentService);
            totalAmount = totalAmount.add(serviceDTO.getPrice());
        }
        appointment.setTotalAmount(totalAmount);

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return convertToDTO(savedAppointment);
    }

    // Update Appointment Status
    public AppointmentDTO updateAppointmentStatus(Long id, String status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));

        Appointment.Status newStatus = Appointment.Status.valueOf(status.toUpperCase());
        appointment.setStatus(newStatus);

        // If completed, calculate commissions
        if (newStatus == Appointment.Status.COMPLETED) {
            commissionService.calculateCommissions(appointment);
        }

        // Send status update notification
        notificationService.sendAppointmentStatusUpdate(appointment);

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return convertToDTO(savedAppointment);
    }

    // Cancel Appointment
    public AppointmentDTO cancelAppointment(Long id, String reason) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));

        appointment.setStatus(Appointment.Status.CANCELLED);
        if (reason != null && !reason.isEmpty()) {
            appointment.setNotes((appointment.getNotes() != null ? appointment.getNotes() + "\n" : "") + 
                               "Cancellation reason: " + reason);
        }

        // Send cancellation notification
        notificationService.sendAppointmentCancellation(appointment);

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return convertToDTO(savedAppointment);
    }

    // Reschedule Appointment
    public AppointmentDTO rescheduleAppointment(Long id, LocalDate newDate, LocalTime newStartTime, LocalTime newEndTime) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));

        // Validate new time slot
        validateTimeSlotForUpdate(id, appointment.getStaff().getId(), newDate, newStartTime, newEndTime);

        appointment.setAppointmentDate(newDate);
        appointment.setStartTime(newStartTime);
        appointment.setEndTime(newEndTime);

        // Send reschedule notification
        notificationService.sendAppointmentReschedule(appointment);

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return convertToDTO(savedAppointment);
    }

    // Delete Appointment
    public void deleteAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
        
        if (appointment.getStatus() == Appointment.Status.COMPLETED) {
            throw new RuntimeException("Cannot delete completed appointment");
        }

        appointmentRepository.delete(appointment);
    }

    // Availability Management
    public AvailabilityDTO getStaffAvailability(Long staffId, LocalDate date) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found with id: " + staffId));

        List<Appointment> existingAppointments = appointmentRepository.findStaffAppointmentsForDate(staffId, date);
        List<TimeSlotDTO> availableSlots = generateAvailableTimeSlots(existingAppointments, date);
        List<TimeSlotDTO> bookedSlots = convertAppointmentsToTimeSlots(existingAppointments);

        AvailabilityDTO availability = new AvailabilityDTO();
        availability.setStaffId(staffId);
        availability.setStaffName(staff.getFullName());
        availability.setBranchId(staff.getBranch().getId());
        availability.setBranchName(staff.getBranch().getName());
        availability.setDate(date);
        availability.setAvailableSlots(availableSlots);
        availability.setBookedSlots(bookedSlots);

        return availability;
    }

    public List<AvailabilityDTO> getBranchAvailability(Long branchId, LocalDate date) {
        List<Staff> staffList = staffRepository.findByBranchIdAndIsActiveTrue(branchId);
        return staffList.stream()
                .map(staff -> getStaffAvailability(staff.getId(), date))
                .collect(Collectors.toList());
    }

    // Validation Methods
    private void validateTimeSlot(Long staffId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        List<Appointment> conflicts = appointmentRepository.findConflictingAppointments(staffId, date, startTime, endTime);
        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Time slot conflicts with existing appointment");
        }
    }

    private void validateTimeSlotForUpdate(Long appointmentId, Long staffId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        List<Appointment> conflicts = appointmentRepository.findConflictingAppointments(staffId, date, startTime, endTime);
        conflicts = conflicts.stream()
                .filter(appointment -> !appointment.getId().equals(appointmentId))
                .collect(Collectors.toList());
        
        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Time slot conflicts with existing appointment");
        }
    }

    // Helper Methods
    private List<TimeSlotDTO> generateAvailableTimeSlots(List<Appointment> existingAppointments, LocalDate date) {
        List<TimeSlotDTO> slots = new ArrayList<>();
        LocalTime startTime = LocalTime.of(9, 0); // Salon opens at 9 AM
        LocalTime endTime = LocalTime.of(18, 0);  // Salon closes at 6 PM
        LocalTime currentTime = startTime;

        while (currentTime.isBefore(endTime)) {
            LocalTime slotEnd = currentTime.plusMinutes(30); // 30-minute slots
            if (slotEnd.isAfter(endTime)) break;

            boolean isAvailable = true;
            for (Appointment appointment : existingAppointments) {
                if (isTimeSlotConflicting(currentTime, slotEnd, appointment.getStartTime(), appointment.getEndTime())) {
                    isAvailable = false;
                    break;
                }
            }

            TimeSlotDTO slot = new TimeSlotDTO(currentTime, slotEnd, isAvailable);
            slots.add(slot);
            currentTime = slotEnd;
        }

        return slots;
    }

    private List<TimeSlotDTO> convertAppointmentsToTimeSlots(List<Appointment> appointments) {
        return appointments.stream()
                .map(appointment -> {
                    TimeSlotDTO slot = new TimeSlotDTO(appointment.getStartTime(), appointment.getEndTime(), false);
                    slot.setAppointmentId(appointment.getId());
                    slot.setCustomerName(appointment.getCustomer().getFullName());
                    slot.setServiceName(appointment.getAppointmentServices().stream()
                            .map(appService -> appService.getService().getName())
                            .collect(Collectors.joining(", ")));
                    return slot;
                })
                .collect(Collectors.toList());
    }

    private boolean isTimeSlotConflicting(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && end1.isAfter(start2);
    }

    private AppointmentDTO convertToDTO(Appointment appointment) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(appointment.getId());
        dto.setCustomerId(appointment.getCustomer().getId());
        dto.setCustomerName(appointment.getCustomer().getFullName());
        dto.setCustomerPhone(appointment.getCustomer().getPhone());
        dto.setStaffId(appointment.getStaff().getId());
        dto.setStaffName(appointment.getStaff().getFullName());
        dto.setBranchId(appointment.getBranch().getId());
        dto.setBranchName(appointment.getBranch().getName());
        dto.setAppointmentDate(appointment.getAppointmentDate());
        dto.setStartTime(appointment.getStartTime());
        dto.setEndTime(appointment.getEndTime());
        dto.setStatus(appointment.getStatus().name());
        dto.setNotes(appointment.getNotes());
        dto.setTotalAmount(appointment.getTotalAmount());

        if (appointment.getCreatedAt() != null) {
            dto.setCreatedAt(appointment.getCreatedAt().format(formatter));
        }
        if (appointment.getUpdatedAt() != null) {
            dto.setUpdatedAt(appointment.getUpdatedAt().format(formatter));
        }

        // Convert appointment services
        List<AppointmentServiceDTO> serviceDTOs = appointment.getAppointmentServices().stream()
                .map(this::convertAppointmentServiceToDTO)
                .collect(Collectors.toList());
        dto.setServices(serviceDTOs);

        return dto;
    }

    private AppointmentServiceDTO convertAppointmentServiceToDTO(com.hexalyte.salon.model.AppointmentService appointmentService) {
        AppointmentServiceDTO dto = new AppointmentServiceDTO();
        dto.setId(appointmentService.getId());
        dto.setServiceId(appointmentService.getService().getId());
        dto.setServiceName(appointmentService.getService().getName());
        dto.setServiceCategory(appointmentService.getService().getCategory());
        dto.setPrice(appointmentService.getPrice());
        dto.setCommissionRate(appointmentService.getCommissionRate());
        dto.setDurationMinutes(appointmentService.getService().getDurationMinutes());
        return dto;
    }
}


