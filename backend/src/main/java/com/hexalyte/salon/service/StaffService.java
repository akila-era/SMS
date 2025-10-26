package com.hexalyte.salon.service;

import com.hexalyte.salon.dto.StaffDTO;
import com.hexalyte.salon.dto.StaffCommissionSummaryDTO;
import com.hexalyte.salon.model.Branch;
import com.hexalyte.salon.model.Staff;
import com.hexalyte.salon.model.User;
import com.hexalyte.salon.repository.BranchRepository;
import com.hexalyte.salon.repository.StaffRepository;
import com.hexalyte.salon.repository.UserRepository;
import com.hexalyte.salon.repository.StaffCommissionSummaryRepository;
import com.hexalyte.salon.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
@Transactional
public class StaffService {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StaffCommissionSummaryRepository commissionSummaryRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<StaffDTO> getAllStaff() {
        return staffRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<StaffDTO> getActiveStaff() {
        return staffRepository.findByStatus(Staff.StaffStatus.ACTIVE).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<StaffDTO> getStaffByBranch(Long branchId) {
        return staffRepository.findByBranchId(branchId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public StaffDTO getStaffById(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found with id: " + id));
        return convertToDTO(staff);
    }

    public StaffDTO createStaff(StaffDTO staffDTO) {
        // Check if employee code already exists
        if (staffRepository.existsByEmployeeCode(staffDTO.getEmployeeCode())) {
            throw new RuntimeException("Employee code already exists: " + staffDTO.getEmployeeCode());
        }

        // Create user account first
        User user = new User();
        user.setUsername(staffDTO.getUsername());
        user.setEmail(staffDTO.getEmail());
        user.setPassword(passwordEncoder.encode("password123")); // Default password
        user.setRole(User.Role.BEAUTICIAN);
        user.setIsActive(true);
        
        User savedUser = userRepository.save(user);

        // Create staff record
        Staff staff = new Staff();
        staff.setUser(savedUser);
        staff.setEmployeeCode(staffDTO.getEmployeeCode());
        staff.setFirstName(staffDTO.getFirstName());
        staff.setLastName(staffDTO.getLastName());
        staff.setPhone(staffDTO.getPhone());
        staff.setEmail(staffDTO.getEmail());
        staff.setGender(staffDTO.getGender() != null ? Staff.Gender.valueOf(staffDTO.getGender()) : null);
        staff.setAddress(staffDTO.getAddress());
        staff.setDesignation(staffDTO.getDesignation());
        staff.setSkillSet(staffDTO.getSkillSet());
        staff.setJoinDate(staffDTO.getJoinDate());
        staff.setSalaryType(staffDTO.getSalaryType() != null ? Staff.SalaryType.valueOf(staffDTO.getSalaryType()) : Staff.SalaryType.FIXED);
        staff.setBaseSalary(staffDTO.getBaseSalary() != null ? staffDTO.getBaseSalary() : BigDecimal.ZERO);
        staff.setCommissionRate(staffDTO.getCommissionRate() != null ? staffDTO.getCommissionRate() : BigDecimal.ZERO);
        staff.setBankDetails(staffDTO.getBankDetails());
        staff.setStatus(staffDTO.getStatus() != null ? Staff.StaffStatus.valueOf(staffDTO.getStatus()) : Staff.StaffStatus.ACTIVE);

        Branch branch = branchRepository.findById(staffDTO.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + staffDTO.getBranchId()));
        staff.setBranch(branch);

        Staff savedStaff = staffRepository.save(staff);
        return convertToDTO(savedStaff);
    }

    public StaffDTO updateStaff(Long id, StaffDTO staffDTO) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found with id: " + id));

        // Check if employee code is being changed and if it already exists
        if (!staff.getEmployeeCode().equals(staffDTO.getEmployeeCode()) && 
            staffRepository.existsByEmployeeCode(staffDTO.getEmployeeCode())) {
            throw new RuntimeException("Employee code already exists: " + staffDTO.getEmployeeCode());
        }

        staff.setEmployeeCode(staffDTO.getEmployeeCode());
        staff.setFirstName(staffDTO.getFirstName());
        staff.setLastName(staffDTO.getLastName());
        staff.setPhone(staffDTO.getPhone());
        staff.setEmail(staffDTO.getEmail());
        staff.setGender(staffDTO.getGender() != null ? Staff.Gender.valueOf(staffDTO.getGender()) : null);
        staff.setAddress(staffDTO.getAddress());
        staff.setDesignation(staffDTO.getDesignation());
        staff.setSkillSet(staffDTO.getSkillSet());
        staff.setJoinDate(staffDTO.getJoinDate());
        staff.setSalaryType(staffDTO.getSalaryType() != null ? Staff.SalaryType.valueOf(staffDTO.getSalaryType()) : Staff.SalaryType.FIXED);
        staff.setBaseSalary(staffDTO.getBaseSalary() != null ? staffDTO.getBaseSalary() : BigDecimal.ZERO);
        staff.setCommissionRate(staffDTO.getCommissionRate() != null ? staffDTO.getCommissionRate() : BigDecimal.ZERO);
        staff.setBankDetails(staffDTO.getBankDetails());
        staff.setStatus(staffDTO.getStatus() != null ? Staff.StaffStatus.valueOf(staffDTO.getStatus()) : Staff.StaffStatus.ACTIVE);

        if (!staff.getBranch().getId().equals(staffDTO.getBranchId())) {
            Branch branch = branchRepository.findById(staffDTO.getBranchId())
                    .orElseThrow(() -> new RuntimeException("Branch not found with id: " + staffDTO.getBranchId()));
            staff.setBranch(branch);
        }

        Staff savedStaff = staffRepository.save(staff);
        return convertToDTO(savedStaff);
    }

    public void deleteStaff(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found with id: " + id));
        
        // Check if staff has appointments
        if (!staff.getAppointments().isEmpty()) {
            throw new RuntimeException("Cannot delete staff with existing appointments. Please reassign appointments first.");
        }

        staffRepository.delete(staff);
        // Also delete the associated user account
        userRepository.delete(staff.getUser());
    }

    public StaffDTO toggleStaffStatus(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found with id: " + id));
        
        if (staff.getStatus() == Staff.StaffStatus.ACTIVE) {
            staff.setStatus(Staff.StaffStatus.INACTIVE);
        } else {
            staff.setStatus(Staff.StaffStatus.ACTIVE);
        }
        
        Staff savedStaff = staffRepository.save(staff);
        return convertToDTO(savedStaff);
    }

    public StaffDTO updateStaffStatus(Long id, String status) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found with id: " + id));
        
        staff.setStatus(Staff.StaffStatus.valueOf(status));
        Staff savedStaff = staffRepository.save(staff);
        return convertToDTO(savedStaff);
    }

    public List<StaffDTO> getStaffByStatus(String status) {
        Staff.StaffStatus staffStatus = Staff.StaffStatus.valueOf(status);
        return staffRepository.findByStatus(staffStatus).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<StaffDTO> getStaffByDesignation(String designation) {
        return staffRepository.findByDesignation(designation).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<StaffDTO> getStaffBySkill(String skill) {
        return staffRepository.findBySkillSetContainingIgnoreCase(skill).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public StaffCommissionSummaryDTO generateCommissionSummary(Long staffId, String month) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found with id: " + staffId));

        // Check if summary already exists
        Optional<com.hexalyte.salon.model.StaffCommissionSummary> existingSummary = 
            commissionSummaryRepository.findByStaffIdAndMonth(staffId, month);
        
        if (existingSummary.isPresent()) {
            return convertCommissionSummaryToDTO(existingSummary.get());
        }

        // Create new commission summary
        com.hexalyte.salon.model.StaffCommissionSummary summary = new com.hexalyte.salon.model.StaffCommissionSummary();
        summary.setStaff(staff);
        summary.setBranch(staff.getBranch());
        summary.setMonth(month);
        summary.setBaseSalary(staff.getBaseSalary());
        
        // Calculate performance metrics (placeholder - would integrate with appointment service)
        summary.setTotalServices(0);
        summary.setTotalCommission(BigDecimal.ZERO);
        summary.setTotalRevenue(BigDecimal.ZERO);
        summary.setAverageRating(BigDecimal.ZERO);
        summary.calculateTotalPayout();
        summary.setIsGenerated(true);

        com.hexalyte.salon.model.StaffCommissionSummary savedSummary = commissionSummaryRepository.save(summary);
        return convertCommissionSummaryToDTO(savedSummary);
    }

    public List<StaffCommissionSummaryDTO> getCommissionSummariesByMonth(String month) {
        return commissionSummaryRepository.findByMonth(month).stream()
                .map(this::convertCommissionSummaryToDTO)
                .collect(Collectors.toList());
    }

    public List<StaffCommissionSummaryDTO> getCommissionSummariesByStaff(Long staffId) {
        return commissionSummaryRepository.findByStaffId(staffId).stream()
                .map(this::convertCommissionSummaryToDTO)
                .collect(Collectors.toList());
    }

    private StaffDTO convertToDTO(Staff staff) {
        StaffDTO dto = new StaffDTO();
        dto.setId(staff.getId());
        dto.setUserId(staff.getUser().getId());
        dto.setUsername(staff.getUser().getUsername());
        dto.setEmployeeCode(staff.getEmployeeCode());
        dto.setFirstName(staff.getFirstName());
        dto.setLastName(staff.getLastName());
        dto.setPhone(staff.getPhone());
        dto.setEmail(staff.getEmail());
        dto.setGender(staff.getGender() != null ? staff.getGender().name() : null);
        dto.setAddress(staff.getAddress());
        dto.setDesignation(staff.getDesignation());
        dto.setSkillSet(staff.getSkillSet());
        dto.setJoinDate(staff.getJoinDate());
        dto.setSalaryType(staff.getSalaryType() != null ? staff.getSalaryType().name() : null);
        dto.setBaseSalary(staff.getBaseSalary());
        dto.setCommissionRate(staff.getCommissionRate());
        dto.setBankDetails(staff.getBankDetails());
        dto.setStatus(staff.getStatus() != null ? staff.getStatus().name() : null);
        dto.setBranchId(staff.getBranch().getId());
        dto.setBranchName(staff.getBranch().getName());
        
        if (staff.getCreatedAt() != null) {
            dto.setCreatedAt(staff.getCreatedAt().format(formatter));
        }
        
        if (staff.getUpdatedAt() != null) {
            dto.setUpdatedAt(staff.getUpdatedAt().format(formatter));
        }
        
        // Calculate performance metrics
        dto.setAppointmentCount(staff.getAppointments().size());
        
        // Calculate attendance metrics for current month
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        
        Long presentDays = attendanceRepository.countPresentDaysByStaffAndDateRange(staff.getId(), startOfMonth, endOfMonth);
        Long absentDays = attendanceRepository.countAbsentDaysByStaffAndDateRange(staff.getId(), startOfMonth, endOfMonth);
        Double totalHours = attendanceRepository.sumTotalHoursByStaffAndDateRange(staff.getId(), startOfMonth, endOfMonth);
        Double overtimeHours = attendanceRepository.sumOvertimeHoursByStaffAndDateRange(staff.getId(), startOfMonth, endOfMonth);
        
        dto.setPresentDays(presentDays != null ? presentDays.intValue() : 0);
        dto.setAbsentDays(absentDays != null ? absentDays.intValue() : 0);
        dto.setTotalHours(totalHours != null ? BigDecimal.valueOf(totalHours) : BigDecimal.ZERO);
        dto.setOvertimeHours(overtimeHours != null ? BigDecimal.valueOf(overtimeHours) : BigDecimal.ZERO);
        
        // Calculate total commission (placeholder - would need commission service)
        dto.setTotalCommission(BigDecimal.ZERO);
        dto.setTotalRevenue(BigDecimal.ZERO);
        dto.setAverageRating(BigDecimal.ZERO);
        dto.setTotalServices(0);
        
        return dto;
    }

    private StaffCommissionSummaryDTO convertCommissionSummaryToDTO(com.hexalyte.salon.model.StaffCommissionSummary summary) {
        StaffCommissionSummaryDTO dto = new StaffCommissionSummaryDTO();
        dto.setId(summary.getId());
        dto.setStaffId(summary.getStaff().getId());
        dto.setStaffName(summary.getStaffName());
        dto.setBranchId(summary.getBranch().getId());
        dto.setBranchName(summary.getBranchName());
        dto.setMonth(summary.getMonth());
        dto.setMonthName(summary.getMonthName());
        dto.setYear(summary.getYear());
        dto.setTotalServices(summary.getTotalServices());
        dto.setTotalCommission(summary.getTotalCommission());
        dto.setBaseSalary(summary.getBaseSalary());
        dto.setTotalPayout(summary.getTotalPayout());
        dto.setTotalRevenue(summary.getTotalRevenue());
        dto.setAverageRating(summary.getAverageRating());
        dto.setIsGenerated(summary.getIsGenerated());
        
        if (summary.getCreatedAt() != null) {
            dto.setCreatedAt(summary.getCreatedAt().format(formatter));
        }
        
        return dto;
    }
}


