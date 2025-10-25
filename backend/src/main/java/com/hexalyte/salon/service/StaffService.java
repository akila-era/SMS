package com.hexalyte.salon.service;

import com.hexalyte.salon.dto.StaffDTO;
import com.hexalyte.salon.model.Branch;
import com.hexalyte.salon.model.Staff;
import com.hexalyte.salon.model.User;
import com.hexalyte.salon.repository.BranchRepository;
import com.hexalyte.salon.repository.StaffRepository;
import com.hexalyte.salon.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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
    private PasswordEncoder passwordEncoder;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<StaffDTO> getAllStaff() {
        return staffRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<StaffDTO> getActiveStaff() {
        return staffRepository.findByIsActiveTrue().stream()
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
        staff.setFirstName(staffDTO.getFirstName());
        staff.setLastName(staffDTO.getLastName());
        staff.setPhone(staffDTO.getPhone());
        staff.setAddress(staffDTO.getAddress());
        staff.setPosition(staffDTO.getPosition());
        staff.setSalary(staffDTO.getSalary() != null ? staffDTO.getSalary() : BigDecimal.ZERO);
        staff.setHireDate(staffDTO.getHireDate());
        staff.setIsActive(staffDTO.getIsActive());

        Branch branch = branchRepository.findById(staffDTO.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + staffDTO.getBranchId()));
        staff.setBranch(branch);

        Staff savedStaff = staffRepository.save(staff);
        return convertToDTO(savedStaff);
    }

    public StaffDTO updateStaff(Long id, StaffDTO staffDTO) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found with id: " + id));

        staff.setFirstName(staffDTO.getFirstName());
        staff.setLastName(staffDTO.getLastName());
        staff.setPhone(staffDTO.getPhone());
        staff.setAddress(staffDTO.getAddress());
        staff.setPosition(staffDTO.getPosition());
        staff.setSalary(staffDTO.getSalary() != null ? staffDTO.getSalary() : BigDecimal.ZERO);
        staff.setHireDate(staffDTO.getHireDate());
        staff.setIsActive(staffDTO.getIsActive());

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
        
        staff.setIsActive(!staff.getIsActive());
        Staff savedStaff = staffRepository.save(staff);
        return convertToDTO(savedStaff);
    }

    private StaffDTO convertToDTO(Staff staff) {
        StaffDTO dto = new StaffDTO();
        dto.setId(staff.getId());
        dto.setUserId(staff.getUser().getId());
        dto.setUsername(staff.getUser().getUsername());
        dto.setEmail(staff.getUser().getEmail());
        dto.setFirstName(staff.getFirstName());
        dto.setLastName(staff.getLastName());
        dto.setPhone(staff.getPhone());
        dto.setAddress(staff.getAddress());
        dto.setPosition(staff.getPosition());
        dto.setSalary(staff.getSalary());
        dto.setHireDate(staff.getHireDate());
        dto.setBranchId(staff.getBranch().getId());
        dto.setBranchName(staff.getBranch().getName());
        dto.setIsActive(staff.getIsActive());
        
        if (staff.getCreatedAt() != null) {
            dto.setCreatedAt(staff.getCreatedAt().format(formatter));
        }
        
        if (staff.getUpdatedAt() != null) {
            dto.setUpdatedAt(staff.getUpdatedAt().format(formatter));
        }
        
        dto.setAppointmentCount(staff.getAppointments().size());
        // Calculate total commission (placeholder - would need commission service)
        dto.setTotalCommission(BigDecimal.ZERO);
        
        return dto;
    }
}


