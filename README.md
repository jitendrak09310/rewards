# 🏆 Rewards Management System (Spring Boot)

## 📌 Overview

This project implements a rewards program for a retailer where customers earn points based on their purchase transactions.

The system exposes REST APIs to:

* Create and store transactions
* Calculate reward points per customer
* Provide monthly and total reward summaries for the last 3 months

---

## 🎯 Problem Statement

A customer earns:

* **2 points** for every dollar spent **above $100**
* **1 point** for every dollar spent **between $50 and $100**

### Example:

Transaction = $120

* $50 → 0 points
* $50 (50–100) → 50 points
* $20 (above 100) → 40 points

✅ Total = **90 points**

---

## 🛠️ Tech Stack

* Java 17
* Spring Boot
* Spring Data JPA
* MySQL
* Maven
---

## 🏗️ Architecture

The application follows **Layered Architecture**:

```
Controller → Service → Repository → Database
```

### Layers:

* **Controller** → Handles HTTP requests
* **Service** → Business logic (reward calculation)
* **Repository** → Data access using JPA
* **Model** → Entity classes
* **DTO** → Request/Response objects

---

## 🗄️ Database Design

### Table: `transactions`

| Column      | Type        |
| ----------- | ----------- |
| id          | BIGINT (PK) |
| customer_id | VARCHAR     |
| amount      | DOUBLE      |
| date        | DATE        |
| pointsEarned| DOUBLE      |

---

## ⚙️ Setup Instructions

### 1. Clone Repository

```bash
git clone <your-repo-url>
cd rewards-system
```

### 2. Configure MySQL

```sql
CREATE DATABASE rewards_db;
```

Update `application.properties`:

```properties
server.port=8082
spring.datasource.url=jdbc:mysql://localhost:3306/rewards_db
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
```

---

### 3. Run Application

```bash
mvn spring-boot:run
```

---

## 📡 API Endpoints

### 🔹 1. Create Transaction

**POST** `/api/createTransaction`

#### Request Body:

```json
{
  "customerId": "C1",
  "amount": 120,
  "date": "2026-06-14"
}
```

#### Response:

```json
{
    "id": 1,
    "customerId": "Jitendra",
    "amount": 1000.0,
    "date": "2026-05-14",
    "pointsEarned": 1850.0
}
```

---

### 🔹 2. Get Rewards

**GET** `/api/getRewards`

#### Response:

```json
[
    {
        "customerId": "C1",
        "monthlyPoints": {
            "MAY": 250,
            "APRIL": 115
        },
        "totalPoints": 365
    },
    {
        "customerId": "C2",
        "monthlyPoints": {
            "JUNE": 110,
            "MAY": 0
        },
        "totalPoints": 110
    },
    {
        "customerId": "Jitendra",
        "monthlyPoints": {
            "JUNE": 0,
            "MAY": 3700,
            "APRIL": 50
        },
        "totalPoints": 3750
    }
]
```

---

## 🧠 Reward Calculation Logic

```java
if (amount <= 50) return 0;
if (amount <= 100) return amount - 50;
return (50) + (amount - 100) * 2;
```

---

## 📊 Approach

1. Store transactions in MySQL
2. Fetch transactions for last 3 months
3. Group data:

   * By Customer
   * By Month
4. Calculate reward points per transaction
5. Aggregate:

   * Monthly rewards
   * Total rewards
6. Return structured response via REST API

---
## 👨‍💻 Author

Jitendra
Senior Software Engineer

---

## ✅ Conclusion

This project demonstrates:

* Clean architecture
* RESTful API design
* Java 8 stream usage
* Database integration using JPA
* Real-world reward calculation logic
---
