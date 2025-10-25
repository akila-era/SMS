package com.hexalyte.salon.config;

import com.hexalyte.salon.model.Branch;
import com.hexalyte.salon.model.User;
import com.hexalyte.salon.repository.BranchRepository;
import com.hexalyte.salon.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if admin user exists
        if (!userRepository.findByUsername("admin").isPresent()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@salon.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(User.Role.ADMIN);
            admin.setIsActive(true);
            userRepository.save(admin);
            System.out.println("Created admin user: admin/admin123");
        }

        // Check if test user exists
        if (!userRepository.findByUsername("test").isPresent()) {
            User testUser = new User();
            testUser.setUsername("test");
            testUser.setEmail("test@salon.com");
            testUser.setPassword(passwordEncoder.encode("test123"));
            testUser.setRole(User.Role.BRANCH_MANAGER);
            testUser.setIsActive(true);
            
            // Assign to first branch if available
            if (branchRepository.count() > 0) {
                Branch firstBranch = branchRepository.findAll().get(0);
                testUser.setBranch(firstBranch);
            }
            
            userRepository.save(testUser);
            System.out.println("Created test user: test/test123");
        }

        // Create additional test users
        createTestUsers();
    }

    private void createTestUsers() {
        // Manager user
        if (!userRepository.findByUsername("manager").isPresent()) {
            User manager = new User();
            manager.setUsername("manager");
            manager.setEmail("manager@salon.com");
            manager.setPassword(passwordEncoder.encode("manager123"));
            manager.setRole(User.Role.BRANCH_MANAGER);
            manager.setIsActive(true);
            
            if (branchRepository.count() > 0) {
                Branch firstBranch = branchRepository.findAll().get(0);
                manager.setBranch(firstBranch);
            }
            
            userRepository.save(manager);
            System.out.println("Created manager user: manager/manager123");
        }

        // Receptionist user
        if (!userRepository.findByUsername("receptionist").isPresent()) {
            User receptionist = new User();
            receptionist.setUsername("receptionist");
            receptionist.setEmail("receptionist@salon.com");
            receptionist.setPassword(passwordEncoder.encode("receptionist123"));
            receptionist.setRole(User.Role.RECEPTIONIST);
            receptionist.setIsActive(true);
            
            if (branchRepository.count() > 0) {
                Branch firstBranch = branchRepository.findAll().get(0);
                receptionist.setBranch(firstBranch);
            }
            
            userRepository.save(receptionist);
            System.out.println("Created receptionist user: receptionist/receptionist123");
        }

        // Beautician user
        if (!userRepository.findByUsername("beautician").isPresent()) {
            User beautician = new User();
            beautician.setUsername("beautician");
            beautician.setEmail("beautician@salon.com");
            beautician.setPassword(passwordEncoder.encode("beautician123"));
            beautician.setRole(User.Role.BEAUTICIAN);
            beautician.setIsActive(true);
            
            if (branchRepository.count() > 0) {
                Branch firstBranch = branchRepository.findAll().get(0);
                beautician.setBranch(firstBranch);
            }
            
            userRepository.save(beautician);
            System.out.println("Created beautician user: beautician/beautician123");
        }
    }
}
