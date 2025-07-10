# spring-gateway

# Rate-Limited API Gateway using Spring Boot, JWT, and Redis

# Project Overview
This project implements a secure API Gateway using Spring Boot. It enforces rate-limiting per authenticated user using a sliding window
algorithm powered by Redis and supports secure access using JWT tokens. It provides reusable and extensible filters for JWT authentication and rate limiting, making it suitable for any microservices-based architecture.


## A lightweight, secure, and scalable API Gateway middleware that supports:

- JWT-based Authentication
- Redis-backed Rate Limiting
- Spring Security Integration
- Per-user Sliding Window Rate Limiting (100 requests/minute)


##  Features

-  JWT Authentication Filter
-  Rate Limiting Filter (**configurable** number of req/ **configurable** time duration / user)
-  Redis for token sliding window storage
-  Global Exception Handling
-  Secure endpoints
-  Fully tested with **JUnit 5** and **Mockito**
-  Load-tested & concurrency-safe


##  Tech Stack

|      Layer       |      Technology      |
|------------------|----------------------|
| Backend          | Spring Boot          |
| Security         | Spring Security, JWT |
| Rate Limiting    | Redis, ZSet          |
| Auth             | Custom filter + JWT  |
| Testing          | JUnit 5, Mockito     |
| Build Tool       | Maven                |



##  Project Structure

```text
src/
 └── main/
     └── java/
         └── com/
             └── gateway/   
                 ├── config/         → Security config classes
                 ├── constants/      → App-wide constants
                 ├── controller/     → REST controllers
                 ├── filter/         → JWT & rate limit filters
                 ├── dto/            → Request/response DTOs
                 ├── exception/      → Custom exceptions & handlers
                 ├── entity/         → Data entities (if needed)
                 ├── repository/     → Redis & data access
                 ├── service/        
                 │   └── impl/       → Business logic implementations
                 └── util/           → Utility classes (e.g., JWT parser)
 └── test/
     └── java/
         └── com/
             └── gateway/   
                 ├── controller/
                 ├── filter/
                 ├── exception/
                 ├── service/
                 │   └── impl/
                 └── util/
```

##  Installation & Run

### Prerequisites
- Java 21+
- Redis Server running (locally or remote)
- Maven 3+
- Postgresql Server running (locally or remote)


## Security Considerations

Since this project focuses on demonstrating JWT authentication and rate-limiting middleware, it does not include endpoints for:

- User Registration
- Password Reset
- Password Encryption with BCrypt or other secure algorithms

To simplify testing Passwords are **base64-encoded** and stored in the database.

During login, the system checks base64-encoded passwords for matching.

##Assumptions Made

- The application is expected to run in a cloud-native or microservices environment.
- Users are already registered, and the **/auth** endpoint only generates tokens (no registration or password reset logic is included).
- A Redis instance is assumed to be running on localhost development and testing.
- The system will be deployed behind a reverse proxy or API gateway that forwards all requests to this Spring Boot application.
- Refresh tokens are not implemented. Access tokens are short-lived and must be reissued upon expiry.
- Each authenticated user is uniquely identified using the user-name header, which is expected to be included in requests and used during JWT validation.
