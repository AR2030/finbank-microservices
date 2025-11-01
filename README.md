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
- [Observability and Monitoring](#observability-and-monitoring)
- [Security](#security)

## 🎯 Overview

FinBank is a microservices-based banking application that demonstrates enterprise-grade patterns including:

- **Service Discovery** with Netflix Eureka
- **Centralized Configuration** with Spring Cloud Config
- **API Gateway** pattern with Spring Cloud Gateway
- **Circuit Breaker** pattern with Resilience4J
- **Event-Driven Architecture** with RabbitMQ
- **Distributed Tracing** with OpenTelemetry and Tempo
- **Metrics Monitoring** with Prometheus and Grafana
- **Log Aggregation** with Loki and Alloy
- **OAuth2/OpenID Connect** authentication with Keycloak
- **Rate Limiting** with Redis
- **Containerization** with Docker
- **Full Observability Stack** with Grafana LGTM (Loki, Grafana, Tempo, Minio)

## 🏗️ Architecture

The system follows a microservices architecture with comprehensive observability and monitoring:

```
┌───────────────────────────────────────────────────────────────────────────┐
│                         OBSERVABILITY LAYER                               │
│                                                                           │
│  ┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐          │
│  │ Grafana  │◄───┤Prometheus│    │  Tempo   │    │   Loki   │          │
│  │  (3000)  │    │  (9090)  │    │ (3110)   │    │ (3100)   │          │
│  └────┬─────┘    └────┬─────┘    └────┬─────┘    └────┬─────┘          │
│       │               │ Metrics        │ Traces         │ Logs            │
│       │  Dashboards   │                │                │                 │
│       └───────────────┼────────────────┼────────────────┘                 │
│                       │                │                                   │
└───────────────────────┼────────────────┼───────────────────────────────────┘
                        │                │                ▲
                        │                │                │ (Alloy)
                        │                │                │ Log Collection
┌───────────────────────┼────────────────┼────────────────┴─────────────────┐
│                  SECURITY & GATEWAY LAYER                                 │
│                                                                           │
│  ┌─────────────┐                                                         │
│  │   Keycloak  │ (Authentication & Authorization - Port 7080)           │
│  └──────┬──────┘                                                         │
│         │                                                                 │
│  ┌──────▼────────────────────────────────────────────┐                  │
│  │           API Gateway (8072)                      │◄─ OpenTelemetry │
│  │  - Rate Limiting (Redis)                          │   Instrumentation│
│  │  - OAuth2 Resource Server                         │                  │
│  │  - Load Balancing                                 │                  │
│  │  - Request Routing                                │                  │
│  └──────┬────────────────────────────────────────────┘                  │
└─────────┼────────────────────────────────────────────────────────────────┘
          │
          │
┌─────────┼────────────────────────────────────────────────────────────────┐
│         │          INFRASTRUCTURE SERVICES LAYER                         │
│         │                                                                 │
│         │    ┌─────────────┐      ┌──────────────┐                      │
│         ├────► Eureka      │◄─────┤ Config       │◄─ OpenTelemetry     │
│         │    │ Server      │      │ Server       │   Instrumentation    │
│         │    │ (8070)      │      │ (8071)       │                      │
│         │    └─────────────┘      └──────┬───────┘                      │
│         │                                 │                               │
│         │                          ┌──────▼──────┐                       │
│         │                          │  RabbitMQ   │                       │
│         │                          │  (Messages) │                       │
│         │                          │   (5672)    │                       │
│         │                          └─────────────┘                       │
│         │          ┌──────────┐                                          │
│         │          │  Redis   │                                          │
│         │          │  (6379)  │                                          │
│         │          └──────────┘                                          │
└─────────┼──────────────────────────────────────────────────────────────┘
          │
          │
┌─────────┼────────────────────────────────────────────────────────────────┐
│         │            BUSINESS SERVICES LAYER                             │
│         │                                                                 │
│         ├────────┬──────────┬──────────┬────────────┐                   │
│         │        │          │          │            │                    │
│     ┌───▼───┐ ┌─▼────┐ ┌──▼─────┐ ┌──▼──────┐     │                   │
│     │Accounts│ │Loans │ │ Cards  │ │ Message │◄────┘ OpenTelemetry    │
│     │(8080)  │ │(8090)│ │ (9000) │ │ Service │   Auto-Instrumentation  │
│     └───┬────┘ └──┬───┘ └───┬────┘ └─────────┘   (Metrics/Traces/Logs) │
│         │         │          │                                           │
└─────────┼─────────┼──────────┼───────────────────────────────────────────┘
          │         │          │
          │         │          │
┌─────────┼─────────┼──────────┼───────────────────────────────────────────┐
│         │   DATA PERSISTENCE LAYER                                       │
│         │         │          │                                           │
│     ┌───▼────┐┌──▼─────┐┌───▼────┐                                     │
│     │ MySQL  ││ MySQL  ││ MySQL  │                                      │
│     │Accounts││ Loans  ││ Cards  │                                      │
│     │(3306)  ││(3307)  ││(3308)  │                                      │
│     └────────┘└────────┘└────────┘                                      │
│                                                                           │
│     ┌─────────────────────────────┐                                     │
│     │        Minio S3             │  (Loki Storage Backend)             │
│     │      (Object Storage)       │                                      │
│     └─────────────────────────────┘                                     │
└───────────────────────────────────────────────────────────────────────────┘

Legend:
  ─────►  HTTP/REST Communication
  ◄─────  Bi-directional Communication
  ◄─      Data Flow (Metrics, Traces, Logs)
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

### Observability & Monitoring
- **Prometheus** - Metrics collection and storage
- **Grafana** - Visualization and dashboards
- **Tempo** - Distributed tracing backend
- **Loki** - Log aggregation and querying
- **Alloy** - Log collection agent
- **OpenTelemetry** - Instrumentation for traces
- **Minio** - Object storage for Loki
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

#### Production Environment (with Full Observability Stack)
```bash
cd docker-compose/prod
docker-compose up -d

# The production environment includes:
# - All microservices
# - Complete observability stack (Grafana, Prometheus, Loki, Tempo)
# - All supporting infrastructure
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

#### Core Services
- **Eureka Dashboard**: http://localhost:8070
- **Config Server**: http://localhost:8071/actuator/health
- **Gateway Server**: http://localhost:8072/actuator/health
- **Keycloak Admin**: http://localhost:7080 (admin/admin)

#### Observability Tools (Production Environment Only)
- **Grafana Dashboard**: http://localhost:3000
- **Prometheus**: http://localhost:9090
- **Tempo**: http://localhost:3110
- **Loki Gateway**: http://localhost:3100

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

#### Core Infrastructure
- **3 MySQL databases** (one per microservice)
- **Keycloak** for authentication
- **Redis** for rate limiting
- **RabbitMQ** for messaging
- **Config Server**
- **Eureka Server**
- **Gateway Server**
- **3 Business Microservices** (Accounts, Loans, Cards)

#### Observability Stack (Production Environment)
- **Grafana** - Unified observability dashboard
- **Prometheus** - Metrics collection and storage
- **Tempo** - Distributed tracing backend
- **Loki** - Log aggregation (Read/Write/Backend components)
- **Alloy** - Log collection agent
- **Minio** - Object storage for Loki
- **NGINX Gateway** - Loki request routing

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

## 📊 Observability and Monitoring

The FinBank microservices platform includes a comprehensive observability stack based on the **Grafana LGTM** (Loki, Grafana, Tempo, Minio) stack, providing full visibility into metrics, logs, and traces.

### Observability Stack Components

#### 1. **Grafana** - Unified Observability Dashboard
- **Port**: 3000
- **URL**: http://localhost:3000
- **Purpose**: Unified visualization platform for metrics, logs, and traces
- **Features**:
  - Pre-configured dashboards for service monitoring
  - Query and visualize metrics from Prometheus
  - Search and analyze logs from Loki
  - Trace analysis from Tempo
  - Correlation between metrics, logs, and traces
- **Authentication**: Anonymous access enabled (Admin role)

#### 2. **Prometheus** - Metrics Collection & Storage
- **Port**: 9090
- **URL**: http://localhost:9090
- **Purpose**: Time-series database for metrics collection
- **Features**:
  - Scrapes metrics from all microservices via `/actuator/prometheus` endpoint
  - 5-second scrape interval for real-time monitoring
  - Query metrics using PromQL
  - Stores application and JVM metrics
- **Monitored Services**:
  - Accounts Service (8080)
  - Loans Service (8090)
  - Cards Service (9000)
  - Gateway Server (8072)
  - Eureka Server (8070)
  - Config Server (8071)

#### 3. **Tempo** - Distributed Tracing
- **Port**: 3110 (HTTP), 4318 (OTLP)
- **URL**: http://localhost:3110
- **Purpose**: Distributed tracing backend for request flow analysis
- **Features**:
  - Receives traces via OpenTelemetry Protocol (OTLP)
  - Traces requests across all microservices
  - Identifies performance bottlenecks
  - Service dependency mapping
  - Correlation with logs and metrics
- **Integration**: All services auto-instrumented with OpenTelemetry Java Agent

#### 4. **Loki** - Log Aggregation
- **Port**: 3100 (via NGINX Gateway)
- **Purpose**: Centralized log aggregation and querying
- **Architecture**:
  - **Read Component** (3101): Handles log queries
  - **Write Component** (3102): Ingests log data
  - **Backend Component**: Compaction and maintenance
  - **Gateway (NGINX)**: Routes read/write requests
- **Features**:
  - Label-based log indexing
  - LogQL query language
  - Efficient log storage with Minio backend
  - Multi-tenancy support
- **Storage**: Minio S3-compatible object storage

#### 5. **Alloy** - Log Collection Agent
- **Port**: 12345
- **Purpose**: Collects logs from Docker containers
- **Features**:
  - Monitors all Docker containers via Docker socket
  - Automatic log collection and forwarding to Loki
  - Container metadata enrichment
  - Real-time log streaming

#### 6. **Minio** - Object Storage
- **Port**: 9000 (internal)
- **Purpose**: S3-compatible storage backend for Loki
- **Features**:
  - Stores log data in `loki-data` bucket
  - Stores ruler data in `loki-ruler` bucket
  - High-performance object storage
- **Credentials**: loki/supersecret

### OpenTelemetry Integration

All microservices are automatically instrumented with **OpenTelemetry Java Agent** (v1.27.0) for distributed tracing:

- **Auto-instrumentation**: Captures HTTP requests, database queries, and inter-service calls
- **Trace Export**: Sends traces to Tempo via OTLP protocol (http://tempo:4318)
- **Service Names**: Each service has a unique identifier (OTEL_SERVICE_NAME)
- **Protocol**: HTTP/Protobuf for efficient trace transmission
- **Exporters**: Traces only (metrics and logs handled separately)

### Accessing Observability Tools

#### Production Environment (docker-compose/prod)

```bash
# Start all services including observability stack
cd docker-compose/prod
docker-compose up -d

# Access the observability tools
# Grafana Dashboard
http://localhost:3000

# Prometheus Metrics
http://localhost:9090

# Direct access to components (optional)
http://localhost:3110  # Tempo
http://localhost:3100  # Loki Gateway
```

#### Quick Start Guide

1. **View Service Metrics**:
   - Open Grafana at http://localhost:3000
   - Navigate to Explore → Prometheus
   - Query: `rate(http_server_requests_seconds_count[5m])`
   - View request rates, response times, and error rates

2. **Search Logs**:
   - Open Grafana at http://localhost:3000
   - Navigate to Explore → Loki
   - Query: `{container="accounts-ms"}`
   - Filter logs by service, time range, and log level

3. **Analyze Traces**:
   - Open Grafana at http://localhost:3000
   - Navigate to Explore → Tempo
   - View service map and trace dependencies
   - Click on a trace to see the complete request flow
   - Identify slow requests and bottlenecks

4. **Correlate Data**:
   - When viewing logs, click on trace IDs to jump to corresponding traces
   - From traces, view related logs and metrics
   - Get complete visibility into request lifecycle

### Actuator Endpoints

All services expose Spring Boot Actuator endpoints for health checks and metrics:

```bash
# Health check
GET http://localhost:{port}/actuator/health

# Detailed health information
GET http://localhost:{port}/actuator/health/liveness
GET http://localhost:{port}/actuator/health/readiness

# Prometheus metrics endpoint
GET http://localhost:{port}/actuator/prometheus

# Application info
GET http://localhost:{port}/actuator/info

# All available metrics
GET http://localhost:{port}/actuator/metrics

# Environment variables
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
- Grafana dashboards with Resilience4j metrics
- Health endpoint: `/actuator/health`

### Key Metrics to Monitor

#### Application Metrics
- **Request Rate**: `rate(http_server_requests_seconds_count[5m])`
- **Response Time**: `http_server_requests_seconds_sum / http_server_requests_seconds_count`
- **Error Rate**: `rate(http_server_requests_seconds_count{status=~"5.."}[5m])`
- **Circuit Breaker State**: `resilience4j_circuitbreaker_state`

#### JVM Metrics
- **Heap Memory**: `jvm_memory_used_bytes{area="heap"}`
- **GC Pause Time**: `jvm_gc_pause_seconds_sum`
- **Thread Count**: `jvm_threads_live_threads`
- **CPU Usage**: `process_cpu_usage`

#### Database Metrics
- **Connection Pool**: `hikaricp_connections_active`
- **Query Time**: `spring_data_repository_invocations_seconds`

### Log Levels and Format

- **Log Format**: Includes trace ID for correlation with distributed traces
- **Default Level**: INFO
- **Log Pattern**: `[${spring.application.name},${traceId},${spanId}]`

### Troubleshooting Tips

1. **Service Not Appearing in Prometheus**:
   - Check service health: `curl http://localhost:{port}/actuator/health`
   - Verify Prometheus scrape config in `prometheus.yml`
   - Check Prometheus targets: http://localhost:9090/targets

2. **No Traces in Tempo**:
   - Verify OpenTelemetry agent is loaded: Check service logs for "opentelemetry-javaagent"
   - Ensure OTEL_EXPORTER_OTLP_ENDPOINT is configured correctly
   - Check Tempo health: `curl http://localhost:3110/ready`

3. **Logs Not Showing in Loki**:
   - Verify Alloy is collecting logs: http://localhost:12345
   - Check Loki gateway: `curl http://localhost:3100`
   - Ensure containers are properly labeled

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
