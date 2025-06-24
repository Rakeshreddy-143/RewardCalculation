
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

### 1. Summary for All Customers

**POST** `/rewards/summary`

#### Request Body

```json
[
  {
    "personId": 1,
    "amount": 120,
    "transactionDate": "2025-05-15"
  }
]
```
### Response
```json
{
  "rewards": {
    "1": {
      "totalRewardPoints": 90,
      "transactions": [
        {
          "month": "2025-05-01",
          "totalSpent": 120,
          "rewardPoints": 90
        }
      ]
    }
  }
}
```


⸻

### 2. Summary for Specific Customer and Date Range

**POST** `/rewards/customer/{customerId}?from=2025-05-01&to=2025-06-01`

Path Parameter
	•	customerId: ID of the customer

Query Parameters
	•	from: Start date (inclusive)
	•	to: End date (inclusive)

Request Body

[
  {
    "personId": 1,
    "amount": 120,
    "transactionDate": "2025-05-15"
  },
  {
    "personId": 1,
    "amount": 90,
    "transactionDate": "2025-05-20"
  },
  {
    "personId": 2,
    "amount": 100,
    "transactionDate": "2025-05-22"
  }
]

Response

{
  "rewards": {
    "1": {
      "totalRewardPoints": 110,
      "transactions": [
        {
          "month": "2025-05-01",
          "totalSpent": 210,
          "rewardPoints": 110
        }
      ]
    }
  }
}


⸻

How to Run

Prerequisites
	•	Java 17+
	•	Maven 3.6+

Build & Run

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run

The app will start at:
http://localhost:8080

⸻

🗂 Project Structure

src/main/java/
└── com/example/retail
    ├── controller
    │   └── RetailApplicationController.java     # Exposes API endpoints
    ├── service
    │   └── RetailApplicationService.java        # Core reward calculation logic
    ├── model
    │   ├── RewardRequest.java                   # Input DTO
    │   ├── RewardResponse.java                  # Output DTO
    │   ├── RewardTransactionSummary.java        # Monthly reward breakdown
    │   └── MonthlyRewardSummary.java            # Per customer summary
    └── repository
        └── TransactionRepository.java           # (Stub/Placeholder for persistence)


⸻

🧪 Sample Usage with curl

Calculate Rewards for All Customers

curl -X POST http://localhost:8080/rewards/summary \
  -H "Content-Type: application/json" \
  -d '[{"personId":1,"amount":120,"transactionDate":"2025-05-15"}]'

Calculate Rewards for a Specific Customer

curl -X POST "http://localhost:8080/rewards/customer/1?from=2025-05-01&to=2025-06-01" \
  -H "Content-Type: application/json" \
  -d '[{"personId":1,"amount":120,"transactionDate":"2025-05-15"}]'


⸻

