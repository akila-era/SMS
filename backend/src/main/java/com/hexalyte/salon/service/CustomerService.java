package com.hexalyte.salon.service;

import com.hexalyte.salon.dto.CustomerDTO;
import com.hexalyte.salon.model.Customer;
import com.hexalyte.salon.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

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
}
