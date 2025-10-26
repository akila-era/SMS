package com.hexalyte.salon.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "branch_user_access")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BranchUserAccess {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Enumerated(EnumType.STRING)
    @NotNull
    private AccessRole role;

    @Enumerated(EnumType.STRING)
    private AccessLevel accessLevel = AccessLevel.LIMITED;

    @Column(name = "can_view_appointments")
    private Boolean canViewAppointments = true;

    @Column(name = "can_view_inventory")
    private Boolean canViewInventory = true;

    @Column(name = "can_view_financial_reports")
    private Boolean canViewFinancialReports = false;

    @Column(name = "can_view_payroll")
    private Boolean canViewPayroll = false;

    @Column(name = "can_manage_staff")
    private Boolean canManageStaff = false;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum AccessRole {
        ADMIN, MANAGER, STAFF, ACCOUNTANT, RECEPTIONIST
    }

    public enum AccessLevel {
        FULL, LIMITED
    }

    // Constructors
    public BranchUserAccess() {}

    public BranchUserAccess(User user, Branch branch, AccessRole role) {
        this.user = user;
        this.branch = branch;
        this.role = role;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public AccessRole getRole() {
        return role;
    }

    public void setRole(AccessRole role) {
        this.role = role;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
