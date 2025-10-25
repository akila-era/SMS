# Multi-Branch Salon Management System

A comprehensive salon management system built with Spring Boot backend and React.js frontend, designed to manage multiple salon branches with staff commission tracking, appointment scheduling, inventory management, and comprehensive reporting.

## 🚀 Features

- **Multi-Branch Management**: Manage multiple salon locations from a single dashboard
- **Staff & Commission Tracking**: Automated commission calculation and staff management
- **Appointment Scheduling**: Advanced booking system with calendar integration
- **Customer Management**: Customer profiles with loyalty points and visit history
- **Inventory Control**: Product tracking with auto-deduction and low-stock alerts
- **Billing & Payments**: Multi-payment support with invoice generation
- **Payroll Integration**: Automated salary + commission calculations
- **Reports & Analytics**: Comprehensive reporting with charts and export functionality
- **Role-Based Access**: Different access levels for Admin, Managers, Staff, etc.

## 🛠️ Tech Stack

### Backend
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security** with JWT authentication
- **Spring Data JPA** with Hibernate
- **MySQL 8.0** database
- **Maven** for dependency management

### Frontend
- **React 18**
- **React Router** for navigation
- **Tailwind CSS** for styling
- **Axios** for API calls
- **React Query** for state management
- **Chart.js** for analytics

### DevOps
- **Docker** & **Docker Compose** for containerization
- **MySQL** for database
- **Nginx** for reverse proxy (production)

## 📋 Prerequisites

- Java 17 or higher
- Node.js 18 or higher
- Docker and Docker Compose
- MySQL 8.0 (if running without Docker)

## 🚀 Quick Start

### Option 1: Using Docker Compose (Recommended)

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd salon-management
   ```

2. **Start all services**
   ```bash
   docker-compose up -d
   ```

3. **Access the application**
   - Frontend: http://localhost:3000
   - Backend API: http://localhost:8080/api
   - Database: localhost:3306

### Option 2: Manual Setup

1. **Start MySQL Database**
   ```bash
   # Using Docker
   docker run --name salon-mysql -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=salon_management -p 3306:3306 -d mysql:8.0
   
   # Or install MySQL locally and create database
   mysql -u root -p
   CREATE DATABASE salon_management;
   ```

2. **Start Backend**
   ```bash
   cd backend
   ./mvnw spring-boot:run
   ```

3. **Start Frontend**
   ```bash
   cd frontend
   npm install
   npm start
   ```

## 🔐 Default Login Credentials

- **Username**: admin
- **Password**: password
- **Role**: ADMIN

## 📁 Project Structure

```
salon-management/
├── backend/                 # Spring Boot backend
│   ├── src/main/java/com/hexalyte/salon/
│   │   ├── config/         # Configuration classes
│   │   ├── controller/     # REST controllers
│   │   ├── dto/           # Data Transfer Objects
│   │   ├── model/         # JPA entities
│   │   ├── repository/    # JPA repositories
│   │   ├── security/      # Security configuration
│   │   └── service/       # Business logic
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   └── schema.sql     # Database schema
│   └── pom.xml
├── frontend/               # React frontend
│   ├── src/
│   │   ├── components/    # Reusable components
│   │   ├── context/       # React context
│   │   ├── pages/         # Page components
│   │   ├── services/      # API services
│   │   └── App.js
│   ├── package.json
│   └── tailwind.config.js
├── docker-compose.yml     # Docker services
└── README.md
```

## 🗄️ Database Schema

The system includes the following main entities:

- **Branch**: Salon locations
- **Users**: Authentication and user management
- **Staff**: Staff information and branch assignment
- **Services**: Service catalog with pricing
- **Customers**: Customer profiles and loyalty
- **Appointments**: Booking system
- **Commission**: Staff commission tracking
- **Billing**: Payment and invoice management
- **Inventory**: Product and stock management
- **Payroll**: Salary and commission integration

## 🔧 Configuration

### Backend Configuration
Edit `backend/src/main/resources/application.properties`:

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/salon_management
spring.datasource.username=root
spring.datasource.password=password

# JWT
jwt.secret=your-secret-key
jwt.expiration=86400000

# CORS
cors.allowed-origins=http://localhost:3000
```

### Frontend Configuration
Edit `frontend/src/services/api.js`:

```javascript
const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';
```

## 📊 API Documentation

Once the backend is running, access the API documentation at:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## 🧪 Testing

### Backend Tests
```bash
cd backend
./mvnw test
```

### Frontend Tests
```bash
cd frontend
npm test
```

## 🚀 Deployment

### Production Build

1. **Build Backend**
   ```bash
   cd backend
   ./mvnw clean package -Pproduction
   ```

2. **Build Frontend**
   ```bash
   cd frontend
   npm run build
   ```

3. **Docker Production**
   ```bash
   docker-compose -f docker-compose.prod.yml up -d
   ```

## 📈 Development Roadmap

The system is being developed in phases:

- ✅ **Phase 1**: Foundation & Project Setup
- ✅ **Phase 2**: Authentication & User Management
- 🔄 **Phase 3**: Branch Management
- ⏳ **Phase 4**: Staff Management
- ⏳ **Phase 5**: Service Management
- ⏳ **Phase 6**: Customer Management
- ⏳ **Phase 7**: Appointment Scheduling
- ⏳ **Phase 8**: Commission Management
- ⏳ **Phase 9**: Inventory Management
- ⏳ **Phase 10**: Billing & Payments
- ⏳ **Phase 11**: Payroll Module
- ⏳ **Phase 12**: Reports & Analytics
- ⏳ **Phase 13**: Notifications
- ⏳ **Phase 14**: Settings & Configuration
- ⏳ **Phase 15**: Testing & QA
- ⏳ **Phase 16**: Documentation & Deployment

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Contact: support@hexalyte.com

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- React team for the amazing frontend library
- Tailwind CSS for the utility-first CSS framework
- All contributors and testers


