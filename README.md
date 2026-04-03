# Zorvyn Solution

Zorvyn Solution is a Spring Boot application designed with a robust Role-Based Access Control (RBAC) system and PostgreSQL persistence. It provides secure entry points for different user personas: Viewers, Analysts, and Administrators.

## Core Functionality

### 1. PostgreSQL Integration (Docker)
The application is configured to connect to a PostgreSQL database running in a Docker container. All user credentials and roles are persisted in the `users` table.

### 2. Spring Security & RBAC 
A comprehensive security layer is implemented using Spring Security. Access to API endpoints is strictly governed by the user's assigned role:
- **Viewer**: Read-only access to basic dashboard data.
- **Analyst**: Access to dashboard, insights, and record viewing capabilities.
- **Admin**: Full authority including record creation, modification, and user management.

### 3. Database-backed Authentication
Authentication is performed against the database using a custom `UserDetailsService`. Passwords are encrypted using `BCryptPasswordEncoder` before storage.

### 4. Automated Data Seeding
On every startup, the `DataInitializer` checks if the database is empty and automatically creates three default users for testing purposes:
- **Admin**: `admin` / `admin123`
- **Analyst**: `analyst` / `analyst123`
- **Viewer**: `viewer` / `viewer123`

### 5. Data Seeding
The application provides two ways to populate the database with sample data:
- **Automatic**: On first run (if database is empty), the `DataInitializer` automatically creates sample incomes and expenses for the last 3 months.
- **Manual Script**: Run the provided bash script to add more data via the API:
  ```bash
  ./seed-data.sh
  ```

---

## API Documentation

All endpoints require **Basic Authentication** or **Form-based Login**.

### Endpoint Overview

| Method | URL | Access Level | Description |
| :--- | :--- | :--- | :--- |
| `GET` | `/dashboard/data` | VIEWER, ANALYST, ADMIN | Retrieves high-level dashboard metrics. |
| `GET` | `/api/insights/access` | ANALYST, ADMIN | Accesses deep insights and analytical data. |
| `GET` | `/api/records/view` | ANALYST, ADMIN | Retrieves detailed records for viewing. |
| `POST` | `/api/records/create` | ADMIN | Creates a new record in the system. |
| `PUT` | `/api/records/update` | ADMIN | Updates an existing record. |
| `DELETE` | `/api/records/delete` | ADMIN | Removes a record from the database. |
| `POST` | `/api/transactions` | ADMIN | Creates a new financial transaction. |
| `GET` | `/api/transactions` | ANALYST, ADMIN | Retrieves all financial transactions. |
| `GET` | `/api/transactions/{id}` | ANALYST, ADMIN | Retrieves a single transaction by ID. |
| `GET` | `/api/transactions/filter` | ANALYST, ADMIN | Filters transactions by type, category, date. |
| `PUT` | `/api/transactions/{id}` | ADMIN | Updates an existing transaction. |
| `DELETE` | `/api/transactions/{id}` | ADMIN | Deletes a transaction. |
| `GET` | `/dashboard/summary` | VIEWER, ANALYST, ADMIN | Dashboard overall totals & net balance. |
| `GET` | `/dashboard/categories` | VIEWER, ANALYST, ADMIN | Dashboard category breakdown. |
| `GET` | `/dashboard/recent` | VIEWER, ANALYST, ADMIN | Dashboard recent activity (limit parameter optional). |
| `GET` | `/dashboard/trends/monthly` | VIEWER, ANALYST, ADMIN | Dashboard monthly income/expense trends. |

### Request & Response Objects

#### 1. Dashboard Data
- **URL**: `/dashboard/data`
- **Request**: `GET` (Empty body)
- **Response**: `200 OK`
  - **Body**: `String` ("Dashboard Data: Accessible by Viewer, Analyst, and Admin.")
- **Error**: `401 Unauthorized` (if not logged in)

#### 2. Analytical Insights
- **URL**: `/api/insights/access`
- **Request**: `GET` (Empty body)
- **Response**: `200 OK`
  - **Body**: `String` ("Insights Access: Restricted to Analyst and Admin.")
- **Error**: `403 Forbidden` (if accessed by Viewer)

#### 3. Record Management (Admin Only)
- **URL**: `/api/records/create` | `/api/records/update` | `/api/records/delete`
- **Request**: `POST` | `PUT` | `DELETE` (Empty body in sample)
- **Response**: `200 OK`
  - **Body**: `String` (e.g., "Record Created: Admin only.")
- **Error**: `403 Forbidden` (if accessed by Viewer or Analyst)

---

### 4. Transactions API

All transaction endpoints are under `/api/transactions` and require authentication.

#### 4.1 Create Transaction
- **URL**: `POST /api/transactions`
- **Access**: Admin
- **Request Body**:
  ```json
  {
    "amount": 1500.00,
    "type": "INCOME",
    "category": "Salary",
    "date": "2026-04-01",
    "notes": "Monthly salary credit"
  }
  ```
- **Response**: `201 Created`
  ```json
  {
    "id": 1,
    "amount": 1500.00,
    "type": "INCOME",
    "category": "Salary",
    "date": "2026-04-01",
    "notes": "Monthly salary credit"
  }
  ```

#### 4.2 Get All Transactions
- **URL**: `GET /api/transactions`
- **Access**: Analyst, Admin
- **Response**: `200 OK` — Array of transaction objects.

#### 4.3 Get Transaction by ID
- **URL**: `GET /api/transactions/{id}`
- **Access**: Analyst, Admin
- **Response**: `200 OK` — Single transaction object.
- **Error**: `500` if ID not found.

#### 4.4 Filter Transactions
- **URL**: `GET /api/transactions/filter`
- **Access**: Analyst, Admin
- **Query Parameters** (all optional, can be combined):
  | Parameter | Type | Example |
  | :--- | :--- | :--- |
  | `type` | `INCOME` or `EXPENSE` | `type=INCOME` |
  | `category` | String | `category=Salary` |
  | `startDate` | ISO date | `startDate=2026-01-01` |
  | `endDate` | ISO date | `endDate=2026-03-31` |
- **Response**: `200 OK` — Filtered array of transaction objects.

#### 4.5 Update Transaction
- **URL**: `PUT /api/transactions/{id}`
- **Access**: Admin
- **Request Body** (all fields optional — only provided fields are updated):
  ```json
  {
    "amount": 2000.00,
    "notes": "Updated salary amount"
  }
  ```
- **Response**: `200 OK` — Updated transaction object.

#### 4.6 Delete Transaction
- **URL**: `DELETE /api/transactions/{id}`
- **Access**: Admin
- **Response**: `204 No Content`

#### Transaction Field Reference

| Field | Type | Required | Description |
| :--- | :--- | :---: | :--- |
| `amount` | `BigDecimal` | ✅ | Monetary value (e.g., `1500.00`) |
| `type` | `INCOME` \| `EXPENSE` | ✅ | Transaction type |
| `category` | `String` | ✅ | Category label (e.g., `Salary`, `Rent`) |
| `date` | `LocalDate` (ISO) | ❌ | Defaults to today if omitted |
| `notes` | `String` | ❌ | Optional description or memo |

---

## Quick Testing with Curl

You can verify the role-based access by running the following commands. Ensure the application is running at `localhost:8080`.

### 1. Unauthenticated Access (Expected: 401 Unauthorized)
```bash
curl -i http://localhost:8080/dashboard/data
```

### 2. Viewer Access
- **Dashboard (Success):**
  ```bash
  curl -u viewer:viewer123 http://localhost:8080/dashboard/data
  ```
- **Insights (Expected: 403 Forbidden):**
  ```bash
  curl -i -u viewer:viewer123 http://localhost:8080/api/insights/access
  ```

### 3. Analyst Access
- **Insights (Success):**
  ```bash
  curl -u analyst:analyst123 http://localhost:8080/api/insights/access
  ```
- **Manage Records (Expected: 403 Forbidden):**
  ```bash
  curl -i -u analyst:analyst123 -X POST http://localhost:8080/api/records/create
  ```

### 4. Admin Access
- **Manage Records (Success):**
  ```bash
  curl -u admin:admin123 -X POST http://localhost:8080/api/records/create
  ```

### 5. Transactions API

- **Create a transaction (Admin):**
  ```bash
  curl -u admin:admin123 -X POST http://localhost:8080/api/transactions \
    -H "Content-Type: application/json" \
    -d '{"amount":1500.00,"type":"INCOME","category":"Salary","date":"2026-04-01","notes":"Monthly salary"}'
  ```

- **Get all transactions (Analyst):**
  ```bash
  curl -u analyst:analyst123 http://localhost:8080/api/transactions
  ```

- **Get transaction by ID (Analyst):**
  ```bash
  curl -u analyst:analyst123 http://localhost:8080/api/transactions/1
  ```

- **Filter by type:**
  ```bash
  curl -u analyst:analyst123 "http://localhost:8080/api/transactions/filter?type=INCOME"
  ```

- **Filter by category:**
  ```bash
  curl -u analyst:analyst123 "http://localhost:8080/api/transactions/filter?category=Salary"
  ```

- **Filter by date range:**
  ```bash
  curl -u analyst:analyst123 "http://localhost:8080/api/transactions/filter?startDate=2026-01-01&endDate=2026-03-31"
  ```

- **Filter by all criteria combined:**
  ```bash
  curl -u analyst:analyst123 "http://localhost:8080/api/transactions/filter?type=INCOME&category=Salary&startDate=2026-01-01&endDate=2026-04-30"
  ```

- **Update a transaction (Admin):**
  ```bash
  curl -u admin:admin123 -X PUT http://localhost:8080/api/transactions/1 \
    -H "Content-Type: application/json" \
    -d '{"amount":2000.00,"notes":"Updated salary"}'
  ```

- **Delete a transaction (Admin):**
  ```bash
  curl -u admin:admin123 -X DELETE http://localhost:8080/api/transactions/1
  ```

- **Analyst attempting to create (Expected: 403 Forbidden):**
  ```bash
  curl -i -u analyst:analyst123 -X POST http://localhost:8080/api/transactions \
    -H "Content-Type: application/json" \
    -d '{"amount":500.00,"type":"EXPENSE","category":"Food","date":"2026-04-01"}'
  ```

### 6. Dashboard Summary API

These endpoints are designed for a dashboard UI and are accessible by **Viewer**, **Analyst**, and **Admin**.

- **Get overall summary (income, expenses, net balance, transaction counts):**
  ```bash
  curl -u viewer:viewer123 http://localhost:8080/dashboard/summary
  ```

- **Get totals grouped by category:**
  ```bash
  curl -u viewer:viewer123 http://localhost:8080/dashboard/categories
  ```

- **Get recent transaction activity (default limit is 5):**
  ```bash
  curl -u viewer:viewer123 http://localhost:8080/dashboard/recent
  ```

- **Get recent transaction activity with custom limit:**
  ```bash
  curl -u viewer:viewer123 http://localhost:8080/dashboard/recent?limit=10
  ```

- **Get monthly trends (default is last 6 months):**
  ```bash
  curl -u viewer:viewer123 http://localhost:8080/dashboard/trends/monthly
  ```

- **Get monthly trends for a specific date range:**
  ```bash
  curl -u viewer:viewer123 "http://localhost:8080/dashboard/trends/monthly?startDate=2025-01-01&endDate=2026-12-31"
  ```

---

## Getting Started

### Prerequisites
- Docker
- Java 21+
- Maven

### Installation
1. Ensure your PostgreSQL container is running:
   ```bash
   docker start postgres-container
   ```
2. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
