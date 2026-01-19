# Expense Tracker REST API

A secure and scalable **Expense Tracker Backend** built using **Spring Boot**, **Spring Security**, and **JWT authentication**.  
The application allows users to manage expenses, categories, and budgets with strict ownership and security controls.

---

## 🚀 Features

- User Registration & Login (JWT-based authentication)
- Role-based authorization
- Expense CRUD operations
- Category management
- Monthly budget limit enforcement
- Pagination & sorting for expenses
- Global exception handling
- Secure password encryption using BCrypt
- Docker-based MySQL support

---

## 🛠 Tech Stack

- Java 17
- Spring Boot
- Spring Security
- JWT (JSON Web Tokens)
- Spring Data JPA (Hibernate)
- MySQL
- Docker
- Maven

---

## 🔐 Security Highlights

- Stateless authentication using JWT
- Password hashing with BCrypt
- Ownership validation (users can access only their own expenses)
- Protected APIs using Spring Security filter chain

---

## 📌 API Endpoints

### 🔑 Authentication
| Method | Endpoint | Description |
|------|---------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login & get JWT token |

---

### 🗂 Category
| Method | Endpoint | Description |
|------|---------|-------------|
| GET | `/categories` | Get all predefined categories |

---

### 💸 Expense
| Method | Endpoint | Description |
|------|---------|-------------|
| POST | `/expenses` | Create a new expense |
| GET | `/expenses` | Get expenses (paginated) |
| GET | `/expenses/{id}` | Get expense by ID |
| PUT | `/expenses/{id}` | Update expense |
| DELETE | `/expenses/{id}` | Delete expense |

---

### 💰 Budget
| Method | Endpoint | Description |
|------|---------|-------------|
| POST | `/budgets` | Set monthly budget |
| GET | `/budgets` | Get current budget |

---

## 📤 Request Example (Create Expense)

```json
{
  "amount": 500,
  "description": "Groceries",
  "expenseDate": "2026-01-17",
  "categoryId": 1
}