package com.hexalyte.salon.service;

import com.hexalyte.salon.dto.BranchDTO;
import com.hexalyte.salon.model.Branch;
import com.hexalyte.salon.model.User;
import com.hexalyte.salon.repository.BranchRepository;
import com.hexalyte.salon.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BranchService {

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private UserRepository userRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<BranchDTO> getAllBranches() {
        return branchRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BranchDTO> getActiveBranches() {
        return branchRepository.findByIsActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BranchDTO getBranchById(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + id));
        return convertToDTO(branch);
    }

    public BranchDTO createBranch(BranchDTO branchDTO) {
        Branch branch = new Branch();
        branch.setName(branchDTO.getName());
        branch.setAddress(branchDTO.getAddress());
        branch.setPhone(branchDTO.getPhone());
        branch.setEmail(branchDTO.getEmail());
        branch.setIsActive(branchDTO.getIsActive());

        if (branchDTO.getManagerId() != null) {
            User manager = userRepository.findById(branchDTO.getManagerId())
                    .orElseThrow(() -> new RuntimeException("Manager not found with id: " + branchDTO.getManagerId()));
            branch.setManager(manager);
        }

        Branch savedBranch = branchRepository.save(branch);
        return convertToDTO(savedBranch);
    }

    public BranchDTO updateBranch(Long id, BranchDTO branchDTO) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + id));

        branch.setName(branchDTO.getName());
        branch.setAddress(branchDTO.getAddress());
        branch.setPhone(branchDTO.getPhone());
        branch.setEmail(branchDTO.getEmail());
        branch.setIsActive(branchDTO.getIsActive());

        if (branchDTO.getManagerId() != null) {
            User manager = userRepository.findById(branchDTO.getManagerId())
                    .orElseThrow(() -> new RuntimeException("Manager not found with id: " + branchDTO.getManagerId()));
            branch.setManager(manager);
        } else {
            branch.setManager(null);
        }

        Branch savedBranch = branchRepository.save(branch);
        return convertToDTO(savedBranch);
    }

    public void deleteBranch(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + id));
        
        // Check if branch has staff or appointments
        if (!branch.getStaff().isEmpty()) {
            throw new RuntimeException("Cannot delete branch with existing staff. Please reassign staff first.");
        }
        
        if (!branch.getAppointments().isEmpty()) {
            throw new RuntimeException("Cannot delete branch with existing appointments. Please handle appointments first.");
        }

        branchRepository.delete(branch);
    }

    public BranchDTO toggleBranchStatus(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + id));
        
        branch.setIsActive(!branch.getIsActive());
        Branch savedBranch = branchRepository.save(branch);
        return convertToDTO(savedBranch);
    }

    private BranchDTO convertToDTO(Branch branch) {
        BranchDTO dto = new BranchDTO();
        dto.setId(branch.getId());
        dto.setName(branch.getName());
        dto.setAddress(branch.getAddress());
        dto.setPhone(branch.getPhone());
        dto.setEmail(branch.getEmail());
        dto.setIsActive(branch.getIsActive());
        
        if (branch.getManager() != null) {
            dto.setManagerId(branch.getManager().getId());
            dto.setManagerName(branch.getManager().getUsername());
        }
        
        if (branch.getCreatedAt() != null) {
            dto.setCreatedAt(branch.getCreatedAt().format(formatter));
        }
        
        if (branch.getUpdatedAt() != null) {
            dto.setUpdatedAt(branch.getUpdatedAt().format(formatter));
        }
        
        dto.setStaffCount(branch.getStaff().size());
        dto.setAppointmentCount(branch.getAppointments().size());
        
        return dto;
    }
}


