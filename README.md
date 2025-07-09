# spring-gateway

# Rate-Limited API Gateway using Spring Boot, JWT, and Redis

A lightweight, secure, and scalable API Gateway middleware that supports:
- **JWT-based Authentication**
- **Redis-backed Rate Limiting**
- **Spring Security Integration**
- **Per-user Sliding Window Rate Limiting (100 requests/minute)**

---

## 📌 Features

- ✅ JWT Authentication Filter
- ✅ Rate Limiting Filter (100 req/min/user)
- ✅ Redis for token bucket/sliding window storage
- ✅ Global Exception Handling
- ✅ Secure endpoints
- ✅ Fully tested with **JUnit 5** and **Mockito**
- ✅ Load-tested & concurrency-safe

---

## 🚀 Tech Stack

| Layer            | Technology         |
| Backend          | Spring Boot        |
| Security         | Spring Security, JWT |
| Rate Limiting    | Redis, ZSet        |
| Auth             | Custom filter + JWT |
| Testing          | JUnit 5, Mockito   |
| Build Tool       | Maven              |

---

## 🧠 Architecture Overview

