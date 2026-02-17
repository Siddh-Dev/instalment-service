# 💳 Credit Card Instalment API

A Spring Boot REST API that returns credit card transactions eligible for instalment conversion.
Demonstrates calling a downstream API using **Spring `RestTemplate`** within a clean layered architecture.

---

## 🏗️ Architecture

```
Client (curl / Postman)
        │
        ▼
┌─────────────────────────────────────────────────────────┐
│                    Spring Boot App                       │
│                                                         │
│  ┌──────────────────────────────────────────────────┐  │
│  │           InstalmentController                    │  │
│  │   GET /api/v1/instalment/eligible-transactions   │  │
│  └────────────────────┬─────────────────────────────┘  │
│                       │ delegates to                    │
│  ┌────────────────────▼─────────────────────────────┐  │
│  │             InstalmentService                     │  │
│  │   Uses RestTemplate to call downstream URL       │  │
│  └────────────────────┬─────────────────────────────┘  │
│                       │ HTTP GET via RestTemplate       │
│  ┌────────────────────▼─────────────────────────────┐  │
│  │         MockDownstreamController                  │  │
│  │   GET /mock/downstream/eligible-transactions     │  │
│  │   (Simulates an external downstream API)         │  │
│  └──────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
```

---

## 📁 Project Structure

```
instalment-api/
├── pom.xml                                          # Maven build descriptor (Java 21, Spring Boot 3.2.3)
└── src/
    ├── main/
    │   ├── java/com/creditcard/instalment/
    │   │   ├── InstalmentApiApplication.java        # @SpringBootApplication entry point
    │   │   ├── config/
    │   │   │   └── RestTemplateConfig.java          # RestTemplate bean (timeouts configured)
    │   │   ├── controller/
    │   │   │   ├── InstalmentController.java        # PUBLIC API: /api/v1/instalment/...
    │   │   │   └── MockDownstreamController.java    # MOCK: /mock/downstream/... (simulates downstream)
    │   │   ├── model/
    │   │   │   ├── EligibleTransaction.java         # Transaction DTO
    │   │   │   └── EligibleTransactionsResponse.java# Response wrapper DTO
    │   │   └── service/
    │   │       └── InstalmentService.java           # Calls downstream via RestTemplate
    │   └── resources/
    │       └── application.properties              # Server config + downstream URL
    └── test/
        └── java/com/creditcard/instalment/
            └── controller/
                └── InstalmentControllerTest.java   # MockMvc unit tests
```

---

## 🚀 How to Run

### Prerequisites
- Java 21+
- Maven 3.8+

### Build & Start
```bash
cd instalment-api
mvn clean install
mvn spring-boot:run
```

The application starts on **port 8080**.

---

## 🌐 API Endpoints

### ✅ Primary Public Endpoint

```
GET http://localhost:8080/api/v1/instalment/eligible-transactions
```

**Response (200 OK):**
```json
{
  "eligibleTransactions": [
    {
      "merchantName": "Amazon India",
      "transactionDate": "2026-02-10",
      "amount": 24999.0
    },
    {
      "merchantName": "Reliance Digital",
      "transactionDate": "2026-02-08",
      "amount": 58990.0
    },
    {
      "merchantName": "Flipkart",
      "transactionDate": "2026-02-05",
      "amount": 18999.0
    },
    {
      "merchantName": "Croma",
      "transactionDate": "2026-02-02",
      "amount": 74999.0
    },
    {
      "merchantName": "Apple Store Online",
      "transactionDate": "2026-01-30",
      "amount": 129900.0
    }
  ]
}
```

**Error Response (502 Bad Gateway):** Returned if downstream call fails.

---

### 🔧 Mock Downstream Endpoint (internal use)

```
GET http://localhost:8080/mock/downstream/eligible-transactions
```

This simulates the downstream API. You can also call it directly for testing.

---

### 🩺 Actuator Health Check

```
GET http://localhost:8080/actuator/health
```

---

## 🧪 Running Tests

```bash
mvn test
```

Tests use `@WebMvcTest` + `MockMvc` and mock the `InstalmentService` to test:
- `200 OK` with correct transaction payload
- `502 Bad Gateway` when downstream throws `RestClientException`

---

## ⚙️ Configuration (`application.properties`)

| Property | Default | Description |
|---|---|---|
| `server.port` | `8080` | Application port |
| `downstream.api.base-url` | `http://localhost:8080` | Downstream base URL |
| `downstream.api.eligible-transactions-path` | `/mock/downstream/eligible-transactions` | Downstream path |

To point to a **real** downstream API, just change these properties:
```properties
downstream.api.base-url=https://real-api.bank.com
downstream.api.eligible-transactions-path=/v2/card/eligible-transactions
```

---

## 🛠️ Tech Stack

| Technology | Version |
|---|---|
| Java | 21 |
| Spring Boot | 3.2.3 |
| Spring Web (RestTemplate) | via Spring Boot |
| Lombok | via Spring Boot |
| JUnit 5 + MockMvc | via spring-boot-starter-test |
| Maven | 3.8+ |

---

## 📞 cURL Example

```bash
curl -s -X GET http://localhost:8080/api/v1/instalment/eligible-transactions \
  -H "Accept: application/json" | jq .
```
