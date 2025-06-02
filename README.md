# Boost Voucher Platform

## Overview
A modern and robust voucher management system built with Spring Boot. 
This platform allows for the creation, distribution, and management of special offers and their corresponding voucher codes.

## Technology Stack
- Java 17
- Spring Boot 3.5.0
- Spring Data JPA
- Spring MVC
- PostgreSQL
- Lombok
- Swagger/OpenAPI Documentation
- Maven (Multi-module project)

## Project Structure
This project follows a modular architecture:

- **app-voucher**: Voucher application module
- **module-voucher**: Core voucher functionality module
- **app-recipient**: Recipient application module
- **module-recipient**: Core recipient functionality module
- **module-core**: Common utilities and base components
- **db-migration**: Database migration scripts

## Key Features

### Special Offers Management
- Create special offers with discount percentages
- Retrieve, update and delete offers
- Prevent duplicate offer names

### Recipient Management
- Create and manage recipient profiles with name and email
- Validate unique email addresses
- Retrieve recipients by email address
- Update and delete recipient information

### Voucher Code System
- Secure voucher code generation with formatting (XXXX-XXXX-XXXX)
- Track voucher code usage and expiration
- Associate vouchers with recipients and special offers
- Validation logic for voucher redemption

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.8+ 
- PostgreSQL

### Setup and Installation
1. Clone the repository
2. Start PostgreSQL using Docker Compose:
   ```bash
   cd infra/db
   docker-compose up -d
   ```
3. Run database migrations:
   ```bash
   cd db-migration
   mvn spring-boot:run
   ```
4. Build the project: 
   ```bash
   mvn clean install
   ```
5. Start the applications:
   - Run Voucher application:
     ```bash
     cd app-voucher
     mvn spring-boot:run
     ```
   - Run Recipient application in a new terminal:
     ```bash
     cd app-recipient
     mvn spring-boot:run
     ```
6. Access the Swagger UI documentation:
   - Voucher API: http://localhost:8080/api/v1/voucher/swagger-ui/index.html
   - Recipient API: http://localhost:8081/api/v1/recipient/swagger-ui/index.html

### Configuration
Key configuration options include:
- `voucher.code.length`: Defines the length of generated voucher codes (default: 12)
- `voucher.code.characters`: Character set for code generation

## Error Handling
The system includes comprehensive error handling with descriptive error messages and appropriate HTTP status codes for various scenarios such as:
- Duplicate offer names
- Invalid offer IDs
- Expired or used vouchers
- Duplicate email addresses
- Invalid recipient data

## Security Considerations
- Secure random code generation
- Input validation using Jakarta validation annotations

## Development

### Building
```bash
mvn clean install
```

### Running Tests
```bash
mvn test
```