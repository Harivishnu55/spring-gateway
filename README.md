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

-------------------------------------------
|      Layer       |      Technology      |
|------------------|----------------------|
| Backend          | Spring Boot          |
| Security         | Spring Security, JWT |
| Rate Limiting    | Redis, ZSet          |
| Auth             | Custom filter + JWT  |
| Testing          | JUnit 5, Mockito     |
| Build Tool       | Maven                |
-------------------------------------------



##  Project Structure

src/
 └── main/
    ├── java/
        ├── com/
            ├── gateway/   
                ├── config/
                ├── constants/
                ├── controller/
                ├── filter/
                ├── dto/
                ├── exception/
                ├── entity/
                ├── repository/
                ├── service/
                    ├── impl/
                └── util/
 └── test/
     ├── java/
        ├── com/
            ├── gateway/   
                ├── controller/
                ├── filter/
                ├── exception/
                ├── service/
                    ├── impl/
                └── util/


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