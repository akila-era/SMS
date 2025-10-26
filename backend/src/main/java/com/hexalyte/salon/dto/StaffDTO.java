package com.hexalyte.salon.dto;

import com.hexalyte.salon.model.Staff;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;

import java.math.BigDecimal;
import java.time.LocalDate;

public class StaffDTO {
    
    private Long id;
    private Long userId;
    private String username;
    
    @NotBlank(message = "Employee code is required")
    private String employeeCode;
    
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    private String phone;
    
    @Email(message = "Invalid email format")
    private String email;
    
    private String gender;
    private String address;
    private String designation;
    private String skillSet;
    private LocalDate joinDate;
    private String salaryType;
    
    @DecimalMin(value = "0.0", message = "Base salary must be non-negative")
    private BigDecimal baseSalary;
    
    @DecimalMin(value = "0.0", message = "Commission rate must be non-negative")
    private BigDecimal commissionRate;
    
    private String bankDetails;
    private String status;
    
    @NotNull(message = "Branch is required")
    private Long branchId;
    private String branchName;
    
    private String createdAt;
    private String updatedAt;
    
    // Performance metrics
    private BigDecimal totalCommission;
    private Integer appointmentCount;
    private BigDecimal totalRevenue;
    private BigDecimal averageRating;
    private Integer totalServices;
    private Integer presentDays;
    private Integer absentDays;
    private BigDecimal totalHours;
    private BigDecimal overtimeHours;

    // Constructors
    public StaffDTO() {}

    public StaffDTO(String employeeCode, String firstName, String lastName, Long branchId) {
        this.employeeCode = employeeCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.branchId = branchId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getSkillSet() {
        return skillSet;
    }

    public void setSkillSet(String skillSet) {
        this.skillSet = skillSet;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public String getSalaryType() {
        return salaryType;
    }

    public void setSalaryType(String salaryType) {
        this.salaryType = salaryType;
    }

    public BigDecimal getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(BigDecimal baseSalary) {
        this.baseSalary = baseSalary;
    }

    public BigDecimal getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(BigDecimal commissionRate) {
        this.commissionRate = commissionRate;
    }

    public String getBankDetails() {
        return bankDetails;
    }

    public void setBankDetails(String bankDetails) {
        this.bankDetails = bankDetails;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Performance metrics getters and setters
    public BigDecimal getTotalCommission() {
        return totalCommission;
    }

    public void setTotalCommission(BigDecimal totalCommission) {
        this.totalCommission = totalCommission;
    }

    public Integer getAppointmentCount() {
        return appointmentCount;
    }

    public void setAppointmentCount(Integer appointmentCount) {
        this.appointmentCount = appointmentCount;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(BigDecimal averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getTotalServices() {
        return totalServices;
    }

    public void setTotalServices(Integer totalServices) {
        this.totalServices = totalServices;
    }

    public Integer getPresentDays() {
        return presentDays;
    }

    public void setPresentDays(Integer presentDays) {
        this.presentDays = presentDays;
    }

    public Integer getAbsentDays() {
        return absentDays;
    }

    public void setAbsentDays(Integer absentDays) {
        this.absentDays = absentDays;
    }

    public BigDecimal getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(BigDecimal totalHours) {
        this.totalHours = totalHours;
    }

    public BigDecimal getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(BigDecimal overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    // Helper methods
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean isActive() {
        return "ACTIVE".equals(status);
    }

    public boolean isOnLeave() {
        return "ON_LEAVE".equals(status);
    }

    public boolean isInactive() {
        return "INACTIVE".equals(status);
    }

    public String[] getSkills() {
        if (skillSet == null || skillSet.trim().isEmpty()) {
            return new String[0];
        }
        return skillSet.split(",");
    }

    public BigDecimal calculateTotalPayout() {
        if ("FIXED".equals(salaryType)) {
            return baseSalary != null ? baseSalary : BigDecimal.ZERO;
        } else if ("COMMISSION".equals(salaryType)) {
            return totalCommission != null ? totalCommission : BigDecimal.ZERO;
        } else { // HYBRID
            BigDecimal base = baseSalary != null ? baseSalary : BigDecimal.ZERO;
            BigDecimal commission = totalCommission != null ? totalCommission : BigDecimal.ZERO;
            return base.add(commission);
        }
    }
}


