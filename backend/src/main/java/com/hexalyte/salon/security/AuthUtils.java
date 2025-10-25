package com.hexalyte.salon.security;

import com.hexalyte.salon.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Utility class for authentication and authorization operations
 */
public class AuthUtils {

    /**
     * Get the currently authenticated user
     * @return User object or null if not authenticated
     */
    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // You might need to fetch the full User object from database
            // For now, return null as we need to implement this properly
            return null;
        }
        return null;
    }

    /**
     * Get the current username
     * @return username or null if not authenticated
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }

    /**
     * Get current user roles
     * @return List of role names
     */
    public static List<String> getCurrentUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities() != null) {
            return authentication.getAuthorities().stream()
                    .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                    .toList();
        }
        return List.of();
    }

    /**
     * Check if current user has a specific role
     * @param role Role to check (without ROLE_ prefix)
     * @return true if user has the role
     */
    public static boolean hasRole(String role) {
        List<String> roles = getCurrentUserRoles();
        return roles.contains(role);
    }

    /**
     * Check if current user has any of the specified roles
     * @param roles Roles to check
     * @return true if user has any of the roles
     */
    public static boolean hasAnyRole(String... roles) {
        List<String> userRoles = getCurrentUserRoles();
        for (String role : roles) {
            if (userRoles.contains(role)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if current user has all of the specified roles
     * @param roles Roles to check
     * @return true if user has all roles
     */
    public static boolean hasAllRoles(String... roles) {
        List<String> userRoles = getCurrentUserRoles();
        for (String role : roles) {
            if (!userRoles.contains(role)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if current user is authenticated
     * @return true if authenticated
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && 
               !"anonymousUser".equals(authentication.getPrincipal());
    }

    /**
     * Check if current user is admin
     * @return true if user is admin
     */
    public static boolean isAdmin() {
        return hasRole("ADMIN");
    }

    /**
     * Check if current user is branch manager or admin
     * @return true if user is branch manager or admin
     */
    public static boolean isBranchManagerOrAdmin() {
        return hasAnyRole("ADMIN", "BRANCH_MANAGER");
    }

    /**
     * Check if current user can manage staff
     * @return true if user can manage staff
     */
    public static boolean canManageStaff() {
        return hasAnyRole("ADMIN", "BRANCH_MANAGER");
    }

    /**
     * Check if current user can manage customers
     * @return true if user can manage customers
     */
    public static boolean canManageCustomers() {
        return hasAnyRole("ADMIN", "BRANCH_MANAGER", "RECEPTIONIST");
    }

    /**
     * Check if current user can manage appointments
     * @return true if user can manage appointments
     */
    public static boolean canManageAppointments() {
        return hasAnyRole("ADMIN", "BRANCH_MANAGER", "RECEPTIONIST", "BEAUTICIAN");
    }

    /**
     * Check if current user can view appointments
     * @return true if user can view appointments
     */
    public static boolean canViewAppointments() {
        return hasAnyRole("ADMIN", "BRANCH_MANAGER", "RECEPTIONIST", "BEAUTICIAN");
    }

    /**
     * Check if current user can manage services
     * @return true if user can manage services
     */
    public static boolean canManageServices() {
        return hasAnyRole("ADMIN", "BRANCH_MANAGER", "RECEPTIONIST");
    }

    /**
     * Check if current user can manage branches
     * @return true if user can manage branches
     */
    public static boolean canManageBranches() {
        return hasAnyRole("ADMIN", "BRANCH_MANAGER");
    }

    /**
     * Get current user's branch ID (if applicable)
     * @return Branch ID or null
     */
    public static Long getCurrentUserBranchId() {
        // This would need to be implemented based on your User model
        // For now, return null
        return null;
    }

    /**
     * Check if current user belongs to a specific branch
     * @param branchId Branch ID to check
     * @return true if user belongs to the branch
     */
    public static boolean belongsToBranch(Long branchId) {
        Long userBranchId = getCurrentUserBranchId();
        return userBranchId != null && userBranchId.equals(branchId);
    }
}
