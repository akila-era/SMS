package com.hexalyte.salon.security;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple in-memory rate limiting service
 * In production, consider using Redis or a more sophisticated solution
 */
@Service
public class RateLimitingService {

    private final ConcurrentHashMap<String, RateLimitInfo> rateLimitMap = new ConcurrentHashMap<>();
    private final long WINDOW_SIZE_MS = 15 * 60 * 1000; // 15 minutes
    private final int MAX_ATTEMPTS = 5; // Max attempts per window
    private final long LOCKOUT_DURATION_MS = 30 * 60 * 1000; // 30 minutes lockout

    /**
     * Check if IP is rate limited
     */
    public boolean isRateLimited(String ipAddress) {
        long currentTime = System.currentTimeMillis();
        RateLimitInfo info = rateLimitMap.computeIfAbsent(ipAddress, k -> new RateLimitInfo());

        // Reset if window has passed
        if (currentTime - info.windowStart > WINDOW_SIZE_MS) {
            info.attempts.set(0);
            info.windowStart = currentTime;
            info.lockedUntil = 0;
        }

        // Check if still locked out
        if (info.lockedUntil > currentTime) {
            return true;
        }

        // Check if exceeded max attempts
        if (info.attempts.get() >= MAX_ATTEMPTS) {
            info.lockedUntil = currentTime + LOCKOUT_DURATION_MS;
            return true;
        }

        return false;
    }

    /**
     * Record a failed attempt
     */
    public void recordFailedAttempt(String ipAddress) {
        long currentTime = System.currentTimeMillis();
        RateLimitInfo info = rateLimitMap.computeIfAbsent(ipAddress, k -> new RateLimitInfo());

        // Reset if window has passed
        if (currentTime - info.windowStart > WINDOW_SIZE_MS) {
            info.attempts.set(0);
            info.windowStart = currentTime;
            info.lockedUntil = 0;
        }

        info.attempts.incrementAndGet();
    }

    /**
     * Record a successful attempt (reset counter)
     */
    public void recordSuccessfulAttempt(String ipAddress) {
        RateLimitInfo info = rateLimitMap.get(ipAddress);
        if (info != null) {
            info.attempts.set(0);
            info.lockedUntil = 0;
        }
    }

    /**
     * Get remaining attempts for IP
     */
    public int getRemainingAttempts(String ipAddress) {
        RateLimitInfo info = rateLimitMap.get(ipAddress);
        if (info == null) {
            return MAX_ATTEMPTS;
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - info.windowStart > WINDOW_SIZE_MS) {
            return MAX_ATTEMPTS;
        }

        return Math.max(0, MAX_ATTEMPTS - info.attempts.get());
    }

    /**
     * Get lockout time remaining
     */
    public long getLockoutTimeRemaining(String ipAddress) {
        RateLimitInfo info = rateLimitMap.get(ipAddress);
        if (info == null) {
            return 0;
        }

        long currentTime = System.currentTimeMillis();
        return Math.max(0, info.lockedUntil - currentTime);
    }

    private static class RateLimitInfo {
        AtomicInteger attempts = new AtomicInteger(0);
        long windowStart = System.currentTimeMillis();
        long lockedUntil = 0;
    }
}
