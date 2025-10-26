package com.hexalyte.salon.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CommissionReportDTO {
    private String reportType; // "BRANCH_WISE", "STAFF_WISE", "SUMMARY"
    private String period; // "2025-10", "2025-Q4", "2025"
    private LocalDateTime generatedAt;
    private List<BranchCommissionSummary> branchSummaries;
    private List<StaffCommissionSummary> staffSummaries;
    private CommissionDashboardData dashboardData;

    // Constructors
    public CommissionReportDTO() {}

    public CommissionReportDTO(String reportType, String period) {
        this.reportType = reportType;
        this.period = period;
        this.generatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public List<BranchCommissionSummary> getBranchSummaries() {
        return branchSummaries;
    }

    public void setBranchSummaries(List<BranchCommissionSummary> branchSummaries) {
        this.branchSummaries = branchSummaries;
    }

    public List<StaffCommissionSummary> getStaffSummaries() {
        return staffSummaries;
    }

    public void setStaffSummaries(List<StaffCommissionSummary> staffSummaries) {
        this.staffSummaries = staffSummaries;
    }

    public CommissionDashboardData getDashboardData() {
        return dashboardData;
    }

    public void setDashboardData(CommissionDashboardData dashboardData) {
        this.dashboardData = dashboardData;
    }

    // Inner classes
    public static class BranchCommissionSummary {
        private Long branchId;
        private String branchName;
        private String month;
        private Integer totalServices;
        private BigDecimal totalCommission;
        private BigDecimal averageCommissionPerService;

        // Constructors
        public BranchCommissionSummary() {}

        public BranchCommissionSummary(Long branchId, String branchName, String month, 
                                     Integer totalServices, BigDecimal totalCommission) {
            this.branchId = branchId;
            this.branchName = branchName;
            this.month = month;
            this.totalServices = totalServices;
            this.totalCommission = totalCommission;
            this.averageCommissionPerService = totalServices > 0 ? 
                totalCommission.divide(BigDecimal.valueOf(totalServices), 2, BigDecimal.ROUND_HALF_UP) : 
                BigDecimal.ZERO;
        }

        // Getters and Setters
        public Long getBranchId() {
            return branchId;
        }

        public void setBranchId(Long branchId) {
            this.branchId = branchId;
        }

        public String getBranchName() {
            return branchName;
        }

        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public Integer getTotalServices() {
            return totalServices;
        }

        public void setTotalServices(Integer totalServices) {
            this.totalServices = totalServices;
        }

        public BigDecimal getTotalCommission() {
            return totalCommission;
        }

        public void setTotalCommission(BigDecimal totalCommission) {
            this.totalCommission = totalCommission;
        }

        public BigDecimal getAverageCommissionPerService() {
            return averageCommissionPerService;
        }

        public void setAverageCommissionPerService(BigDecimal averageCommissionPerService) {
            this.averageCommissionPerService = averageCommissionPerService;
        }
    }

    public static class StaffCommissionSummary {
        private Long staffId;
        private String staffName;
        private String staffEmployeeCode;
        private Long branchId;
        private String branchName;
        private String month;
        private Integer totalServices;
        private BigDecimal totalCommission;
        private BigDecimal averageCommissionPerService;

        // Constructors
        public StaffCommissionSummary() {}

        public StaffCommissionSummary(Long staffId, String staffName, String staffEmployeeCode,
                                    Long branchId, String branchName, String month,
                                    Integer totalServices, BigDecimal totalCommission) {
            this.staffId = staffId;
            this.staffName = staffName;
            this.staffEmployeeCode = staffEmployeeCode;
            this.branchId = branchId;
            this.branchName = branchName;
            this.month = month;
            this.totalServices = totalServices;
            this.totalCommission = totalCommission;
            this.averageCommissionPerService = totalServices > 0 ? 
                totalCommission.divide(BigDecimal.valueOf(totalServices), 2, BigDecimal.ROUND_HALF_UP) : 
                BigDecimal.ZERO;
        }

        // Getters and Setters
        public Long getStaffId() {
            return staffId;
        }

        public void setStaffId(Long staffId) {
            this.staffId = staffId;
        }

        public String getStaffName() {
            return staffName;
        }

        public void setStaffName(String staffName) {
            this.staffName = staffName;
        }

        public String getStaffEmployeeCode() {
            return staffEmployeeCode;
        }

        public void setStaffEmployeeCode(String staffEmployeeCode) {
            this.staffEmployeeCode = staffEmployeeCode;
        }

        public Long getBranchId() {
            return branchId;
        }

        public void setBranchId(Long branchId) {
            this.branchId = branchId;
        }

        public String getBranchName() {
            return branchName;
        }

        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public Integer getTotalServices() {
            return totalServices;
        }

        public void setTotalServices(Integer totalServices) {
            this.totalServices = totalServices;
        }

        public BigDecimal getTotalCommission() {
            return totalCommission;
        }

        public void setTotalCommission(BigDecimal totalCommission) {
            this.totalCommission = totalCommission;
        }

        public BigDecimal getAverageCommissionPerService() {
            return averageCommissionPerService;
        }

        public void setAverageCommissionPerService(BigDecimal averageCommissionPerService) {
            this.averageCommissionPerService = averageCommissionPerService;
        }
    }

    public static class CommissionDashboardData {
        private BigDecimal totalCommissionsThisMonth;
        private Integer totalPendingApprovals;
        private BigDecimal totalPayoutPendingApproval;
        private List<TopEarningStaff> topEarningStaff;
        private List<BranchComparison> branchComparisons;

        // Constructors
        public CommissionDashboardData() {}

        // Getters and Setters
        public BigDecimal getTotalCommissionsThisMonth() {
            return totalCommissionsThisMonth;
        }

        public void setTotalCommissionsThisMonth(BigDecimal totalCommissionsThisMonth) {
            this.totalCommissionsThisMonth = totalCommissionsThisMonth;
        }

        public Integer getTotalPendingApprovals() {
            return totalPendingApprovals;
        }

        public void setTotalPendingApprovals(Integer totalPendingApprovals) {
            this.totalPendingApprovals = totalPendingApprovals;
        }

        public BigDecimal getTotalPayoutPendingApproval() {
            return totalPayoutPendingApproval;
        }

        public void setTotalPayoutPendingApproval(BigDecimal totalPayoutPendingApproval) {
            this.totalPayoutPendingApproval = totalPayoutPendingApproval;
        }

        public List<TopEarningStaff> getTopEarningStaff() {
            return topEarningStaff;
        }

        public void setTopEarningStaff(List<TopEarningStaff> topEarningStaff) {
            this.topEarningStaff = topEarningStaff;
        }

        public List<BranchComparison> getBranchComparisons() {
            return branchComparisons;
        }

        public void setBranchComparisons(List<BranchComparison> branchComparisons) {
            this.branchComparisons = branchComparisons;
        }
    }

    public static class TopEarningStaff {
        private Long staffId;
        private String staffName;
        private String branchName;
        private BigDecimal totalEarnings;
        private Integer totalServices;

        // Constructors
        public TopEarningStaff() {}

        public TopEarningStaff(Long staffId, String staffName, String branchName, 
                              BigDecimal totalEarnings, Integer totalServices) {
            this.staffId = staffId;
            this.staffName = staffName;
            this.branchName = branchName;
            this.totalEarnings = totalEarnings;
            this.totalServices = totalServices;
        }

        // Getters and Setters
        public Long getStaffId() {
            return staffId;
        }

        public void setStaffId(Long staffId) {
            this.staffId = staffId;
        }

        public String getStaffName() {
            return staffName;
        }

        public void setStaffName(String staffName) {
            this.staffName = staffName;
        }

        public String getBranchName() {
            return branchName;
        }

        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }

        public BigDecimal getTotalEarnings() {
            return totalEarnings;
        }

        public void setTotalEarnings(BigDecimal totalEarnings) {
            this.totalEarnings = totalEarnings;
        }

        public Integer getTotalServices() {
            return totalServices;
        }

        public void setTotalServices(Integer totalServices) {
            this.totalServices = totalServices;
        }
    }

    public static class BranchComparison {
        private Long branchId;
        private String branchName;
        private BigDecimal totalCommission;
        private Integer totalServices;
        private BigDecimal averageCommissionPerService;

        // Constructors
        public BranchComparison() {}

        public BranchComparison(Long branchId, String branchName, BigDecimal totalCommission, 
                              Integer totalServices) {
            this.branchId = branchId;
            this.branchName = branchName;
            this.totalCommission = totalCommission;
            this.totalServices = totalServices;
            this.averageCommissionPerService = totalServices > 0 ? 
                totalCommission.divide(BigDecimal.valueOf(totalServices), 2, BigDecimal.ROUND_HALF_UP) : 
                BigDecimal.ZERO;
        }

        // Getters and Setters
        public Long getBranchId() {
            return branchId;
        }

        public void setBranchId(Long branchId) {
            this.branchId = branchId;
        }

        public String getBranchName() {
            return branchName;
        }

        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }

        public BigDecimal getTotalCommission() {
            return totalCommission;
        }

        public void setTotalCommission(BigDecimal totalCommission) {
            this.totalCommission = totalCommission;
        }

        public Integer getTotalServices() {
            return totalServices;
        }

        public void setTotalServices(Integer totalServices) {
            this.totalServices = totalServices;
        }

        public BigDecimal getAverageCommissionPerService() {
            return averageCommissionPerService;
        }

        public void setAverageCommissionPerService(BigDecimal averageCommissionPerService) {
            this.averageCommissionPerService = averageCommissionPerService;
        }
    }
}
