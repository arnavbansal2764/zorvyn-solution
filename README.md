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
