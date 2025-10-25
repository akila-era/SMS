package com.hexalyte.salon.controller;

import com.hexalyte.salon.dto.JwtAuthenticationResponse;
import com.hexalyte.salon.dto.LoginRequest;
import com.hexalyte.salon.model.User;
import com.hexalyte.salon.repository.UserRepository;
import com.hexalyte.salon.security.AuditLogger;
import com.hexalyte.salon.security.AuthUtils;
import com.hexalyte.salon.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuditLogger auditLogger;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, 
                                            HttpServletRequest request) {
        try {
            String clientIp = auditLogger.getClientIpAddress(request);
            
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);

            User user = userRepository.findByUsername(loginRequest.getUsername()).orElse(null);
            Long branchId = user != null && user.getBranch() != null ? user.getBranch().getId() : null;

            // Log successful login
            auditLogger.logSuccessfulLogin(loginRequest.getUsername(), clientIp);

            return ResponseEntity.ok(new JwtAuthenticationResponse(
                    jwt,
                    user != null ? user.getRole().name() : null,
                    user != null ? user.getUsername() : null,
                    branchId
            ));
        } catch (BadCredentialsException e) {
            String clientIp = auditLogger.getClientIpAddress(request);
            auditLogger.logFailedLogin(loginRequest.getUsername(), clientIp, "Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        } catch (Exception e) {
            String clientIp = auditLogger.getClientIpAddress(request);
            auditLogger.logFailedLogin(loginRequest.getUsername(), clientIp, "Authentication error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Authentication failed");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request) {
        String username = AuthUtils.getCurrentUsername();
        String clientIp = auditLogger.getClientIpAddress(request);
        
        SecurityContextHolder.clearContext();
        
        if (username != null) {
            auditLogger.logLogout(username, clientIp);
        }
        
        return ResponseEntity.ok("User logged out successfully");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        if (!AuthUtils.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }
        
        String username = AuthUtils.getCurrentUsername();
        User user = userRepository.findByUsername(username).orElse(null);
        
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        
        return ResponseEntity.ok(user);
    }

    @GetMapping("/roles")
    public ResponseEntity<?> getCurrentUserRoles() {
        if (!AuthUtils.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }
        
        return ResponseEntity.ok(AuthUtils.getCurrentUserRoles());
    }
}


