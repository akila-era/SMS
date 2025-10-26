package com.hexalyte.salon.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "staff")
@EntityListeners(AuditingEntityListener.class)
public class Staff {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank
    @Column(name = "employee_code", unique = true)
    private String employeeCode;

    @NotBlank
    @Column(name = "first_name")
    private String firstName;

    @NotBlank
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone")
    private String phone;

    @Email
    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(name = "designation")
    private String designation;

    @Column(name = "skill_set", columnDefinition = "TEXT")
    private String skillSet;

    @Column(name = "join_date")
    private LocalDate joinDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "salary_type")
    private SalaryType salaryType;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "base_salary")
    private BigDecimal baseSalary = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "commission_rate")
    private BigDecimal commissionRate = BigDecimal.ZERO;

    @Column(name = "bank_details", columnDefinition = "TEXT")
    private String bankDetails;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StaffStatus status = StaffStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Enums
    public enum Gender {
        MALE, FEMALE, OTHER
    }

    public enum SalaryType {
        FIXED, COMMISSION, HYBRID
    }

    public enum StaffStatus {
        ACTIVE, INACTIVE, ON_LEAVE
    }

    // Constructors
    public Staff() {}

    public Staff(User user, String employeeCode, String firstName, String lastName, Branch branch) {
        this.user = user;
        this.employeeCode = employeeCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.branch = branch;
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
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

    public SalaryType getSalaryType() {
        return salaryType;
    }

    public void setSalaryType(SalaryType salaryType) {
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

    public StaffStatus getStatus() {
        return status;
    }

    public void setStatus(StaffStatus status) {
        this.status = status;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
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

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public List<Appointment> getAppointments() {
        // This would typically be a @OneToMany relationship, but for now return empty list
        // In a real implementation, you would add:
        // @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        // private List<Appointment> appointments = new ArrayList<>();
        return new ArrayList<>();
    }

    // Helper methods
    public boolean isActive() {
        return status == StaffStatus.ACTIVE;
    }

    public boolean isOnLeave() {
        return status == StaffStatus.ON_LEAVE;
    }

    public boolean isInactive() {
        return status == StaffStatus.INACTIVE;
    }

    public boolean hasSkill(String skill) {
        return skillSet != null && skillSet.toLowerCase().contains(skill.toLowerCase());
    }

    public String[] getSkills() {
        if (skillSet == null || skillSet.trim().isEmpty()) {
            return new String[0];
        }
        return skillSet.split(",");
    }

    public BigDecimal calculateTotalPayout(BigDecimal commissionAmount) {
        if (salaryType == SalaryType.FIXED) {
            return baseSalary;
        } else if (salaryType == SalaryType.COMMISSION) {
            return commissionAmount;
        } else { // HYBRID
            return baseSalary.add(commissionAmount);
        }
    }
}