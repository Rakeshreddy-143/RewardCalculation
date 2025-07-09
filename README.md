
# 🛍️ Retail Rewards Application

This is a Spring Boot REST API that calculates customer reward points based on their transaction history. It processes transaction data and returns monthly and total reward summaries for individual or multiple customers.

---

## ✅ Features

- Compute reward points based on transaction amounts.
- Summarize monthly reward points per customer.
- Filter transactions by customer ID and date range.
- RESTful API design using Spring Boot.
- Input validation and clean DTO-based architecture.

---

## 🎯 Reward Calculation Rules

Reward points are calculated per transaction as follows:

- **2 points** for every dollar spent **over $100**.
- **1 point** for every dollar spent **over $50**, up to $100.
- **No points** for transactions under $50.

**Example:**
- A $120 purchase gives:  
  → (120 - 100) × 2 = 40  
  → (100 - 50) × 1 = 50  
  → **Total = 90 points**

---

## 🔗 API Endpoints

### 1. Summary for a Customer

**GET** `/rewards/customer/{customerId}`

#### Response
```json
{
  "rewards": {
    "1": {
      "transactions": [
        {
          "month": "2025-05-01",
          "totalSpent": 120,
          "rewardPoints": 90,
          "numberOfTransactions": 1
        }
      ]
    }
  }
}
```


⸻

### 2. Summary for Specific Customer and Date Range

`GET` `/rewards/customer/{customerId}?from=2025-05-01&to=2025-06-01`

---

#### **Path Parameter**

* `customerId`: ID of the customer

---

#### **Query Parameters**

| Name   | Type     | Description                                   |
| ------ | -------- | --------------------------------------------- |
| `from` | `String` | Start date (inclusive) — format: `YYYY-MM-DD` |
| `to`   | `String` | End date (inclusive) — format: `YYYY-MM-DD`   |

---

#### 📤 **Response**

```json
{
  "rewards": {
    "1": {
      "transactions": [
        {
          "month": "2025-05-01",
          "totalSpent": 210,
          "rewardPoints": 110,
          "numberOfTransactions": 3
        }
      ]
    }
  }
}
```

⸻

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run

The app will start at:
http://localhost:8080

⸻

### 🧪 Sample Usage with curl

#### Calculate Rewards for a Specific Customer

curl --location 'http://localhost:8080/api/v1/rewards/customer/1?from=2025-05-01&to=2025-06-01'

⸻
