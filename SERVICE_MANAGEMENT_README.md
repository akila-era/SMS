# Service Management Module - Advanced Version

## Overview

The Service Management module provides comprehensive functionality for managing salon services, including advanced features like branch-specific pricing, service packages, promotions, and analytics. This module supports detailed service configuration, commission management, and business intelligence reporting.

## Features

### 1. Service Definition
- **Service Creation**: Add, edit, or deactivate individual services
- **Advanced Configuration**: 
  - Service name, category, and description
  - Standard duration with preparation and cleanup buffers
  - Base price and commission setup
  - Required skills and resource requirements
  - Taxable flag and default products
  - Commission type (percentage or fixed amount)

### 2. Branch-Specific Configuration
- **Branch Override Pricing**: Different prices for each branch
- **Branch-Specific Settings**:
  - Custom duration per branch
  - Branch-specific commission rates
  - Enable/disable services per branch
  - Custom skill requirements per branch
  - Resource requirements per branch

### 3. Service Packages & Bundles
- **Package Creation**: Combine multiple services into packages
- **Package Features**:
  - Fixed or dynamic pricing with discounts
  - Auto-calculated total duration
  - Split session capability
  - Validity period management
  - Optional service items

### 4. Promotions & Seasonal Offers
- **Promotion Management**:
  - Time-limited discounts
  - Service-specific or general promotions
  - Usage limits and minimum amounts
  - Branch-specific applicability
  - Automatic discount application

### 5. Product Usage Integration
- **Inventory Integration**:
  - Link services to required products
  - Auto-deduct product quantities
  - Cost calculation per service
  - Product usage tracking

### 6. Analytics & Reporting
- **Service Analytics**:
  - Service popularity reports
  - Revenue and profitability analysis
  - Average duration vs actual completion
  - Commission tracking
  - Customer satisfaction metrics

## API Endpoints

### Service Management
- `GET /api/services` - Get all services
- `GET /api/services/active` - Get active services
- `GET /api/services/categories` - Get service categories
- `GET /api/services/category/{category}` - Get services by category
- `GET /api/services/search` - Search services
- `GET /api/services/{id}` - Get service by ID
- `POST /api/services` - Create service
- `PUT /api/services/{id}` - Update service
- `DELETE /api/services/{id}` - Delete service
- `PATCH /api/services/{id}/toggle-status` - Toggle service status

### Branch Configuration
- `POST /api/services/{serviceId}/branch-configurations` - Create branch configuration
- `PUT /api/services/branch-configurations/{configId}` - Update branch configuration
- `GET /api/services/{serviceId}/branch-configurations` - Get branch configurations
- `GET /api/services/{serviceId}/branch-configurations/{branchId}` - Get specific branch configuration

### Service Packages
- `POST /api/services/packages` - Create service package
- `PUT /api/services/packages/{packageId}` - Update service package
- `GET /api/services/packages` - Get all packages
- `GET /api/services/packages/active` - Get active packages
- `GET /api/services/packages/{packageId}` - Get package by ID

### Promotions
- `POST /api/services/promotions` - Create promotion
- `GET /api/services/promotions/active` - Get active promotions
- `GET /api/services/{serviceId}/promotions` - Get promotions for service

### Product Usage
- `POST /api/services/{serviceId}/product-usages` - Add product usage
- `GET /api/services/{serviceId}/product-usages` - Get product usages for service

### Analytics
- `GET /api/services/{serviceId}/analytics` - Get service analytics
- `GET /api/services/analytics/most-popular` - Get most popular services
- `GET /api/services/analytics/most-profitable` - Get most profitable services

## Data Models

### Service Model
```java
@Entity
public class Service {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer durationMinutes;
    private BigDecimal commissionRate;
    private String category;
    private Boolean isActive;
    private Integer preparationBufferMinutes;
    private Integer cleanupBufferMinutes;
    private String defaultProducts;
    private Boolean isTaxable;
    private String requiredSkills;
    private String resourceRequirements;
    private CommissionType commissionType;
    private BigDecimal fixedCommissionAmount;
    // ... relationships and helper methods
}
```

### Service Branch Configuration
```java
@Entity
public class ServiceBranchConfiguration {
    private Long id;
    private Service service;
    private Branch branch;
    private BigDecimal price;
    private Integer durationMinutes;
    private BigDecimal commissionRate;
    private CommissionType commissionType;
    private BigDecimal fixedCommissionAmount;
    private Boolean isEnabled;
    private String requiredSkills;
    private String resourceRequirements;
    // ... relationships and helper methods
}
```

### Service Package
```java
@Entity
public class ServicePackage {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal discountPercentage;
    private Boolean isActive;
    private Integer totalDurationMinutes;
    private Boolean canSplitSessions;
    private Integer maxValidityDays;
    // ... relationships and helper methods
}
```

### Service Promotion
```java
@Entity
public class ServicePromotion {
    private Long id;
    private Service service;
    private ServicePackage servicePackage;
    private String name;
    private String description;
    private BigDecimal discountPercentage;
    private BigDecimal discountAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isActive;
    private Integer maxUses;
    private Integer usedCount;
    private BigDecimal minServiceAmount;
    private String applicableBranches;
    // ... relationships and helper methods
}
```

## Frontend Features

### Service Management Interface
- **Tabbed Interface**: Services, Packages, Promotions, Analytics
- **Service Cards**: Visual representation with key information
- **Modal Forms**: Create/edit services, packages, and promotions
- **Real-time Updates**: Dynamic data loading and updates
- **Search and Filter**: Find services by category or name

### Service Form Features
- **Basic Information**: Name, category, description
- **Pricing**: Base price, commission setup
- **Duration**: Service duration with buffers
- **Skills & Resources**: Required skills and equipment
- **Advanced Options**: Taxable flag, product requirements

### Package Management
- **Package Creation**: Combine multiple services
- **Pricing Options**: Fixed price or discount-based
- **Session Management**: Split session capability
- **Validity Control**: Time-limited packages

### Promotion Management
- **Promotion Setup**: Time-limited offers
- **Discount Types**: Percentage or fixed amount
- **Usage Limits**: Maximum uses and minimum amounts
- **Branch Targeting**: Specific branch applicability

### Analytics Dashboard
- **Popular Services**: Most booked services
- **Revenue Analysis**: Service profitability
- **Performance Metrics**: Duration and rating analysis
- **Trend Analysis**: Historical data visualization

## Usage Examples

### Creating a Service
```javascript
const serviceData = {
  name: "Haircut",
  category: "Hair",
  description: "Professional haircut service",
  price: 2000,
  durationMinutes: 45,
  preparationBufferMinutes: 5,
  cleanupBufferMinutes: 10,
  commissionRate: 10,
  commissionType: "PERCENTAGE",
  requiredSkills: "Hair Styling",
  resourceRequirements: "Chair, Mirror",
  isTaxable: true
};

await api.post('/services', serviceData);
```

### Creating a Service Package
```javascript
const packageData = {
  name: "Bridal Package",
  description: "Complete bridal makeover package",
  price: 9500,
  discountPercentage: 10,
  canSplitSessions: true,
  maxValidityDays: 30,
  isActive: true
};

await api.post('/services/packages', packageData);
```

### Creating a Promotion
```javascript
const promotionData = {
  name: "October Facial Special",
  description: "20% off all facial services",
  serviceId: 5,
  discountPercentage: 20,
  startDate: "2024-10-01",
  endDate: "2024-10-31",
  maxUses: 100,
  minServiceAmount: 1000,
  applicableBranches: "1,2,3"
};

await api.post('/services/promotions', promotionData);
```

## Business Logic

### Commission Calculation
- **Percentage-based**: `commission = servicePrice * commissionRate / 100`
- **Fixed Amount**: `commission = fixedCommissionAmount`
- **Branch Override**: Uses branch-specific commission settings if available

### Package Pricing
- **Original Price**: `originalPrice = packagePrice / (1 - discountPercentage / 100)`
- **Discount Amount**: `discountAmount = originalPrice - packagePrice`
- **Total Duration**: Sum of all included service durations

### Promotion Validation
- **Date Check**: Promotion must be within start and end dates
- **Usage Check**: Must not exceed maximum uses
- **Amount Check**: Service amount must meet minimum requirement
- **Branch Check**: Must be applicable to current branch

## Security

### Role-based Access
- **Admin**: Full access to all features
- **Branch Manager**: Limited to branch-specific configurations
- **Receptionist**: Read-only access to service information

### Data Validation
- **Input Validation**: All form inputs are validated
- **Business Rules**: Commission rates, pricing, and duration constraints
- **Data Integrity**: Foreign key relationships and data consistency

## Performance Considerations

### Database Optimization
- **Indexing**: Proper indexes on frequently queried fields
- **Lazy Loading**: Relationships loaded only when needed
- **Query Optimization**: Efficient queries for analytics

### Caching Strategy
- **Service Cache**: Frequently accessed services cached
- **Analytics Cache**: Pre-calculated analytics data
- **Configuration Cache**: Branch-specific configurations cached

## Future Enhancements

### Planned Features
- **Dynamic Pricing**: Time-based pricing adjustments
- **Service Dependencies**: Prerequisite services
- **Advanced Analytics**: Machine learning insights
- **Mobile App**: Native mobile interface
- **Integration APIs**: Third-party system integration

### Scalability
- **Microservices**: Service management as separate service
- **Event Sourcing**: Event-driven architecture
- **Real-time Updates**: WebSocket-based live updates
- **Multi-tenant**: Support for multiple salon chains

## Troubleshooting

### Common Issues
1. **Service Not Found**: Check service ID and active status
2. **Branch Configuration Missing**: Verify branch-specific settings
3. **Promotion Not Applied**: Check date range and usage limits
4. **Analytics Not Loading**: Verify date range and permissions

### Debug Mode
- Enable debug logging for service operations
- Check database constraints and relationships
- Verify API endpoint accessibility
- Monitor performance metrics

## Support

For technical support or feature requests, please contact the development team or create an issue in the project repository.

---

*This documentation covers the advanced Service Management module implementation. For basic service management features, refer to the standard Service Management documentation.*
