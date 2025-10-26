package com.hexalyte.salon.service;

import com.hexalyte.salon.dto.*;
import com.hexalyte.salon.model.*;
import com.hexalyte.salon.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class BranchService {

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private BranchUserAccessRepository branchUserAccessRepository;

    @Autowired
    private BranchSummaryRepository branchSummaryRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Basic CRUD Operations
    public List<BranchDTO> getAllBranches() {
        return branchRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BranchDTO> getActiveBranches() {
        return branchRepository.findByStatusOrderByBranchNameAsc(Branch.BranchStatus.ACTIVE).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BranchDTO> getBranchesByStatus(Branch.BranchStatus status) {
        return branchRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BranchDTO getBranchById(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + id));
        return convertToDTO(branch);
    }

    public BranchDTO getBranchByCode(String branchCode) {
        Branch branch = branchRepository.findByBranchCode(branchCode)
                .orElseThrow(() -> new RuntimeException("Branch not found with code: " + branchCode));
        return convertToDTO(branch);
    }

    public BranchDTO createBranch(BranchDTO branchDTO, Long currentUserId) {
        // Check if branch code already exists
        if (branchRepository.existsByBranchCode(branchDTO.getBranchCode())) {
            throw new RuntimeException("Branch code already exists: " + branchDTO.getBranchCode());
        }

        Branch branch = new Branch();
        branch.setBranchCode(branchDTO.getBranchCode());
        branch.setBranchName(branchDTO.getBranchName());
        branch.setAddress(branchDTO.getAddress());
        branch.setCity(branchDTO.getCity());
        branch.setPostalCode(branchDTO.getPostalCode());
        branch.setContactNumber(branchDTO.getContactNumber());
        branch.setEmail(branchDTO.getEmail());
        branch.setWorkingHours(branchDTO.getWorkingHours());
        branch.setStatus(branchDTO.getStatus());
        branch.setLogoUrl(branchDTO.getLogoUrl());

        // Set manager if provided
        if (branchDTO.getManagerId() != null) {
            Staff manager = staffRepository.findById(branchDTO.getManagerId())
                    .orElseThrow(() -> new RuntimeException("Manager not found with id: " + branchDTO.getManagerId()));
            branch.setManager(manager);
        }

        // Set audit fields
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Current user not found"));
        branch.setCreatedBy(currentUser);

        Branch savedBranch = branchRepository.save(branch);
        return convertToDTO(savedBranch);
    }

    public BranchDTO updateBranch(Long id, BranchDTO branchDTO, Long currentUserId) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + id));

        // Check if branch code is being changed and if it already exists
        if (!branch.getBranchCode().equals(branchDTO.getBranchCode()) && 
            branchRepository.existsByBranchCode(branchDTO.getBranchCode())) {
            throw new RuntimeException("Branch code already exists: " + branchDTO.getBranchCode());
        }

        branch.setBranchCode(branchDTO.getBranchCode());
        branch.setBranchName(branchDTO.getBranchName());
        branch.setAddress(branchDTO.getAddress());
        branch.setCity(branchDTO.getCity());
        branch.setPostalCode(branchDTO.getPostalCode());
        branch.setContactNumber(branchDTO.getContactNumber());
        branch.setEmail(branchDTO.getEmail());
        branch.setWorkingHours(branchDTO.getWorkingHours());
        branch.setStatus(branchDTO.getStatus());
        branch.setLogoUrl(branchDTO.getLogoUrl());

        // Update manager if provided
        if (branchDTO.getManagerId() != null) {
            Staff manager = staffRepository.findById(branchDTO.getManagerId())
                    .orElseThrow(() -> new RuntimeException("Manager not found with id: " + branchDTO.getManagerId()));
            branch.setManager(manager);
        } else {
            branch.setManager(null);
        }

        // Set audit fields
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Current user not found"));
        branch.setUpdatedBy(currentUser);

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

        // Delete user access records
        branchUserAccessRepository.deleteByBranchId(id);
        
        branchRepository.delete(branch);
    }

    public BranchDTO toggleBranchStatus(Long id, Long currentUserId) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + id));
        
        Branch.BranchStatus newStatus = branch.getStatus() == Branch.BranchStatus.ACTIVE 
            ? Branch.BranchStatus.INACTIVE 
            : Branch.BranchStatus.ACTIVE;
        
        branch.setStatus(newStatus);
        
        // Set audit fields
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Current user not found"));
        branch.setUpdatedBy(currentUser);
        
        Branch savedBranch = branchRepository.save(branch);
        return convertToDTO(savedBranch);
    }

    // Search and Filter Operations
    public List<BranchDTO> searchBranches(String searchTerm) {
        return branchRepository.searchBranches(searchTerm).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BranchDTO> getBranchesByCity(String city) {
        return branchRepository.findByCityOrderByBranchNameAsc(city).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BranchDTO> getBranchesByManager(Long managerId) {
        return branchRepository.findByManagerId(managerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BranchDTO> getBranchesWithoutManager() {
        return branchRepository.findByManagerIsNull().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Branch User Access Management
    public BranchUserAccessDTO assignUserToBranch(BranchUserAccessDTO accessDTO) {
        // Check if user already has access to this branch
        if (branchUserAccessRepository.existsByUserIdAndBranchId(accessDTO.getUserId(), accessDTO.getBranchId())) {
            throw new RuntimeException("User already has access to this branch");
        }

        User user = userRepository.findById(accessDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + accessDTO.getUserId()));
        
        Branch branch = branchRepository.findById(accessDTO.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + accessDTO.getBranchId()));

        BranchUserAccess access = new BranchUserAccess();
        access.setUser(user);
        access.setBranch(branch);
        access.setRole(accessDTO.getRole());
        access.setAccessLevel(accessDTO.getAccessLevel());
        access.setCanViewAppointments(accessDTO.getCanViewAppointments());
        access.setCanViewInventory(accessDTO.getCanViewInventory());
        access.setCanViewFinancialReports(accessDTO.getCanViewFinancialReports());
        access.setCanViewPayroll(accessDTO.getCanViewPayroll());
        access.setCanManageStaff(accessDTO.getCanManageStaff());

        BranchUserAccess savedAccess = branchUserAccessRepository.save(access);
        return convertToDTO(savedAccess);
    }

    public List<BranchUserAccessDTO> getBranchUserAccess(Long branchId) {
        return branchUserAccessRepository.findByBranchId(branchId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BranchUserAccessDTO> getUserBranchAccess(Long userId) {
        return branchUserAccessRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void removeUserFromBranch(Long userId, Long branchId) {
        branchUserAccessRepository.deleteByUserIdAndBranchId(userId, branchId);
    }

    // Dashboard and Analytics
    public BranchDashboardDTO getBranchDashboard() {
        BranchDashboardDTO dashboard = new BranchDashboardDTO();
        
        // Basic statistics
        dashboard.setTotalBranches(branchRepository.count());
        dashboard.setActiveBranches(branchRepository.countByStatus(Branch.BranchStatus.ACTIVE));
        dashboard.setInactiveBranches(branchRepository.countByStatus(Branch.BranchStatus.INACTIVE));
        
        // Get current month data
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now();
        
        dashboard.setMonthlyRevenue(branchSummaryRepository.getTotalRevenueInDateRange(startOfMonth, endOfMonth));
        dashboard.setTotalAppointments(branchSummaryRepository.getTotalAppointmentsInDateRange(startOfMonth, endOfMonth).intValue());
        
        // Get top performing branches
        List<BranchPerformanceDTO> topBranches = getTopPerformingBranches(startOfMonth, endOfMonth, 3);
        dashboard.setTopPerformingBranches(topBranches);
        
        // Get branch comparison data
        List<BranchPerformanceDTO> comparison = getBranchPerformanceComparison(startOfMonth, endOfMonth);
        dashboard.setBranchComparison(comparison);
        
        // Generate chart data
        dashboard.setRevenueByBranch(generateRevenueChartData(comparison));
        dashboard.setAppointmentsByBranch(generateAppointmentsChartData(comparison));
        
        return dashboard;
    }

    public List<BranchPerformanceDTO> getTopPerformingBranches(LocalDate startDate, LocalDate endDate, int limit) {
        List<BranchSummary> summaries = branchSummaryRepository.findTopPerformingBranches(startDate, endDate);
        
        return summaries.stream()
                .limit(limit)
                .map(this::convertToPerformanceDTO)
                .collect(Collectors.toList());
    }

    public List<BranchPerformanceDTO> getBranchPerformanceComparison(LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = branchSummaryRepository.getBranchPerformanceComparison(startDate, endDate);
        
        return results.stream()
                .map(this::convertToPerformanceDTO)
                .collect(Collectors.toList());
    }

    public List<BranchPerformanceDTO> getInactiveBranches(LocalDate startDate, LocalDate endDate) {
        List<Object> inactiveBranches = branchSummaryRepository.findInactiveBranches(startDate, endDate);
        
        return inactiveBranches.stream()
                .map(branch -> {
                    BranchPerformanceDTO dto = new BranchPerformanceDTO();
                    dto.setBranchId(((Branch) branch).getId());
                    dto.setBranchName(((Branch) branch).getBranchName());
                    dto.setBranchCode(((Branch) branch).getBranchCode());
                    dto.setCity(((Branch) branch).getCity());
                    dto.setTotalAppointments(0);
                    dto.setTotalRevenue(BigDecimal.ZERO);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // Helper Methods
    private BranchDTO convertToDTO(Branch branch) {
        BranchDTO dto = new BranchDTO();
        dto.setId(branch.getId());
        dto.setBranchCode(branch.getBranchCode());
        dto.setBranchName(branch.getBranchName());
        dto.setAddress(branch.getAddress());
        dto.setCity(branch.getCity());
        dto.setPostalCode(branch.getPostalCode());
        dto.setContactNumber(branch.getContactNumber());
        dto.setEmail(branch.getEmail());
        dto.setWorkingHours(branch.getWorkingHours());
        dto.setStatus(branch.getStatus());
        dto.setLogoUrl(branch.getLogoUrl());
        
        if (branch.getManager() != null) {
            dto.setManagerId(branch.getManager().getId());
            dto.setManagerName(branch.getManager().getFirstName() + " " + branch.getManager().getLastName());
        }
        
        if (branch.getCreatedAt() != null) {
            dto.setCreatedAt(branch.getCreatedAt().format(formatter));
        }
        
        if (branch.getUpdatedAt() != null) {
            dto.setUpdatedAt(branch.getUpdatedAt().format(formatter));
        }
        
        if (branch.getCreatedBy() != null) {
            dto.setCreatedById(branch.getCreatedBy().getId());
            dto.setCreatedByName(branch.getCreatedBy().getUsername());
        }
        
        if (branch.getUpdatedBy() != null) {
            dto.setUpdatedById(branch.getUpdatedBy().getId());
            dto.setUpdatedByName(branch.getUpdatedBy().getUsername());
        }
        
        // Calculate statistics
        dto.setStaffCount(branch.getStaff().size());
        dto.setAppointmentCount(branch.getAppointments().size());
        
        // Get customer count for this branch
        long customerCount = customerRepository.countByPreferredBranchId(branch.getId());
        dto.setCustomerCount((int) customerCount);
        
        return dto;
    }

    private BranchUserAccessDTO convertToDTO(BranchUserAccess access) {
        BranchUserAccessDTO dto = new BranchUserAccessDTO();
        dto.setId(access.getId());
        dto.setUserId(access.getUser().getId());
        dto.setUserName(access.getUser().getUsername());
        dto.setUserEmail(access.getUser().getEmail());
        dto.setBranchId(access.getBranch().getId());
        dto.setBranchName(access.getBranch().getBranchName());
        dto.setBranchCode(access.getBranch().getBranchCode());
        dto.setRole(access.getRole());
        dto.setAccessLevel(access.getAccessLevel());
        dto.setCanViewAppointments(access.getCanViewAppointments());
        dto.setCanViewInventory(access.getCanViewInventory());
        dto.setCanViewFinancialReports(access.getCanViewFinancialReports());
        dto.setCanViewPayroll(access.getCanViewPayroll());
        dto.setCanManageStaff(access.getCanManageStaff());
        
        if (access.getCreatedAt() != null) {
            dto.setCreatedAt(access.getCreatedAt().format(formatter));
        }
        
        if (access.getUpdatedAt() != null) {
            dto.setUpdatedAt(access.getUpdatedAt().format(formatter));
        }
        
        return dto;
    }

    private BranchPerformanceDTO convertToPerformanceDTO(BranchSummary summary) {
        BranchPerformanceDTO dto = new BranchPerformanceDTO();
        dto.setBranchId(summary.getBranch().getId());
        dto.setBranchName(summary.getBranch().getBranchName());
        dto.setBranchCode(summary.getBranch().getBranchCode());
        dto.setCity(summary.getBranch().getCity());
        dto.setTotalAppointments(summary.getTotalAppointments());
        dto.setTotalRevenue(summary.getTotalRevenue());
        dto.setTotalCustomers(summary.getTotalCustomers());
        dto.setTotalStaff(summary.getTotalStaff());
        dto.setAverageRating(summary.getAverageRating());
        dto.setReportDate(summary.getSummaryDate());
        
        // Calculate average bill
        if (summary.getTotalAppointments() > 0) {
            dto.setAverageBill(summary.getTotalRevenue().divide(BigDecimal.valueOf(summary.getTotalAppointments()), 2, BigDecimal.ROUND_HALF_UP));
        } else {
            dto.setAverageBill(BigDecimal.ZERO);
        }
        
        return dto;
    }

    private BranchPerformanceDTO convertToPerformanceDTO(Object[] result) {
        BranchPerformanceDTO dto = new BranchPerformanceDTO();
        dto.setBranchId((Long) result[0]);
        dto.setBranchName((String) result[1]);
        dto.setTotalRevenue((BigDecimal) result[2]);
        dto.setTotalAppointments(((Long) result[3]).intValue());
        dto.setAverageRating((BigDecimal) result[4]);
        
        // Calculate average bill
        if (dto.getTotalAppointments() > 0) {
            dto.setAverageBill(dto.getTotalRevenue().divide(BigDecimal.valueOf(dto.getTotalAppointments()), 2, BigDecimal.ROUND_HALF_UP));
        } else {
            dto.setAverageBill(BigDecimal.ZERO);
        }
        
        return dto;
    }

    private List<ChartDataDTO> generateRevenueChartData(List<BranchPerformanceDTO> branches) {
        return branches.stream()
                .map(branch -> new ChartDataDTO(branch.getBranchName(), branch.getTotalRevenue()))
                .collect(Collectors.toList());
    }

    private List<ChartDataDTO> generateAppointmentsChartData(List<BranchPerformanceDTO> branches) {
        return branches.stream()
                .map(branch -> new ChartDataDTO(branch.getBranchName(), BigDecimal.valueOf(branch.getTotalAppointments())))
                .collect(Collectors.toList());
    }
}


