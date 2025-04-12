# Healthcare System Microservices

A comprehensive healthcare system built with Spring Boot microservices architecture.

## Services

1. **Identity Service** (Port: 8081)
   - User authentication and authorization
   - JWT token management
   - User registration

2. **People Service** (Port: 8082)
   - Patient management
   - Doctor management
   - Administrator management

3. **Scheduling Service** (Port: 8083)
   - Appointment management
   - Referral management
   - Schedule coordination

4. **Facility Service** (Port: 8084)
   - Healthcare facility management
   - Doctor schedule management
   - Facility capacity tracking

## Prerequisites

- Docker and Docker Compose
- Java 17
- Maven

## Building and Running

1. Build all services:
   ```bash
   mvn clean package -DskipTests
   ```

2. Make the database initialization script executable:
   ```bash
   chmod +x init-multiple-databases.sh
   ```

3. Start all services using Docker Compose:
   ```bash
   docker-compose up -d
   ```

4. Check service status:
   ```bash
   docker-compose ps
   ```

## Service Dependencies

- Kafka (Event Bus)
- PostgreSQL (Database)
  - healthcare_identity
  - healthcare_people
  - healthcare_scheduling
  - healthcare_facility

## API Documentation

Each service exposes its API documentation at:
- Identity Service: http://localhost:8081/swagger-ui.html
- People Service: http://localhost:8082/swagger-ui.html
- Scheduling Service: http://localhost:8083/swagger-ui.html
- Facility Service: http://localhost:8084/swagger-ui.html

## Stopping the System

To stop all services:
```bash
docker-compose down
```

To stop and remove all data (including volumes):
```bash
docker-compose down -v