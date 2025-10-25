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
}
