package com.hexalyte.salon.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Service for logging authentication and authorization events
 */
@Component
public class AuditLogger {

    private static final Logger logger = LoggerFactory.getLogger(AuditLogger.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Log successful authentication
     */
    public void logSuccessfulLogin(String username, String ipAddress) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("AUTH_SUCCESS: User '{}' successfully logged in from IP '{}' at {}", 
                   username, ipAddress, timestamp);
    }

    /**
     * Log failed authentication attempt
     */
    public void logFailedLogin(String username, String ipAddress, String reason) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.warn("AUTH_FAILURE: User '{}' failed to login from IP '{}' at {} - Reason: {}", 
                   username, ipAddress, timestamp, reason);
    }

    /**
     * Log logout event
     */
    public void logLogout(String username, String ipAddress) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("AUTH_LOGOUT: User '{}' logged out from IP '{}' at {}", 
                   username, ipAddress, timestamp);
    }

    /**
     * Log access denied event
     */
    public void logAccessDenied(String username, String resource, String ipAddress) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.warn("ACCESS_DENIED: User '{}' denied access to '{}' from IP '{}' at {}", 
                   username, resource, ipAddress, timestamp);
    }

    /**
     * Log role-based access
     */
    public void logRoleAccess(String username, String role, String resource, String ipAddress) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("ROLE_ACCESS: User '{}' with role '{}' accessed '{}' from IP '{}' at {}", 
                   username, role, resource, ipAddress, timestamp);
    }

    /**
     * Log token validation failure
     */
    public void logTokenValidationFailure(String token, String ipAddress, String reason) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.warn("TOKEN_INVALID: Token validation failed from IP '{}' at {} - Reason: {}", 
                   ipAddress, timestamp, reason);
    }

    /**
     * Log suspicious activity
     */
    public void logSuspiciousActivity(String username, String activity, String ipAddress) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.error("SUSPICIOUS_ACTIVITY: User '{}' - Activity: '{}' from IP '{}' at {}", 
                    username, activity, ipAddress, timestamp);
    }

    /**
     * Log password change
     */
    public void logPasswordChange(String username, String ipAddress) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("PASSWORD_CHANGE: User '{}' changed password from IP '{}' at {}", 
                   username, ipAddress, timestamp);
    }

    /**
     * Log account lockout
     */
    public void logAccountLockout(String username, String ipAddress, String reason) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.error("ACCOUNT_LOCKOUT: User '{}' account locked from IP '{}' at {} - Reason: {}", 
                    username, ipAddress, timestamp, reason);
    }

    /**
     * Get client IP address from request
     */
    public String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }

    /**
     * Log API access
     */
    public void logApiAccess(String username, String method, String endpoint, String ipAddress, int statusCode) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("API_ACCESS: User '{}' {} {} from IP '{}' - Status: {} at {}", 
                   username, method, endpoint, ipAddress, statusCode, timestamp);
    }

    // Branch Management Audit Logging

    /**
     * Log branch creation
     */
    public void logBranchCreated(String username, String branchName, String branchCode, String ipAddress) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("BRANCH_CREATED: User '{}' created branch '{}' (Code: {}) from IP '{}' at {}", 
                   username, branchName, branchCode, ipAddress, timestamp);
    }

    /**
     * Log branch update
     */
    public void logBranchUpdated(String username, String branchName, String branchCode, String changes, String ipAddress) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("BRANCH_UPDATED: User '{}' updated branch '{}' (Code: {}) from IP '{}' at {} - Changes: {}", 
                   username, branchName, branchCode, ipAddress, timestamp, changes);
    }

    /**
     * Log branch deletion
     */
    public void logBranchDeleted(String username, String branchName, String branchCode, String ipAddress) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.warn("BRANCH_DELETED: User '{}' deleted branch '{}' (Code: {}) from IP '{}' at {}", 
                   username, branchName, branchCode, ipAddress, timestamp);
    }

    /**
     * Log branch status change
     */
    public void logBranchStatusChanged(String username, String branchName, String branchCode, String oldStatus, String newStatus, String ipAddress) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("BRANCH_STATUS_CHANGED: User '{}' changed branch '{}' (Code: {}) status from '{}' to '{}' from IP '{}' at {}", 
                   username, branchName, branchCode, oldStatus, newStatus, ipAddress, timestamp);
    }

    /**
     * Log manager assignment
     */
    public void logManagerAssigned(String username, String branchName, String branchCode, String managerName, String ipAddress) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("MANAGER_ASSIGNED: User '{}' assigned manager '{}' to branch '{}' (Code: {}) from IP '{}' at {}", 
                   username, managerName, branchName, branchCode, ipAddress, timestamp);
    }

    /**
     * Log user access assignment to branch
     */
    public void logUserAccessAssigned(String username, String targetUser, String branchName, String branchCode, String role, String ipAddress) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("USER_ACCESS_ASSIGNED: User '{}' assigned user '{}' with role '{}' to branch '{}' (Code: {}) from IP '{}' at {}", 
                   username, targetUser, role, branchName, branchCode, ipAddress, timestamp);
    }

    /**
     * Log user access removal from branch
     */
    public void logUserAccessRemoved(String username, String targetUser, String branchName, String branchCode, String ipAddress) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("USER_ACCESS_REMOVED: User '{}' removed user '{}' access from branch '{}' (Code: {}) from IP '{}' at {}", 
                   username, targetUser, branchName, branchCode, ipAddress, timestamp);
    }

    /**
     * Log branch access denied
     */
    public void logBranchAccessDenied(String username, String branchName, String branchCode, String action, String ipAddress) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.warn("BRANCH_ACCESS_DENIED: User '{}' denied access to '{}' for branch '{}' (Code: {}) from IP '{}' at {}", 
                   username, action, branchName, branchCode, ipAddress, timestamp);
    }

    /**
     * Log branch data export
     */
    public void logBranchDataExported(String username, String branchName, String branchCode, String exportType, String ipAddress) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("BRANCH_DATA_EXPORTED: User '{}' exported '{}' data for branch '{}' (Code: {}) from IP '{}' at {}", 
                   username, exportType, branchName, branchCode, ipAddress, timestamp);
    }

    /**
     * Log branch performance report access
     */
    public void logBranchReportAccessed(String username, String reportType, String branchName, String branchCode, String ipAddress) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("BRANCH_REPORT_ACCESSED: User '{}' accessed '{}' report for branch '{}' (Code: {}) from IP '{}' at {}", 
                   username, reportType, branchName, branchCode, ipAddress, timestamp);
    }

    // Commission Management Audit Logging

    /**
     * Log commission calculation
     */
    public void logCommissionCalculation(com.hexalyte.salon.model.Commission commission) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("COMMISSION_CALCULATED: Commission ID {} for Staff {} - Amount: {} - Type: {} at {}", 
                   commission.getId(), 
                   commission.getStaff() != null ? commission.getStaff().getFullName() : "Unknown",
                   commission.getAmount(), 
                   commission.getCommissionType(), 
                   timestamp);
    }
    
    /**
     * Log commission approval
     */
    public void logCommissionApproval(com.hexalyte.salon.model.Commission commission, com.hexalyte.salon.model.User approver) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("COMMISSION_APPROVED: Commission ID {} approved by {} for Staff {} - Amount: {} at {}", 
                   commission.getId(), 
                   approver.getUsername(),
                   commission.getStaff() != null ? commission.getStaff().getFullName() : "Unknown",
                   commission.getAmount(), 
                   timestamp);
    }
    
    /**
     * Log commission lock
     */
    public void logCommissionLock(com.hexalyte.salon.model.Commission commission) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("COMMISSION_LOCKED: Commission ID {} locked for Staff {} - Amount: {} at {}", 
                   commission.getId(), 
                   commission.getStaff() != null ? commission.getStaff().getFullName() : "Unknown",
                   commission.getAmount(), 
                   timestamp);
    }
    
    /**
     * Log commission reversal
     */
    public void logCommissionReversal(com.hexalyte.salon.model.Commission commission) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("COMMISSION_REVERSED: Commission ID {} reversed for Staff {} - Amount: {} at {}", 
                   commission.getId(), 
                   commission.getStaff() != null ? commission.getStaff().getFullName() : "Unknown",
                   commission.getAmount(), 
                   timestamp);
    }
    
    /**
     * Log commission adjustment
     */
    public void logCommissionAdjustment(com.hexalyte.salon.model.Commission commission, com.hexalyte.salon.model.CommissionAdjustmentLog adjustmentLog) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("COMMISSION_ADJUSTED: Commission ID {} adjusted by User {} - Old: {} New: {} Reason: {} at {}", 
                   commission.getId(), 
                   adjustmentLog.getChangedBy(),
                   adjustmentLog.getOldAmount(), 
                   adjustmentLog.getNewAmount(),
                   adjustmentLog.getReason(),
                   timestamp);
    }
    
    /**
     * Log commission summary generation
     */
    public void logCommissionSummaryGeneration(com.hexalyte.salon.model.CommissionSummary summary) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("COMMISSION_SUMMARY_GENERATED: Summary ID {} for Staff {} - Month: {} - Total: {} at {}", 
                   summary.getId(), 
                   summary.getStaff() != null ? summary.getStaff().getFullName() : "Unknown",
                   summary.getMonth(),
                   summary.getTotalCommission(), 
                   timestamp);
    }
    
    /**
     * Log commission summary approval
     */
    public void logCommissionSummaryApproval(com.hexalyte.salon.model.CommissionSummary summary, Long approverId) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("COMMISSION_SUMMARY_APPROVED: Summary ID {} approved by User {} for Staff {} - Month: {} - Total: {} at {}", 
                   summary.getId(), 
                   approverId,
                   summary.getStaff() != null ? summary.getStaff().getFullName() : "Unknown",
                   summary.getMonth(),
                   summary.getTotalCommission(), 
                   timestamp);
    }
    
    /**
     * Log commission summary lock
     */
    public void logCommissionSummaryLock(com.hexalyte.salon.model.CommissionSummary summary) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("COMMISSION_SUMMARY_LOCKED: Summary ID {} locked for Staff {} - Month: {} - Total: {} at {}", 
                   summary.getId(), 
                   summary.getStaff() != null ? summary.getStaff().getFullName() : "Unknown",
                   summary.getMonth(),
                   summary.getTotalCommission(), 
                   timestamp);
    }
}
