-- Multi-Branch Salon Management System Database Schema

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS salon_management;
USE salon_management;

-- Branch table (Advanced Version)
CREATE TABLE IF NOT EXISTS branch (
    branch_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    branch_code VARCHAR(20) UNIQUE NOT NULL,
    branch_name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    city VARCHAR(50),
    postal_code VARCHAR(20),
    contact_number VARCHAR(20),
    email VARCHAR(100),
    manager_id BIGINT NULL,
    working_hours JSON, -- Example: {"open":"09:00","close":"19:00","days":["MON","TUE","WED","THU","FRI","SAT"]}
    status ENUM('ACTIVE','INACTIVE') DEFAULT 'ACTIVE',
    logo_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT,
    FOREIGN KEY (manager_id) REFERENCES staff(id) ON DELETE SET NULL,
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (updated_by) REFERENCES users(id) ON DELETE SET NULL
);

-- Users table (for authentication)
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'BRANCH_MANAGER', 'RECEPTIONIST', 'BEAUTICIAN', 'ACCOUNTANT') NOT NULL,
    branch_id BIGINT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (branch_id) REFERENCES branch(branch_id) ON DELETE SET NULL
);

-- Branch User Access table (Advanced Version)
CREATE TABLE IF NOT EXISTS branch_user_access (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    branch_id BIGINT NOT NULL,
    role ENUM('ADMIN','MANAGER','STAFF','ACCOUNTANT','RECEPTIONIST') NOT NULL,
    access_level ENUM('FULL','LIMITED') DEFAULT 'LIMITED',
    can_view_appointments BOOLEAN DEFAULT TRUE,
    can_view_inventory BOOLEAN DEFAULT TRUE,
    can_view_financial_reports BOOLEAN DEFAULT FALSE,
    can_view_payroll BOOLEAN DEFAULT FALSE,
    can_manage_staff BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (branch_id) REFERENCES branch(branch_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_branch_role (user_id, branch_id, role)
);

-- Staff table (Advanced Version)
CREATE TABLE IF NOT EXISTS staff (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    employee_code VARCHAR(20) UNIQUE NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(255),
    gender ENUM('MALE', 'FEMALE', 'OTHER'),
    address TEXT,
    designation VARCHAR(100),
    skill_set TEXT,
    join_date DATE,
    salary_type ENUM('FIXED', 'COMMISSION', 'HYBRID') DEFAULT 'FIXED',
    base_salary DECIMAL(10,2) DEFAULT 0.00,
    commission_rate DECIMAL(5,2) DEFAULT 0.00,
    bank_details TEXT,
    status ENUM('ACTIVE', 'INACTIVE', 'ON_LEAVE') DEFAULT 'ACTIVE',
    branch_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (branch_id) REFERENCES branch(branch_id) ON DELETE CASCADE
);

-- Services table
CREATE TABLE IF NOT EXISTS services (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    duration_minutes INT NOT NULL,
    commission_rate DECIMAL(5,2) DEFAULT 0.00,
    category VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Customers table
CREATE TABLE IF NOT EXISTS customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(255),
    address TEXT,
    date_of_birth DATE,
    loyalty_points INT DEFAULT 0,
    membership_level ENUM('BRONZE', 'SILVER', 'GOLD', 'PLATINUM') DEFAULT 'BRONZE',
    total_visits INT DEFAULT 0,
    total_spent DECIMAL(10,2) DEFAULT 0.00,
    preferred_staff_id BIGINT,
    preferred_branch_id BIGINT,
    preferred_time_slot VARCHAR(50),
    communication_preference ENUM('EMAIL', 'SMS', 'PHONE') DEFAULT 'SMS',
    receive_promotions BOOLEAN DEFAULT TRUE,
    receive_reminders BOOLEAN DEFAULT TRUE,
    preferences_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (preferred_staff_id) REFERENCES staff(id) ON DELETE SET NULL,
    FOREIGN KEY (preferred_branch_id) REFERENCES branch(branch_id) ON DELETE SET NULL
);

-- Appointments table
CREATE TABLE IF NOT EXISTS appointments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    staff_id BIGINT NOT NULL,
    branch_id BIGINT NOT NULL,
    appointment_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    status ENUM('BOOKED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED', 'NO_SHOW') DEFAULT 'BOOKED',
    notes TEXT,
    total_amount DECIMAL(10,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
    FOREIGN KEY (staff_id) REFERENCES staff(id) ON DELETE CASCADE,
    FOREIGN KEY (branch_id) REFERENCES branch(branch_id) ON DELETE CASCADE
);

-- Appointment Services junction table
CREATE TABLE IF NOT EXISTS appointment_service (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    commission_rate DECIMAL(5,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (appointment_id) REFERENCES appointments(id) ON DELETE CASCADE,
    FOREIGN KEY (service_id) REFERENCES services(id) ON DELETE CASCADE
);

-- Commission table
CREATE TABLE IF NOT EXISTS commission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    staff_id BIGINT NOT NULL,
    appointment_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    branch_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    commission_rate DECIMAL(5,2) NOT NULL,
    commission_date DATE NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'PAID') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (staff_id) REFERENCES staff(id) ON DELETE CASCADE,
    FOREIGN KEY (appointment_id) REFERENCES appointments(id) ON DELETE CASCADE,
    FOREIGN KEY (service_id) REFERENCES services(id) ON DELETE CASCADE,
    FOREIGN KEY (branch_id) REFERENCES branch(branch_id) ON DELETE CASCADE
);

-- Enhanced Billing table
CREATE TABLE IF NOT EXISTS billing (
    bill_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bill_number VARCHAR(50) UNIQUE NOT NULL,
    branch_id BIGINT NOT NULL,
    appointment_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    bill_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    subtotal DECIMAL(10,2) NOT NULL,
    discount_amount DECIMAL(10,2) DEFAULT 0.00,
    tax_amount DECIMAL(10,2) DEFAULT 0.00,
    loyalty_redeemed DECIMAL(10,2) DEFAULT 0.00,
    total_amount DECIMAL(10,2) NOT NULL,
    paid_amount DECIMAL(10,2) DEFAULT 0.00,
    balance_amount DECIMAL(10,2) DEFAULT 0.00,
    status ENUM('PAID','PARTIAL','UNPAID','REFUNDED','CANCELLED') DEFAULT 'UNPAID',
    payment_method VARCHAR(50),
    remarks TEXT,
    created_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (branch_id) REFERENCES branch(branch_id) ON DELETE CASCADE,
    FOREIGN KEY (appointment_id) REFERENCES appointments(id) ON DELETE CASCADE,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL
);

-- Billing Item table
CREATE TABLE IF NOT EXISTS billing_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bill_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    staff_id BIGINT NOT NULL,
    quantity INT DEFAULT 1,
    unit_price DECIMAL(10,2) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    commission_generated BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (bill_id) REFERENCES billing(bill_id) ON DELETE CASCADE,
    FOREIGN KEY (service_id) REFERENCES services(id) ON DELETE CASCADE,
    FOREIGN KEY (staff_id) REFERENCES staff(id) ON DELETE CASCADE
);

-- Payment Transaction table
CREATE TABLE IF NOT EXISTS payment_transaction (
    transaction_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bill_id BIGINT NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    reference_no VARCHAR(100),
    status ENUM('SUCCESS','FAILED','PENDING','REFUNDED','CANCELLED') DEFAULT 'SUCCESS',
    gateway_response TEXT,
    gateway_transaction_id VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (bill_id) REFERENCES billing(bill_id) ON DELETE CASCADE
);

-- Product table (Enhanced for Inventory Management)
CREATE TABLE IF NOT EXISTS product (
    product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) UNIQUE NOT NULL,
    category VARCHAR(50),
    brand VARCHAR(50),
    uom VARCHAR(20),
    product_type ENUM('CONSUMABLE','RETAIL','TOOL') NOT NULL,
    cost_price DECIMAL(10,2) NOT NULL,
    selling_price DECIMAL(10,2) DEFAULT 0.00,
    alert_quantity DECIMAL(10,2) DEFAULT 0,
    tax_rate DECIMAL(5,2) DEFAULT 0,
    status ENUM('ACTIVE','INACTIVE') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Supplier table
CREATE TABLE IF NOT EXISTS supplier (
    supplier_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    contact_number VARCHAR(20),
    email VARCHAR(100),
    address VARCHAR(255),
    payment_terms VARCHAR(100),
    status ENUM('ACTIVE','INACTIVE') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Inventory table (branch-specific stock)
CREATE TABLE IF NOT EXISTS inventory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    branch_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity DECIMAL(10,2) DEFAULT 0,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (branch_id) REFERENCES branch(branch_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE,
    UNIQUE KEY unique_branch_product (branch_id, product_id)
);

-- Inventory Transaction table
CREATE TABLE IF NOT EXISTS inventory_transaction (
    transaction_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    branch_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    type ENUM('PURCHASE','ADJUSTMENT','TRANSFER','USAGE') NOT NULL,
    quantity DECIMAL(10,2) NOT NULL,
    unit_price DECIMAL(10,2) DEFAULT 0.00,
    total_amount DECIMAL(10,2) DEFAULT 0.00,
    supplier_id BIGINT NULL,
    reference_number VARCHAR(100),
    remarks TEXT,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (branch_id) REFERENCES branch(branch_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE,
    FOREIGN KEY (supplier_id) REFERENCES supplier(supplier_id) ON DELETE SET NULL,
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE
);

-- Inventory Usage table (linked to appointments)
CREATE TABLE IF NOT EXISTS inventory_usage (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    staff_id BIGINT NOT NULL,
    branch_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity_used DECIMAL(10,2) NOT NULL,
    unit_cost DECIMAL(10,2) NOT NULL,
    total_cost DECIMAL(10,2) NOT NULL,
    used_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (appointment_id) REFERENCES appointments(id) ON DELETE CASCADE,
    FOREIGN KEY (service_id) REFERENCES services(id) ON DELETE CASCADE,
    FOREIGN KEY (staff_id) REFERENCES staff(id) ON DELETE CASCADE,
    FOREIGN KEY (branch_id) REFERENCES branch(branch_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE
);

-- Service Product Mapping table (default products per service)
CREATE TABLE IF NOT EXISTS service_product_mapping (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    service_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    default_quantity DECIMAL(10,2) NOT NULL,
    is_required BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (service_id) REFERENCES services(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE,
    UNIQUE KEY unique_service_product (service_id, product_id)
);

-- Low Stock Alert table
CREATE TABLE IF NOT EXISTS low_stock_alert (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    branch_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    current_quantity DECIMAL(10,2) NOT NULL,
    alert_quantity DECIMAL(10,2) NOT NULL,
    alert_type ENUM('LOW_STOCK','OUT_OF_STOCK') NOT NULL,
    is_resolved BOOLEAN DEFAULT FALSE,
    resolved_at TIMESTAMP NULL,
    resolved_by BIGINT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (branch_id) REFERENCES branch(branch_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE,
    FOREIGN KEY (resolved_by) REFERENCES users(id) ON DELETE SET NULL
);

-- Attendance table (Advanced Version)
CREATE TABLE IF NOT EXISTS attendance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    staff_id BIGINT NOT NULL,
    work_date DATE NOT NULL,
    check_in TIME,
    check_out TIME,
    total_hours DECIMAL(5,2) DEFAULT 0.00,
    status ENUM('PRESENT', 'ABSENT', 'LEAVE', 'HALF_DAY', 'LATE') DEFAULT 'PRESENT',
    notes TEXT,
    is_late BOOLEAN DEFAULT FALSE,
    overtime_hours DECIMAL(5,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (staff_id) REFERENCES staff(id) ON DELETE CASCADE
);

-- Payroll table
CREATE TABLE IF NOT EXISTS payroll (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    staff_id BIGINT NOT NULL,
    branch_id BIGINT NOT NULL,
    month_year VARCHAR(7) NOT NULL, -- Format: YYYY-MM
    base_salary DECIMAL(10,2) NOT NULL,
    commission_amount DECIMAL(10,2) DEFAULT 0.00,
    total_earnings DECIMAL(10,2) NOT NULL,
    deductions DECIMAL(10,2) DEFAULT 0.00,
    net_pay DECIMAL(10,2) NOT NULL,
    status ENUM('DRAFT', 'APPROVED', 'PAID') DEFAULT 'DRAFT',
    payment_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (staff_id) REFERENCES staff(id) ON DELETE CASCADE,
    FOREIGN KEY (branch_id) REFERENCES branch(branch_id) ON DELETE CASCADE,
    UNIQUE KEY unique_staff_month (staff_id, month_year)
);

-- Staff Commission Summary table
CREATE TABLE IF NOT EXISTS staff_commission_summary (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    staff_id BIGINT NOT NULL,
    branch_id BIGINT NOT NULL,
    month VARCHAR(7) NOT NULL, -- Format: YYYY-MM
    total_services INT DEFAULT 0,
    total_commission DECIMAL(10,2) DEFAULT 0.00,
    base_salary DECIMAL(10,2) DEFAULT 0.00,
    total_payout DECIMAL(10,2) DEFAULT 0.00,
    total_revenue DECIMAL(10,2) DEFAULT 0.00,
    average_rating DECIMAL(3,2) DEFAULT 0.00,
    is_generated BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (staff_id) REFERENCES staff(id) ON DELETE CASCADE,
    FOREIGN KEY (branch_id) REFERENCES branch(branch_id) ON DELETE CASCADE,
    UNIQUE KEY unique_staff_month_summary (staff_id, month)
);

-- Loyalty Benefits table
CREATE TABLE IF NOT EXISTS loyalty_benefits (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    membership_level ENUM('BRONZE', 'SILVER', 'GOLD', 'PLATINUM') NOT NULL,
    benefit_type ENUM('DISCOUNT', 'FREE_SERVICE', 'PRIORITY_BOOKING', 'BONUS_POINTS') NOT NULL,
    description TEXT,
    discount_percentage DECIMAL(5,2),
    discount_amount DECIMAL(10,2),
    free_service_id BIGINT,
    bonus_points_multiplier INT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (free_service_id) REFERENCES services(id) ON DELETE SET NULL
);

-- System Settings table
CREATE TABLE IF NOT EXISTS system_settings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    setting_key VARCHAR(100) UNIQUE NOT NULL,
    setting_value TEXT,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert default system settings
INSERT INTO system_settings (setting_key, setting_value, description) VALUES
('currency', 'LKR', 'Default currency'),
('tax_rate', '15.0', 'Default tax rate percentage'),
('service_charge', '5.0', 'Default service charge percentage'),
('loyalty_points_per_rupee', '1', 'Loyalty points earned per rupee spent'),
('appointment_reminder_hours', '24', 'Hours before appointment to send reminder'),
('low_stock_threshold', '10', 'Minimum stock level to trigger alert');

-- Branch Summary table for performance optimization
CREATE TABLE IF NOT EXISTS branch_summary (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    branch_id BIGINT NOT NULL,
    summary_date DATE NOT NULL,
    total_appointments INT DEFAULT 0,
    total_revenue DECIMAL(12,2) DEFAULT 0.00,
    total_customers INT DEFAULT 0,
    total_staff INT DEFAULT 0,
    average_rating DECIMAL(3,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (branch_id) REFERENCES branch(branch_id) ON DELETE CASCADE,
    UNIQUE KEY unique_branch_date (branch_id, summary_date)
);

-- Insert sample data for testing
INSERT INTO branch (branch_code, branch_name, address, city, postal_code, contact_number, email, working_hours, status) VALUES
('COL001', 'Main Branch', '123 Main Street, Colombo 01', 'Colombo', '00100', '+94-11-234-5678', 'main@salon.com', '{"open":"09:00","close":"19:00","days":["MON","TUE","WED","THU","FRI","SAT"]}', 'ACTIVE'),
('KAN001', 'Kandy Branch', '456 Galle Road, Kandy', 'Kandy', '20000', '+94-81-234-5679', 'kandy@salon.com', '{"open":"09:00","close":"18:00","days":["MON","TUE","WED","THU","FRI","SAT"]}', 'ACTIVE'),
('NEG001', 'Negombo Branch', '789 Negombo Road, Negombo', 'Negombo', '11500', '+94-31-234-5680', 'negombo@salon.com', '{"open":"08:00","close":"20:00","days":["MON","TUE","WED","THU","FRI","SAT","SUN"]}', 'ACTIVE');

-- Insert admin user
INSERT INTO users (username, email, password, role, branch_id) VALUES
('admin', 'admin@salon.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'ADMIN', NULL);

-- Insert sample staff users
INSERT INTO users (username, email, password, role, branch_id) VALUES
('harsha.perera', 'harsha@salon.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'BEAUTICIAN', 1),
('nadeesha.fernando', 'nadeesha@salon.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'BEAUTICIAN', 1),
('kasun.silva', 'kasun@salon.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'RECEPTIONIST', 1);

-- Insert sample staff
INSERT INTO staff (user_id, employee_code, first_name, last_name, phone, email, gender, designation, skill_set, join_date, salary_type, base_salary, commission_rate, status, branch_id) VALUES
(2, 'EMP001', 'Harsha', 'Perera', '+94-77-123-4567', 'harsha@salon.com', 'MALE', 'Senior Stylist', 'Haircut,Hair Color,Shave', '2023-01-15', 'HYBRID', 60000.00, 10.00, 'ACTIVE', 1),
(3, 'EMP002', 'Nadeesha', 'Fernando', '+94-77-234-5678', 'nadeesha@salon.com', 'FEMALE', 'Beautician', 'Facial,Pedicure,Manicure', '2023-02-01', 'COMMISSION', 0.00, 15.00, 'ACTIVE', 1),
(4, 'EMP003', 'Kasun', 'Silva', '+94-77-345-6789', 'kasun@salon.com', 'MALE', 'Receptionist', 'Customer Service,Appointment Booking', '2023-01-01', 'FIXED', 80000.00, 0.00, 'ACTIVE', 1);

-- Insert sample services
INSERT INTO services (name, description, price, duration_minutes, commission_rate, category) VALUES
('Hair Cut', 'Professional hair cutting service', 1500.00, 60, 15.00, 'Hair'),
('Hair Color', 'Hair coloring and highlighting', 3500.00, 120, 20.00, 'Hair'),
('Facial', 'Deep cleansing facial treatment', 2500.00, 90, 18.00, 'Skin'),
('Manicure', 'Nail care and polish', 1200.00, 45, 12.00, 'Nails'),
('Pedicure', 'Foot care and nail polish', 1500.00, 60, 12.00, 'Nails');

-- Insert sample suppliers
INSERT INTO supplier (name, contact_number, email, address, payment_terms) VALUES
('Beauty Supplies Ltd', '+94-11-123-4567', 'orders@beautysupplies.lk', '123 Industrial Zone, Colombo 15', 'Net 30'),
('Skin Care Co', '+94-11-234-5678', 'sales@skincare.lk', '456 Business Park, Colombo 03', 'Net 15'),
('Nail Art Supplies', '+94-11-345-6789', 'info@nailart.lk', '789 Commercial Street, Colombo 07', 'Cash on Delivery'),
('Hair Tools Inc', '+94-11-456-7890', 'contact@hairtools.lk', '321 Trade Center, Colombo 05', 'Net 30');

-- Insert sample products
INSERT INTO product (name, code, category, brand, uom, product_type, cost_price, selling_price, alert_quantity, tax_rate) VALUES
('L\'Oreal Shampoo', 'LOR-SH-1000', 'Hair Care', 'L\'Oreal', '1000 ml', 'CONSUMABLE', 2500.00, 0.00, 5.00, 15.00),
('L\'Oreal Conditioner', 'LOR-CN-1000', 'Hair Care', 'L\'Oreal', '1000 ml', 'CONSUMABLE', 2200.00, 0.00, 5.00, 15.00),
('Garnier Face Cream', 'GAR-FC-200', 'Skin Care', 'Garnier', '200 ml', 'CONSUMABLE', 800.00, 0.00, 10.00, 15.00),
('OPI Nail Polish', 'OPI-NP-15', 'Nail Care', 'OPI', '15 ml', 'RETAIL', 200.00, 400.00, 20.00, 15.00),
('Professional Hair Dryer', 'HAIR-DRY-001', 'Equipment', 'Philips', 'pcs', 'TOOL', 15000.00, 0.00, 1.00, 15.00),
('Wax Heater', 'WAX-HEAT-001', 'Equipment', 'Satin Smooth', 'pcs', 'TOOL', 3200.00, 0.00, 1.00, 15.00);

-- Insert sample inventory (stock for each branch)
INSERT INTO inventory (branch_id, product_id, quantity) VALUES
(1, 1, 8.00), (1, 2, 6.00), (1, 3, 12.00), (1, 4, 25.00), (1, 5, 2.00), (1, 6, 1.00),
(2, 1, 3.00), (2, 2, 4.00), (2, 3, 8.00), (2, 4, 15.00), (2, 5, 1.00), (2, 6, 1.00),
(3, 1, 5.00), (3, 2, 3.00), (3, 3, 6.00), (3, 4, 18.00), (3, 5, 1.00), (3, 6, 0.00);

-- Insert sample service product mappings
INSERT INTO service_product_mapping (service_id, product_id, default_quantity, is_required) VALUES
(1, 1, 20.00, TRUE),  -- Hair Cut uses Shampoo
(1, 2, 15.00, TRUE),  -- Hair Cut uses Conditioner
(2, 1, 30.00, TRUE),  -- Hair Color uses Shampoo
(2, 2, 25.00, TRUE),  -- Hair Color uses Conditioner
(3, 3, 10.00, TRUE),  -- Facial uses Face Cream
(4, 4, 1.00, TRUE),   -- Manicure uses Nail Polish
(5, 4, 1.00, TRUE);   -- Pedicure uses Nail Polish

-- Insert sample loyalty benefits
INSERT INTO loyalty_benefits (membership_level, benefit_type, description, discount_percentage, bonus_points_multiplier) VALUES
('BRONZE', 'BONUS_POINTS', 'Earn 1 point per rupee spent', NULL, 1),
('SILVER', 'DISCOUNT', '5% discount on all services', 5.00, 1),
('SILVER', 'BONUS_POINTS', 'Earn 1.2x points per rupee spent', NULL, 2),
('GOLD', 'DISCOUNT', '10% discount on all services', 10.00, 1),
('GOLD', 'FREE_SERVICE', 'Free manicure on every 5th visit', NULL, 1),
('GOLD', 'BONUS_POINTS', 'Earn 1.5x points per rupee spent', NULL, 3),
('PLATINUM', 'DISCOUNT', '15% discount on all services', 15.00, 1),
('PLATINUM', 'FREE_SERVICE', 'Free facial on every 3rd visit', NULL, 1),
('PLATINUM', 'PRIORITY_BOOKING', 'Priority booking for appointments', NULL, 1),
('PLATINUM', 'BONUS_POINTS', 'Earn 2x points per rupee spent', NULL, 4);


