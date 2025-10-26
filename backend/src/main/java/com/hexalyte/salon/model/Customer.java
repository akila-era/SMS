package com.hexalyte.salon.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "customers")
@EntityListeners(AuditingEntityListener.class)
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "first_name")
    private String firstName;

    @NotBlank
    @Column(name = "last_name")
    private String lastName;

    @NotBlank
    @Column(unique = true)
    private String phone;

    @Email
    private String email;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "loyalty_points")
    private Integer loyaltyPoints = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "membership_level")
    private MembershipLevel membershipLevel = MembershipLevel.BRONZE;

    @Column(name = "total_visits")
    private Integer totalVisits = 0;

    @Column(name = "total_spent")
    private BigDecimal totalSpent = BigDecimal.ZERO;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> appointments = new ArrayList<>();

    // Customer preferences
    @Column(name = "preferred_staff_id")
    private Long preferredStaffId;

    @Column(name = "preferred_branch_id")
    private Long preferredBranchId;

    @Column(name = "preferred_time_slot")
    private String preferredTimeSlot;

    @Column(name = "communication_preference")
    private String communicationPreference = "SMS";

    @Column(name = "receive_promotions")
    private Boolean receivePromotions = true;

    @Column(name = "receive_reminders")
    private Boolean receiveReminders = true;

    @Column(name = "preferences_notes", columnDefinition = "TEXT")
    private String preferencesNotes;

    // Constructors
    public Customer() {}

    public Customer(String firstName, String lastName, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(Integer loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public MembershipLevel getMembershipLevel() {
        return membershipLevel;
    }

    public void setMembershipLevel(MembershipLevel membershipLevel) {
        this.membershipLevel = membershipLevel;
    }

    public Integer getTotalVisits() {
        return totalVisits;
    }

    public void setTotalVisits(Integer totalVisits) {
        this.totalVisits = totalVisits;
    }

    public BigDecimal getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(BigDecimal totalSpent) {
        this.totalSpent = totalSpent;
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

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    // Preference getters and setters
    public Long getPreferredStaffId() {
        return preferredStaffId;
    }

    public void setPreferredStaffId(Long preferredStaffId) {
        this.preferredStaffId = preferredStaffId;
    }

    public Long getPreferredBranchId() {
        return preferredBranchId;
    }

    public void setPreferredBranchId(Long preferredBranchId) {
        this.preferredBranchId = preferredBranchId;
    }

    public String getPreferredTimeSlot() {
        return preferredTimeSlot;
    }

    public void setPreferredTimeSlot(String preferredTimeSlot) {
        this.preferredTimeSlot = preferredTimeSlot;
    }

    public String getCommunicationPreference() {
        return communicationPreference;
    }

    public void setCommunicationPreference(String communicationPreference) {
        this.communicationPreference = communicationPreference;
    }

    public Boolean getReceivePromotions() {
        return receivePromotions;
    }

    public void setReceivePromotions(Boolean receivePromotions) {
        this.receivePromotions = receivePromotions;
    }

    public Boolean getReceiveReminders() {
        return receiveReminders;
    }

    public void setReceiveReminders(Boolean receiveReminders) {
        this.receiveReminders = receiveReminders;
    }

    public String getPreferencesNotes() {
        return preferencesNotes;
    }

    public void setPreferencesNotes(String preferencesNotes) {
        this.preferencesNotes = preferencesNotes;
    }

    public enum MembershipLevel {
        BRONZE, SILVER, GOLD, PLATINUM
    }
}


