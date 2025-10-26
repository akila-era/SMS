# Customer Management System

## Overview
The Customer Management system provides comprehensive functionality for managing salon customers, their preferences, appointment history, and loyalty benefits. This system is designed to enhance customer experience and build long-term relationships.

## Key Features

### 1. Customer Profile Management
- **Add/Edit Customer Profiles**: Complete customer information management
- **Search & Filter**: Advanced search by name, phone, or membership level
- **Customer Details**: Personal information, contact details, and membership status

### 2. Appointment History & Billing
- **Complete History View**: View all past appointments for each customer
- **Date Range Filtering**: Filter history by specific date ranges
- **Billing Information**: Track total spent and visit counts
- **Service Details**: View services provided in each appointment

### 3. Customer Preferences
- **Preferred Staff**: Set preferred beautician/stylist
- **Preferred Branch**: Set preferred salon location
- **Time Preferences**: Morning, afternoon, or evening preferences
- **Communication Settings**: SMS, email, or phone call preferences
- **Marketing Preferences**: Opt-in/out for promotions and reminders
- **Notes**: Additional customer-specific notes

### 4. Loyalty Points & Membership Tiers
- **Four-Tier System**: Bronze, Silver, Gold, and Platinum levels
- **Automatic Upgrades**: Points-based membership level progression
- **Points Tracking**: Real-time loyalty points balance
- **Visit Statistics**: Track total visits and spending

### 5. Loyalty Benefits Management
- **Benefit Types**: Discounts, free services, priority booking, bonus points
- **Tier-Specific Benefits**: Different benefits for each membership level
- **Flexible Configuration**: Percentage discounts, fixed amounts, or multipliers
- **Active/Inactive Status**: Enable or disable benefits as needed

## Database Schema

### Customers Table
```sql
CREATE TABLE customers (
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
    FOREIGN KEY (preferred_branch_id) REFERENCES branch(id) ON DELETE SET NULL
);
```

### Loyalty Benefits Table
```sql
CREATE TABLE loyalty_benefits (
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
```

## API Endpoints

### Customer Management
- `GET /api/customers` - Get all customers
- `GET /api/customers/search?q={query}` - Search customers
- `GET /api/customers/membership/{level}` - Get customers by membership level
- `GET /api/customers/top` - Get top customers
- `GET /api/customers/{id}` - Get customer by ID
- `GET /api/customers/phone/{phone}` - Get customer by phone
- `POST /api/customers` - Create new customer
- `PUT /api/customers/{id}` - Update customer
- `DELETE /api/customers/{id}` - Delete customer
- `PATCH /api/customers/{id}/loyalty-points?points={points}` - Update loyalty points

### Customer History
- `GET /api/customers/{id}/history` - Get customer appointment history
- `GET /api/customers/{id}/history/range?startDate={date}&endDate={date}` - Get history by date range

### Customer Preferences
- `GET /api/customers/{id}/preferences` - Get customer preferences
- `PUT /api/customers/{id}/preferences` - Update customer preferences

### Loyalty Benefits
- `GET /api/customers/loyalty-benefits` - Get all loyalty benefits
- `GET /api/customers/loyalty-benefits/{level}` - Get benefits by membership level
- `POST /api/customers/loyalty-benefits` - Create new loyalty benefit
- `PUT /api/customers/loyalty-benefits/{id}` - Update loyalty benefit
- `DELETE /api/customers/loyalty-benefits/{id}` - Delete loyalty benefit

## Frontend Features

### Customer Management Dashboard
- **Customer List**: Sortable table with search and filter capabilities
- **Quick Actions**: Edit, view history, manage preferences, delete
- **Membership Badges**: Color-coded membership level indicators
- **Statistics Display**: Visit count, total spent, loyalty points

### Customer Forms
- **Add Customer**: Complete form with validation
- **Edit Customer**: Pre-populated form for updates
- **Validation**: Required fields, email format, phone format

### History Modal
- **Appointment Timeline**: Chronological list of appointments
- **Service Details**: Services provided in each appointment
- **Status Indicators**: Visual status badges (completed, cancelled, etc.)
- **Financial Summary**: Total amounts and payment status

### Preferences Modal
- **Staff Selection**: Dropdown with available staff members
- **Branch Selection**: Dropdown with available branches
- **Time Slot Preferences**: Morning, afternoon, evening options
- **Communication Settings**: SMS, email, phone preferences
- **Marketing Preferences**: Checkboxes for promotions and reminders
- **Notes Section**: Free-text area for additional information

### Loyalty Benefits Management
- **Benefits Table**: Complete list of all loyalty benefits
- **Add New Benefit**: Form to create new benefits
- **Tier Organization**: Benefits grouped by membership level
- **Status Management**: Enable/disable benefits
- **Value Display**: Show discount percentages, amounts, or multipliers

## Membership Tiers & Benefits

### Bronze Level (0-199 points)
- Basic loyalty points (1 point per rupee spent)
- Standard service rates

### Silver Level (200-499 points)
- 5% discount on all services
- 1.2x points multiplier
- Enhanced customer service

### Gold Level (500-999 points)
- 10% discount on all services
- Free manicure on every 5th visit
- 1.5x points multiplier
- Priority booking

### Platinum Level (1000+ points)
- 15% discount on all services
- Free facial on every 3rd visit
- Priority booking for appointments
- 2x points multiplier
- VIP treatment

## Usage Examples

### Adding a New Customer
1. Click "Add Customer" button
2. Fill in required information (name, phone)
3. Add optional details (email, address, date of birth)
4. Select initial membership level
5. Save customer profile

### Managing Customer Preferences
1. Click "Preferences" for any customer
2. Select preferred staff member
3. Choose preferred branch location
4. Set time slot preference
5. Configure communication preferences
6. Add any special notes
7. Save preferences

### Viewing Customer History
1. Click "History" for any customer
2. View chronological appointment list
3. See services provided and amounts paid
4. Filter by date range if needed

### Managing Loyalty Benefits
1. Click "Manage Loyalty Benefits"
2. View current benefits by tier
3. Add new benefits using the form
4. Configure discount percentages or amounts
5. Set bonus point multipliers
6. Enable/disable benefits as needed

## Security & Permissions

### Role-Based Access
- **Admin**: Full access to all customer management features
- **Branch Manager**: Full access within their branch
- **Receptionist**: Read/write access to customer profiles and preferences
- **Beautician**: Read-only access to customer information

### Data Validation
- Phone number format validation
- Email format validation
- Required field validation
- Unique phone number constraint

## Integration Points

### Appointment System
- Customer selection during appointment booking
- Automatic loyalty point calculation
- Visit count updates
- Spending total updates

### Billing System
- Loyalty discount application
- Points redemption
- Payment history tracking

### Communication System
- SMS/email preferences for reminders
- Marketing communication opt-in/out
- Appointment confirmation preferences

## Future Enhancements

### Planned Features
- **Customer Analytics**: Detailed spending and visit analytics
- **Automated Marketing**: Targeted promotions based on preferences
- **Mobile App**: Customer-facing mobile application
- **Referral System**: Customer referral tracking and rewards
- **Birthday Promotions**: Automatic birthday offers
- **Service Recommendations**: AI-powered service suggestions
- **Customer Feedback**: Rating and review system
- **Loyalty Program Expansion**: More benefit types and tiers

### Technical Improvements
- **Caching**: Redis caching for frequently accessed data
- **Search Optimization**: Elasticsearch for advanced search
- **API Rate Limiting**: Protect against abuse
- **Audit Logging**: Track all customer data changes
- **Data Export**: Customer data export functionality
- **Backup & Recovery**: Automated data backup

## Troubleshooting

### Common Issues
1. **Phone Number Already Exists**: Ensure unique phone numbers
2. **Invalid Email Format**: Check email validation
3. **Missing Required Fields**: Complete all mandatory fields
4. **Permission Denied**: Check user role permissions

### Error Handling
- Comprehensive error messages
- Validation feedback
- Graceful failure handling
- User-friendly error displays

## Support

For technical support or feature requests, please contact the development team or create an issue in the project repository.

---

*This Customer Management system is part of the comprehensive Salon Management System and integrates seamlessly with other modules for a complete business solution.*
