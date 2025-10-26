package com.hexalyte.salon.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor for logging API access and enforcing additional security measures
 */
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private AuditLogger auditLogger;

    @Autowired
    private RateLimitingService rateLimitingService;

    @Autowired
    private AuthUtils authUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIp = auditLogger.getClientIpAddress(request);
        String method = request.getMethod();
        String endpoint = request.getRequestURI();
        String username = authUtils.getCurrentUsername();

        // Check rate limiting for authentication endpoints
        if (endpoint.startsWith("/api/auth/")) {
            if (rateLimitingService.isRateLimited(clientIp)) {
                auditLogger.logSuspiciousActivity(username != null ? username : "anonymous", 
                    "Rate limited", clientIp);
                response.setStatus(429); // HTTP 429 Too Many Requests
                response.getWriter().write("Too many requests. Please try again later.");
                return false;
            }
        }

        // Log API access
        if (username != null) {
            auditLogger.logApiAccess(username, method, endpoint, clientIp, response.getStatus());
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                              Object handler, Exception ex) throws Exception {
        String clientIp = auditLogger.getClientIpAddress(request);
        String method = request.getMethod();
        String endpoint = request.getRequestURI();
        String username = authUtils.getCurrentUsername();

        // Log API access with final status code
        if (username != null) {
            auditLogger.logApiAccess(username, method, endpoint, clientIp, response.getStatus());
        }

        // Record failed attempts for authentication endpoints
        if (endpoint.startsWith("/api/auth/login") && response.getStatus() == HttpServletResponse.SC_UNAUTHORIZED) {
            rateLimitingService.recordFailedAttempt(clientIp);
        } else if (endpoint.startsWith("/api/auth/login") && response.getStatus() == HttpServletResponse.SC_OK) {
            rateLimitingService.recordSuccessfulAttempt(clientIp);
        }
    }
}
