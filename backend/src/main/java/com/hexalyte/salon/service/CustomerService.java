package com.hexalyte.salon.service;

import com.hexalyte.salon.dto.*;
import com.hexalyte.salon.model.Customer;
import com.hexalyte.salon.model.LoyaltyBenefit;
import com.hexalyte.salon.repository.CustomerRepository;
import com.hexalyte.salon.repository.LoyaltyBenefitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private LoyaltyBenefitRepository loyaltyBenefitRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CustomerDTO> searchCustomers(String query) {
        return customerRepository.searchByNameOrPhone(query).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CustomerDTO> getCustomersByMembershipLevel(String level) {
        Customer.MembershipLevel membershipLevel = Customer.MembershipLevel.valueOf(level.toUpperCase());
        return customerRepository.findByMembershipLevel(membershipLevel).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CustomerDTO> getTopCustomers() {
        return customerRepository.findTopCustomers().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return convertToDTO(customer);
    }

    public CustomerDTO getCustomerByPhone(String phone) {
        Customer customer = customerRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("Customer not found with phone: " + phone));
        return convertToDTO(customer);
    }

    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        // Check if customer with phone already exists
        if (customerRepository.findByPhone(customerDTO.getPhone()).isPresent()) {
            throw new RuntimeException("Customer with phone number already exists");
        }

        Customer customer = new Customer();
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setPhone(customerDTO.getPhone());
        customer.setEmail(customerDTO.getEmail());
        customer.setAddress(customerDTO.getAddress());
        customer.setDateOfBirth(customerDTO.getDateOfBirth());
        customer.setLoyaltyPoints(customerDTO.getLoyaltyPoints() != null ? customerDTO.getLoyaltyPoints() : 0);
        customer.setMembershipLevel(Customer.MembershipLevel.valueOf(customerDTO.getMembershipLevel()));
        customer.setTotalVisits(customerDTO.getTotalVisits() != null ? customerDTO.getTotalVisits() : 0);
        customer.setTotalSpent(customerDTO.getTotalSpent() != null ? customerDTO.getTotalSpent() : BigDecimal.ZERO);

        Customer savedCustomer = customerRepository.save(customer);
        return convertToDTO(savedCustomer);
    }

    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        // Check if phone number is being changed and if it already exists
        if (!customer.getPhone().equals(customerDTO.getPhone()) && 
            customerRepository.findByPhone(customerDTO.getPhone()).isPresent()) {
            throw new RuntimeException("Customer with phone number already exists");
        }

        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setPhone(customerDTO.getPhone());
        customer.setEmail(customerDTO.getEmail());
        customer.setAddress(customerDTO.getAddress());
        customer.setDateOfBirth(customerDTO.getDateOfBirth());
        customer.setLoyaltyPoints(customerDTO.getLoyaltyPoints() != null ? customerDTO.getLoyaltyPoints() : 0);
        customer.setMembershipLevel(Customer.MembershipLevel.valueOf(customerDTO.getMembershipLevel()));
        customer.setTotalVisits(customerDTO.getTotalVisits() != null ? customerDTO.getTotalVisits() : 0);
        customer.setTotalSpent(customerDTO.getTotalSpent() != null ? customerDTO.getTotalSpent() : BigDecimal.ZERO);

        Customer savedCustomer = customerRepository.save(customer);
        return convertToDTO(savedCustomer);
    }

    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        
        // Check if customer has appointments
        if (!customer.getAppointments().isEmpty()) {
            throw new RuntimeException("Cannot delete customer with existing appointments. Please handle appointments first.");
        }

        customerRepository.delete(customer);
    }

    public CustomerDTO updateLoyaltyPoints(Long id, Integer points) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        
        customer.setLoyaltyPoints(customer.getLoyaltyPoints() + points);
        
        // Update membership level based on loyalty points
        updateMembershipLevel(customer);
        
        Customer savedCustomer = customerRepository.save(customer);
        return convertToDTO(savedCustomer);
    }

    private void updateMembershipLevel(Customer customer) {
        int points = customer.getLoyaltyPoints();
        if (points >= 1000) {
            customer.setMembershipLevel(Customer.MembershipLevel.PLATINUM);
        } else if (points >= 500) {
            customer.setMembershipLevel(Customer.MembershipLevel.GOLD);
        } else if (points >= 200) {
            customer.setMembershipLevel(Customer.MembershipLevel.SILVER);
        } else {
            customer.setMembershipLevel(Customer.MembershipLevel.BRONZE);
        }
    }

    private CustomerDTO convertToDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setPhone(customer.getPhone());
        dto.setEmail(customer.getEmail());
        dto.setAddress(customer.getAddress());
        dto.setDateOfBirth(customer.getDateOfBirth());
        dto.setLoyaltyPoints(customer.getLoyaltyPoints());
        dto.setMembershipLevel(customer.getMembershipLevel().name());
        dto.setTotalVisits(customer.getTotalVisits());
        dto.setTotalSpent(customer.getTotalSpent());
        
        if (customer.getCreatedAt() != null) {
            dto.setCreatedAt(customer.getCreatedAt().format(formatter));
        }
        
        if (customer.getUpdatedAt() != null) {
            dto.setUpdatedAt(customer.getUpdatedAt().format(formatter));
        }
        
        dto.setAppointmentCount(customer.getAppointments().size());
        
        return dto;
    }
    
    // Customer History Methods
    public List<CustomerHistoryDTO> getCustomerHistory(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
        
        return customer.getAppointments().stream()
                .map(this::convertToHistoryDTO)
                .collect(Collectors.toList());
    }
    
    public List<CustomerHistoryDTO> getCustomerHistoryByDateRange(Long customerId, LocalDate startDate, LocalDate endDate) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
        
        return customer.getAppointments().stream()
                .filter(appointment -> !appointment.getAppointmentDate().isBefore(startDate) && 
                                     !appointment.getAppointmentDate().isAfter(endDate))
                .map(this::convertToHistoryDTO)
                .collect(Collectors.toList());
    }
    
    // Customer Preferences Methods
    public CustomerPreferencesDTO getCustomerPreferences(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
        
        return convertToPreferencesDTO(customer);
    }
    
    public CustomerPreferencesDTO updateCustomerPreferences(Long customerId, CustomerPreferencesDTO preferencesDTO) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
        
        customer.setPreferredStaffId(preferencesDTO.getPreferredStaffId() != null ? 
            Long.valueOf(preferencesDTO.getPreferredStaffId()) : null);
        customer.setPreferredBranchId(preferencesDTO.getPreferredBranchId() != null ? 
            Long.valueOf(preferencesDTO.getPreferredBranchId()) : null);
        customer.setPreferredTimeSlot(preferencesDTO.getPreferredTimeSlot());
        customer.setCommunicationPreference(preferencesDTO.getCommunicationPreference());
        customer.setReceivePromotions(preferencesDTO.getReceivePromotions());
        customer.setReceiveReminders(preferencesDTO.getReceiveReminders());
        customer.setPreferencesNotes(preferencesDTO.getNotes());
        
        Customer savedCustomer = customerRepository.save(customer);
        return convertToPreferencesDTO(savedCustomer);
    }
    
    // Loyalty Benefits Methods
    public List<LoyaltyBenefitDTO> getLoyaltyBenefits(String membershipLevel) {
        Customer.MembershipLevel level = Customer.MembershipLevel.valueOf(membershipLevel.toUpperCase());
        return loyaltyBenefitRepository.findByMembershipLevelAndIsActiveTrue(level).stream()
                .map(this::convertToLoyaltyBenefitDTO)
                .collect(Collectors.toList());
    }
    
    public List<LoyaltyBenefitDTO> getAllLoyaltyBenefits() {
        return loyaltyBenefitRepository.findAllActiveOrderedByLevelAndType().stream()
                .map(this::convertToLoyaltyBenefitDTO)
                .collect(Collectors.toList());
    }
    
    public LoyaltyBenefitDTO createLoyaltyBenefit(LoyaltyBenefitDTO benefitDTO) {
        LoyaltyBenefit benefit = new LoyaltyBenefit();
        benefit.setMembershipLevel(Customer.MembershipLevel.valueOf(benefitDTO.getMembershipLevel()));
        benefit.setBenefitType(LoyaltyBenefit.BenefitType.valueOf(benefitDTO.getBenefitType()));
        benefit.setDescription(benefitDTO.getDescription());
        benefit.setDiscountPercentage(benefitDTO.getDiscountPercentage());
        benefit.setDiscountAmount(benefitDTO.getDiscountAmount());
        benefit.setFreeServiceId(benefitDTO.getFreeServiceId() != null ? 
            Long.valueOf(benefitDTO.getFreeServiceId()) : null);
        benefit.setBonusPointsMultiplier(benefitDTO.getBonusPointsMultiplier());
        benefit.setIsActive(benefitDTO.getIsActive());
        
        LoyaltyBenefit savedBenefit = loyaltyBenefitRepository.save(benefit);
        return convertToLoyaltyBenefitDTO(savedBenefit);
    }
    
    public LoyaltyBenefitDTO updateLoyaltyBenefit(Long id, LoyaltyBenefitDTO benefitDTO) {
        LoyaltyBenefit benefit = loyaltyBenefitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loyalty benefit not found with id: " + id));
        
        benefit.setMembershipLevel(Customer.MembershipLevel.valueOf(benefitDTO.getMembershipLevel()));
        benefit.setBenefitType(LoyaltyBenefit.BenefitType.valueOf(benefitDTO.getBenefitType()));
        benefit.setDescription(benefitDTO.getDescription());
        benefit.setDiscountPercentage(benefitDTO.getDiscountPercentage());
        benefit.setDiscountAmount(benefitDTO.getDiscountAmount());
        benefit.setFreeServiceId(benefitDTO.getFreeServiceId() != null ? 
            Long.valueOf(benefitDTO.getFreeServiceId()) : null);
        benefit.setBonusPointsMultiplier(benefitDTO.getBonusPointsMultiplier());
        benefit.setIsActive(benefitDTO.getIsActive());
        
        LoyaltyBenefit savedBenefit = loyaltyBenefitRepository.save(benefit);
        return convertToLoyaltyBenefitDTO(savedBenefit);
    }
    
    public void deleteLoyaltyBenefit(Long id) {
        LoyaltyBenefit benefit = loyaltyBenefitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loyalty benefit not found with id: " + id));
        
        benefit.setIsActive(false);
        loyaltyBenefitRepository.save(benefit);
    }
    
    // Helper conversion methods
    private CustomerHistoryDTO convertToHistoryDTO(Customer customer) {
        // This would need to be implemented based on your Appointment structure
        // For now, returning a basic structure
        return new CustomerHistoryDTO();
    }
    
    private CustomerHistoryDTO convertToHistoryDTO(com.hexalyte.salon.model.Appointment appointment) {
        CustomerHistoryDTO dto = new CustomerHistoryDTO();
        dto.setAppointmentId(appointment.getId());
        dto.setAppointmentDate(appointment.getAppointmentDate());
        dto.setStartTime(appointment.getStartTime());
        dto.setEndTime(appointment.getEndTime());
        dto.setStatus(appointment.getStatus().name());
        dto.setStaffName(appointment.getStaff().getFirstName() + " " + appointment.getStaff().getLastName());
        dto.setBranchName(appointment.getBranch().getName());
        dto.setTotalAmount(appointment.getTotalAmount());
        dto.setNotes(appointment.getNotes());
        dto.setCreatedAt(appointment.getCreatedAt());
        
        // Convert appointment services if needed
        // dto.setServices(appointment.getAppointmentServices().stream()...);
        
        return dto;
    }
    
    private CustomerPreferencesDTO convertToPreferencesDTO(Customer customer) {
        CustomerPreferencesDTO dto = new CustomerPreferencesDTO();
        dto.setCustomerId(customer.getId());
        dto.setPreferredStaffId(customer.getPreferredStaffId() != null ? 
            customer.getPreferredStaffId().toString() : null);
        dto.setPreferredBranchId(customer.getPreferredBranchId() != null ? 
            customer.getPreferredBranchId().toString() : null);
        dto.setPreferredTimeSlot(customer.getPreferredTimeSlot());
        dto.setCommunicationPreference(customer.getCommunicationPreference());
        dto.setReceivePromotions(customer.getReceivePromotions());
        dto.setReceiveReminders(customer.getReceiveReminders());
        dto.setNotes(customer.getPreferencesNotes());
        
        return dto;
    }
    
    private LoyaltyBenefitDTO convertToLoyaltyBenefitDTO(LoyaltyBenefit benefit) {
        LoyaltyBenefitDTO dto = new LoyaltyBenefitDTO();
        dto.setId(benefit.getId());
        dto.setMembershipLevel(benefit.getMembershipLevel().name());
        dto.setBenefitType(benefit.getBenefitType().name());
        dto.setDescription(benefit.getDescription());
        dto.setDiscountPercentage(benefit.getDiscountPercentage());
        dto.setDiscountAmount(benefit.getDiscountAmount());
        dto.setFreeServiceId(benefit.getFreeServiceId() != null ? 
            benefit.getFreeServiceId().toString() : null);
        dto.setBonusPointsMultiplier(benefit.getBonusPointsMultiplier());
        dto.setIsActive(benefit.getIsActive());
        dto.setCreatedAt(benefit.getCreatedAt());
        dto.setUpdatedAt(benefit.getUpdatedAt());
        
        return dto;
    }
}


