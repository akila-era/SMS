package com.hexalyte.salon.security;

import com.hexalyte.salon.model.BranchUserAccess;
import com.hexalyte.salon.model.User;
import com.hexalyte.salon.repository.BranchUserAccessRepository;
import com.hexalyte.salon.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BranchAccessControlService {

    @Autowired
    private BranchUserAccessRepository branchUserAccessRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Check if the current user has access to a specific branch
     */
    public boolean hasBranchAccess(Long branchId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return false;
        }

        // Admin users have access to all branches
        if (currentUser.getRole().name().equals("ADMIN")) {
            return true;
        }

        // Check if user has specific access to this branch
        return branchUserAccessRepository.existsByUserIdAndBranchId(currentUser.getId(), branchId);
    }

    /**
     * Check if the current user has a specific role in a branch
     */
    public boolean hasBranchRole(Long branchId, BranchUserAccess.AccessRole role) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return false;
        }

        // Admin users have all roles
        if (currentUser.getRole().name().equals("ADMIN")) {
            return true;
        }

        return branchUserAccessRepository.existsByUserIdAndBranchIdAndRole(
            currentUser.getId(), branchId, role);
    }

    /**
     * Check if the current user can view appointments in a branch
     */
    public boolean canViewAppointments(Long branchId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return false;
        }

        // Admin users can view all appointments
        if (currentUser.getRole().name().equals("ADMIN")) {
            return true;
        }

        Optional<BranchUserAccess> access = branchUserAccessRepository
            .findByUserIdAndBranchId(currentUser.getId(), branchId);
        
        return access.map(BranchUserAccess::getCanViewAppointments).orElse(false);
    }

    /**
     * Check if the current user can view inventory in a branch
     */
    public boolean canViewInventory(Long branchId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return false;
        }

        // Admin users can view all inventory
        if (currentUser.getRole().name().equals("ADMIN")) {
            return true;
        }

        Optional<BranchUserAccess> access = branchUserAccessRepository
            .findByUserIdAndBranchId(currentUser.getId(), branchId);
        
        return access.map(BranchUserAccess::getCanViewInventory).orElse(false);
    }

    /**
     * Check if the current user can view financial reports in a branch
     */
    public boolean canViewFinancialReports(Long branchId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return false;
        }

        // Admin users can view all financial reports
        if (currentUser.getRole().name().equals("ADMIN")) {
            return true;
        }

        Optional<BranchUserAccess> access = branchUserAccessRepository
            .findByUserIdAndBranchId(currentUser.getId(), branchId);
        
        return access.map(BranchUserAccess::getCanViewFinancialReports).orElse(false);
    }

    /**
     * Check if the current user can view payroll in a branch
     */
    public boolean canViewPayroll(Long branchId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return false;
        }

        // Admin users can view all payroll
        if (currentUser.getRole().name().equals("ADMIN")) {
            return true;
        }

        Optional<BranchUserAccess> access = branchUserAccessRepository
            .findByUserIdAndBranchId(currentUser.getId(), branchId);
        
        return access.map(BranchUserAccess::getCanViewPayroll).orElse(false);
    }

    /**
     * Check if the current user can manage staff in a branch
     */
    public boolean canManageStaff(Long branchId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return false;
        }

        // Admin users can manage all staff
        if (currentUser.getRole().name().equals("ADMIN")) {
            return true;
        }

        Optional<BranchUserAccess> access = branchUserAccessRepository
            .findByUserIdAndBranchId(currentUser.getId(), branchId);
        
        return access.map(BranchUserAccess::getCanManageStaff).orElse(false);
    }

    /**
     * Get all branches the current user has access to
     */
    public List<Long> getAccessibleBranchIds() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return List.of();
        }

        // Admin users have access to all branches
        if (currentUser.getRole().name().equals("ADMIN")) {
            return List.of(); // Empty list means all branches
        }

        return branchUserAccessRepository.findByUserId(currentUser.getId())
            .stream()
            .map(access -> access.getBranch().getId())
            .toList();
    }

    /**
     * Get the current user from security context
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String username = authentication.getName();
        return userRepository.findByUsername(username).orElse(null);
    }

    /**
     * Check if user can perform branch management operations
     */
    public boolean canManageBranches() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return false;
        }

        return currentUser.getRole().name().equals("ADMIN");
    }

    /**
     * Check if user can assign users to branches
     */
    public boolean canAssignUsersToBranches() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return false;
        }

        return currentUser.getRole().name().equals("ADMIN");
    }
}
