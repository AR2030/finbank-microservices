# FinBank – Microservices Banking System

A production-ready, cloud-native banking platform built with **Spring Boot** and **Spring Cloud**, demonstrating modern microservices architecture patterns.

## 📋 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Microservices](#microservices)
- [Technologies](#technologies)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Building the Project](#building-the-project)
- [Running the Services](#running-the-services)
- [API Documentation](#api-documentation)
- [Configuration](#configuration)
- [Docker Deployment](#docker-deployment)
- [Monitoring and Health Checks](#monitoring-and-health-checks)
- [Security](#security)
- [Future Enhancements](#future-enhancements)

## 🎯 Overview

FinBank is a microservices-based banking application that demonstrates enterprise-grade patterns including:

- **Service Discovery** with Netflix Eureka
- **Centralized Configuration** with Spring Cloud Config
- **API Gateway** pattern with Spring Cloud Gateway
- **Circuit Breaker** pattern with Resilience4J
- **Event-Driven Architecture** with RabbitMQ
- **Distributed Tracing** and monitoring
- **OAuth2/OpenID Connect** authentication with Keycloak
- **Rate Limiting** with Redis
- **Containerization** with Docker

## 🏗️ Architecture

The system follows a microservices architecture with the following components:

```
┌─────────────┐
│   Keycloak  │ (Authentication & Authorization)
└──────┬──────┘
       │
┌──────▼───────────────────────────────────────────┐
│           API Gateway (8072)                     │
│  - Rate Limiting (Redis)                        │
│  - OAuth2 Resource Server                       │
│  - Load Balancing                               │
└──────┬───────────────────────────────────────────┘
       │
       │    ┌─────────────┐      ┌──────────────┐
       ├────► Eureka      │◄─────┤ Config       │
       │    │ Server      │      │ Server       │
       │    │ (8070)      │      │ (8071)       │
       │    └─────────────┘      └──────┬───────┘
       │                                 │
       │                          ┌──────▼──────┐
       │                          │  RabbitMQ   │
       │                          │  (Messages) │
       │                          └─────────────┘
       │
       ├────────┬──────────┬──────────┐
       │        │          │          │
   ┌───▼───┐ ┌─▼────┐ ┌──▼─────┐ ┌──▼──────┐
   │Accounts│ │Loans │ │ Cards  │ │ Message │
   │(8080)  │ │(8090)│ │ (9000) │ │         │
   └───┬────┘ └──┬───┘ └───┬────┘ └─────────┘
       │         │          │
   ┌───▼────┐┌──▼─────┐┌───▼────┐
   │MySQL   ││MySQL   ││MySQL   │
   │(3306)  ││(3307)  ││(3308)  │
   └────────┘└────────┘└────────┘
```

## 🚀 Microservices

### Core Services

#### 1. **Accounts Service** (Port: 8080)
- Manages customer accounts
- Creates and updates account information
- Integrates with Loans and Cards services via OpenFeign
- Uses circuit breaker pattern for fault tolerance

#### 2. **Loans Service** (Port: 8090)
- Manages loan accounts
- Handles loan creation, updates, and retrieval
- Independent MySQL database

#### 3. **Cards Service** (Port: 9000)
- Manages credit/debit card accounts
- Handles card operations and information
- Independent MySQL database

#### 4. **Message Service**
- Event consumer for account-related events
- Processes asynchronous messages from RabbitMQ
- Supports event-driven workflows

### Infrastructure Services

#### 5. **Config Server** (Port: 8071)
- Centralized configuration management
- Stores configuration in Git repository
- Supports multiple environments (default, qa, prod)
- Real-time configuration refresh via RabbitMQ

#### 6. **Eureka Server** (Port: 8070)
- Service discovery and registration
- Health monitoring of registered services
- Load balancing support

#### 7. **Gateway Server** (Port: 8072)
- Single entry point for all client requests
- Request routing and load balancing
- Rate limiting with Redis
- OAuth2 authentication/authorization
- Cross-cutting concerns (logging, security)

## 🛠️ Technologies

### Core Frameworks
- **Spring Boot 3.5.6** - Application framework
- **Spring Cloud 2025.0.0** - Microservices infrastructure
- **Java 21** - Programming language

### Spring Cloud Components
- **Spring Cloud Config** - Centralized configuration
- **Spring Cloud Netflix Eureka** - Service discovery
- **Spring Cloud Gateway** - API gateway
- **Spring Cloud OpenFeign** - Declarative REST client
- **Spring Cloud Stream** - Event-driven messaging
- **Resilience4J** - Circuit breaker

### Data & Messaging
- **MySQL** - Relational database
- **RabbitMQ** - Message broker
- **Redis** - Caching and rate limiting
- **Spring Data JPA** - Data persistence

### Security
- **Keycloak** - Identity and access management
- **Spring Security OAuth2** - OAuth2 resource server

### DevOps & Containerization
- **Docker & Docker Compose** - Containerization
- **Jib** - Docker image building
- **Maven** - Build automation

### Monitoring & Documentation
- **Spring Boot Actuator** - Production-ready features
- **Swagger/OpenAPI 3** - API documentation

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

- **Java 21** or higher
- **Maven 3.8+** or use included Maven wrapper
- **Docker** and **Docker Compose** (for containerized deployment)
- **Git** (for cloning the repository)

Optional:
- **MySQL** (if running services locally without Docker)
- **RabbitMQ** (if running services locally without Docker)
- **Redis** (if running gateway locally without Docker)

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/AR2030/finbank-microservices.git
cd finbank-microservices
```

### 2. Build All Services

Using Maven from the root directory:

```bash
mvn clean install
```

Or build individual services:

```bash
cd accounts
mvn clean install

cd ../loans
mvn clean install

# ... and so on for other services
```

## 🔨 Building the Project

### Build All Services at Once

```bash
mvn clean package -DskipTests
```

### Build Docker Images

Each service uses Jib to build Docker images:

```bash
# Build all services
mvn clean compile jib:dockerBuild

# Or build individual service
cd accounts
mvn clean compile jib:dockerBuild
```

This creates Docker images with the naming pattern: `ar2030/{service-name}:v8`

## ▶️ Running the Services

### Option 1: Docker Compose (Recommended)

Docker Compose configurations are available for different environments:

#### Default Environment
```bash
cd docker-compose/default
docker-compose up -d
```

#### QA Environment
```bash
cd docker-compose/qa
docker-compose up -d
```

#### Production Environment
```bash
cd docker-compose/prod
docker-compose up -d
```

### Option 2: Run Services Locally

Start services in the following order:

1. **External Dependencies**
   ```bash
   # Start MySQL instances (3 separate databases)
   # Start RabbitMQ
   # Start Redis
   # Start Keycloak
   ```

2. **Config Server**
   ```bash
   cd configserver
   ./mvnw spring-boot:run
   ```

3. **Eureka Server**
   ```bash
   cd eurekaserver
   ./mvnw spring-boot:run
   ```

4. **Business Microservices** (can run in parallel)
   ```bash
   cd accounts
   ./mvnw spring-boot:run
   
   cd loans
   ./mvnw spring-boot:run
   
   cd cards
   ./mvnw spring-boot:run
   
   cd message
   ./mvnw spring-boot:run
   ```

5. **Gateway Server**
   ```bash
   cd gatewayserver
   ./mvnw spring-boot:run
   ```

### Service Startup Verification

After starting all services, verify they are running:

- **Eureka Dashboard**: http://localhost:8070
- **Config Server**: http://localhost:8071/actuator/health
- **Gateway Server**: http://localhost:8072/actuator/health
- **Keycloak Admin**: http://localhost:7080 (admin/admin)

## 📚 API Documentation

### Access API Documentation

Once the services are running, Swagger UI is available at:

- **Accounts API**: http://localhost:8072/accounts/swagger-ui.html
- **Loans API**: http://localhost:8072/loans/swagger-ui.html
- **Cards API**: http://localhost:8072/cards/swagger-ui.html

### Sample API Endpoints

All requests should go through the **Gateway Server** at `http://localhost:8072`

#### Accounts API
```bash
# Create Account
POST /accounts/api/create
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "mobileNumber": "1234567890"
}

# Get Account Details
GET /accounts/api/fetch?mobileNumber=1234567890

# Update Account
PUT /accounts/api/update

# Delete Account
DELETE /accounts/api/delete?mobileNumber=1234567890

# Get Customer Details (with Loans and Cards)
GET /accounts/api/fetchCustomerDetails?mobileNumber=1234567890
```

#### Loans API
```bash
# Create Loan
POST /loans/api/create

# Get Loan Details
GET /loans/api/fetch?mobileNumber=1234567890

# Update Loan
PUT /loans/api/update

# Delete Loan
DELETE /loans/api/delete?mobileNumber=1234567890
```

#### Cards API
```bash
# Create Card
POST /cards/api/create

# Get Card Details
GET /cards/api/fetch?mobileNumber=1234567890

# Update Card
PUT /cards/api/update

# Delete Card
DELETE /cards/api/delete?mobileNumber=1234567890
```

## ⚙️ Configuration

### Configuration Management

All microservices are configured through **Spring Cloud Config Server**, which pulls configuration from a Git repository.

### Environment Profiles

Three environments are supported:
- **default** - Development environment
- **qa** - QA/Testing environment
- **prod** - Production environment

Set the active profile via environment variable:
```bash
SPRING_PROFILES_ACTIVE=prod
```

### Key Configuration Properties

#### Service Ports
- Config Server: `8071`
- Eureka Server: `8070`
- Gateway Server: `8072`
- Accounts Service: `8080`
- Loans Service: `8090`
- Cards Service: `9000`

#### Database Configuration
Each service uses its own MySQL database:
- Accounts DB: `jdbc:mysql://localhost:3306/accountsdb`
- Loans DB: `jdbc:mysql://localhost:3307/loansdb`
- Cards DB: `jdbc:mysql://localhost:3308/cardsdb`

Default credentials: `root/root`

#### Message Broker
- RabbitMQ Host: `localhost:5672`
- Credentials: `guest/guest`

## 🐳 Docker Deployment

### Architecture Components

The Docker Compose setup includes:

- **3 MySQL databases** (one per microservice)
- **Keycloak** for authentication
- **Redis** for rate limiting
- **RabbitMQ** for messaging
- **Config Server**
- **Eureka Server**
- **Gateway Server**
- **3 Business Microservices** (Accounts, Loans, Cards)

### Quick Start

```bash
# Navigate to the environment folder
cd docker-compose/default

# Start all services
docker-compose up -d

# Check service status
docker-compose ps

# View logs
docker-compose logs -f

# Stop all services
docker-compose down

# Stop and remove volumes
docker-compose down -v
```

### Service Health Checks

All services include health checks configured in Docker Compose. Services will automatically wait for dependencies to be healthy before starting.

### Docker Network

All services communicate within a Docker bridge network named `finbank`.

## 📊 Monitoring and Health Checks

### Actuator Endpoints

All services expose Spring Boot Actuator endpoints:

```bash
# Health check
GET http://localhost:{port}/actuator/health

# Info
GET http://localhost:{port}/actuator/info

# Metrics
GET http://localhost:{port}/actuator/metrics

# Environment
GET http://localhost:{port}/actuator/env
```

### Service Discovery

View all registered services at Eureka Dashboard:
```
http://localhost:8070
```

### Circuit Breaker Monitoring

Circuit breaker states can be monitored through:
- Actuator metrics: `/actuator/metrics/resilience4j.circuitbreaker.*`
- Health endpoint: `/actuator/health`

## 🔐 Security

### OAuth2/OpenID Connect

The system uses **Keycloak** for authentication and authorization:

1. **Keycloak Admin Console**: http://localhost:7080
   - Username: `admin`
   - Password: `admin`

2. **Obtain Access Token**:
   ```bash
   curl -X POST 'http://localhost:7080/realms/master/protocol/openid-connect/token' \
     -H 'Content-Type: application/x-www-form-urlencoded' \
     -d 'grant_type=password' \
     -d 'client_id=admin-cli' \
     -d 'username=admin' \
     -d 'password=admin'
   ```

3. **Use Token in API Requests**:
   ```bash
   curl -X GET 'http://localhost:8072/accounts/api/fetch?mobileNumber=1234567890' \
     -H 'Authorization: Bearer {access_token}'
   ```

### Rate Limiting

The Gateway Server implements rate limiting using Redis:
- Default limits can be configured in the gateway configuration
- Prevents API abuse and ensures fair resource usage

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License.

## 👤 Author

**AR2030**

## 📞 Support

For issues and questions, please create an issue in the GitHub repository.

---

## 🚀 Future Enhancements

FinBank has a comprehensive roadmap for future features and improvements. Check out these documents:

### 📄 [Feature Suggestions](./FEATURE_SUGGESTIONS.md)
Comprehensive list of 25+ features organized by priority:
- **High Priority**: Transaction Management, Notifications, Monitoring, Testing
- **Medium Priority**: Customer Portal, Admin Dashboard, Fraud Detection, Payment Gateway
- **Advanced Features**: Mobile App, AI Chatbot, Blockchain, Open Banking
- **Developer Tools**: CI/CD, Testing Framework, API Documentation

### ⚡ [Quick Wins](./QUICK_WINS.md)
Features that can be implemented in 1-2 weeks with high impact:
1. API Versioning Strategy
2. Database Migration Tool (Flyway)
3. Standardized Error Handling
4. Request/Response Logging
5. Health Check Enhancements
6. CORS Configuration
7. Docker Compose Improvements
8. API Documentation Portal

### 🗺️ [Implementation Roadmap](./ROADMAP.md)
Detailed 2-year roadmap with:
- **Q1**: Production Readiness (Testing, Monitoring, CI/CD)
- **Q2**: Core Banking (Transactions, Notifications, Documents)
- **Q3**: Customer Experience (Portals, Dashboards, Analytics)
- **Q4**: Advanced Features (Payments, Fraud Detection, ML)
- **Year 2**: Mobile, AI, Open Banking, Multi-tenancy

### Want to Contribute?
We welcome contributions! Check the roadmap and pick a feature to implement. See [CONTRIBUTING.md](./CONTRIBUTING.md) for guidelines.
