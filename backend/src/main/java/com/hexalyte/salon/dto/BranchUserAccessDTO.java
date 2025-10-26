package com.hexalyte.salon.dto;

import com.hexalyte.salon.model.BranchUserAccess;
import jakarta.validation.constraints.NotNull;

public class BranchUserAccessDTO {
    
    private Long id;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    private String userName;
    private String userEmail;
    
    @NotNull(message = "Branch ID is required")
    private Long branchId;
    
    private String branchName;
    private String branchCode;
    
    @NotNull(message = "Role is required")
    private BranchUserAccess.AccessRole role;
    
    private BranchUserAccess.AccessLevel accessLevel = BranchUserAccess.AccessLevel.LIMITED;
    
    private Boolean canViewAppointments = true;
    private Boolean canViewInventory = true;
    private Boolean canViewFinancialReports = false;
    private Boolean canViewPayroll = false;
    private Boolean canManageStaff = false;
    
    private String createdAt;
    private String updatedAt;

    // Constructors
    public BranchUserAccessDTO() {}

    public BranchUserAccessDTO(Long userId, Long branchId, BranchUserAccess.AccessRole role) {
        this.userId = userId;
        this.branchId = branchId;
        this.role = role;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
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

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public BranchUserAccess.AccessRole getRole() {
        return role;
    }

    public void setRole(BranchUserAccess.AccessRole role) {
        this.role = role;
    }

    public BranchUserAccess.AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(BranchUserAccess.AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public Boolean getCanViewAppointments() {
        return canViewAppointments;
    }

    public void setCanViewAppointments(Boolean canViewAppointments) {
        this.canViewAppointments = canViewAppointments;
    }

    public Boolean getCanViewInventory() {
        return canViewInventory;
    }

    public void setCanViewInventory(Boolean canViewInventory) {
        this.canViewInventory = canViewInventory;
    }

    public Boolean getCanViewFinancialReports() {
        return canViewFinancialReports;
    }

    public void setCanViewFinancialReports(Boolean canViewFinancialReports) {
        this.canViewFinancialReports = canViewFinancialReports;
    }

    public Boolean getCanViewPayroll() {
        return canViewPayroll;
    }

    public void setCanViewPayroll(Boolean canViewPayroll) {
        this.canViewPayroll = canViewPayroll;
    }

    public Boolean getCanManageStaff() {
        return canManageStaff;
    }

    public void setCanManageStaff(Boolean canManageStaff) {
        this.canManageStaff = canManageStaff;
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
}
