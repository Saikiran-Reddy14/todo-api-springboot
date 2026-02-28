# Todo API - Spring Boot

A secure RESTful API for task management built with Spring Boot, featuring JWT authentication, PostgreSQL persistence, and comprehensive CRUD operations.

## Features

- 🔐 JWT-based authentication (Access & Refresh tokens)
- ✅ Complete CRUD operations for todos
- 📄 Pagination and sorting support
- 🗄️ PostgreSQL database integration
- 🏗️ Clean layered architecture (Controller-Service-Repository)
- ⚠️ Global exception handling
- 🔒 User-specific data isolation
- 🔑 BCrypt password encryption

## Tech Stack

- **Java 25**
- **Spring Boot 4.0.3**
- **Spring Security**
- **Spring Data JPA**
- **PostgreSQL**
- **JWT (jjwt 0.13.0)**
- **Lombok**
- **Maven**

## Prerequisites

- JDK 25 or higher
- PostgreSQL 12+
- Maven 3.6+

## Setup Instructions

### 1. Clone the repository
```bash
git clone https://github.com/Saikiran-Reddy14/todo-api-springboot.git
cd todo-api-springboot
```

### 2. Configure PostgreSQL
Create a database:
```sql
CREATE DATABASE todos_db;
```

### 3. Configure application properties
Copy the example configuration:
```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

Update `application.properties` with your database credentials:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/todos_db
spring.datasource.username=your_username
spring.datasource.password=your_password
jwt.secret=your_secret_key_minimum_256_bits
```

### 4. Build and run
```bash
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8000`

## API Endpoints

### Authentication

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login user |
| POST | `/api/auth/refresh` | Refresh access token |
| POST | `/api/auth/logout` | Logout user |

### Todos

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/todos` | Create a new todo |
| GET | `/api/todos` | Get all todos (paginated) |
| GET | `/api/todos/{id}` | Get todo by ID |
| PATCH | `/api/todos/{id}` | Update todo |
| DELETE | `/api/todos/{id}` | Delete todo |

### Query Parameters for GET /api/todos

- `pageNumber` - Page number (default: 0)
- `pageSize` - Items per page (default: 5)
- `sortBy` - Sort field: id, title, completed (default: id)
- `sortOrder` - Sort direction: asc, desc (default: desc)

## Request/Response Examples

### Register User
```json
POST /api/auth/register
{
  "username": "john_doe",
  "password": "SecurePass123"
}
```

### Login
```json
POST /api/auth/login
{
  "username": "john_doe",
  "password": "SecurePass123"
}

Response:
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### Create Todo
```json
POST /api/todos
Authorization: Bearer <access_token>

{
  "title": "Complete project documentation",
  "description": "Write comprehensive README and API docs",
  "completed": false
}
```

### Get Todos (Paginated)
```
GET /api/todos?pageNumber=0&pageSize=10&sortBy=title&sortOrder=asc
Authorization: Bearer <access_token>
```

## Project Structure

```
src/main/java/com/example/todos/
├── config/          # Security and JWT configuration
├── controller/      # REST controllers
├── dto/             # Data Transfer Objects
├── entity/          # JPA entities
├── exception/       # Custom exceptions and handlers
├── repo/            # JPA repositories
├── service/         # Business logic
└── utils/           # Utility classes (JWT, UserDetails)
```

## Security

- Passwords are encrypted using BCrypt
- JWT tokens for stateless authentication
- Access tokens expire in 15 minutes
- Refresh tokens expire in 3 days
- User-specific data isolation (users can only access their own todos)

## License

This project is open source and available under the MIT License.

## Author

**Saikiran Reddy**
- GitHub: [@Saikiran-Reddy14](https://github.com/Saikiran-Reddy14)
