# Document Management System - Backend

## Overview

This is the backend component of a Document Management System project. Built with Spring Boot, it provides a robust API for managing documents, workspaces, and user authentication.

## Features

- User authentication and authorization
- Document upload, download, and management
- Workspace creation and management
- Search functionality for documents
- Role-based access control (User and Admin roles)
- File metadata management
- RESTful API endpoints

## Technologies Used

- Java 11
- Spring Boot 2.7.18
- Spring Security with JWT authentication
- MongoDB for document storage
- Maven for dependency management

## Getting Started

### Prerequisites

- Java JDK 11 or later
- MongoDB 4.4 or later
- Maven 3.6 or later

### Installation

1. Clone the repository:
   ```
   git clone https://github.com/Nasser3003/atos-dms.git
   ```

2. Navigate to the project directory:
   ```
   cd dms-backend
   ```

3. Install dependencies:
   ```
   mvn install
   ```

4. Configure MongoDB connection in `src/main/resources/application.yml`

5. Run the application:
   ```
   mvn spring-boot:run
   ```

The server will start on `http://localhost:3001`.

## API Endpoints

- `/auth/*` - Authentication endpoints
- `/user/document/*` - User document management
- `/admin/document/*` - Admin document management
- `/user/workspace/*` - User workspace management
- `/admin/workspace/*` - Admin workspace management
- `/user/documents/search` - Document search for users
- `/admin/documents/search` - Document search for admins

For a complete list of endpoints and their usage, please refer to the source code or create API documentation.

## Configuration

Key configuration files:

- `application.yml`: Main configuration file
- `SecurityConfig.java`: Security and CORS configuration
- `JwtConfig.java`: JWT configuration
