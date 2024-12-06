# Travel Expense Management System

**Travel Expense Management System** is a web application designed to simplify the management of business travel expenses. It automates the calculation of travel allowances, generates detailed PDF reports, and adheres to the Polish Labor Code regulations.

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=flat&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=flat&logo=spring-boot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=flat&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2CA5E0?style=flat&logo=docker&logoColor=white)
![OAuth2](https://img.shields.io/badge/OAuth2-Security-brightgreen?style=flat&logo=oauth)
![NGINX](https://img.shields.io/badge/NGINX-009639?style=flat&logo=nginx&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=flat&logo=spring-security&logoColor=white)
![H2 Database](https://img.shields.io/badge/H2_Database-F7DF1E?style=flat&logo=h2database&logoColor=black)
![JUnit 5](https://img.shields.io/badge/JUnit_5-25A162?style=flat&logo=java&logoColor=white)
![Liquibase](https://img.shields.io/badge/Liquibase-4A86E8?style=flat&logo=liquibase&logoColor=white)
![Apache PDFBox](https://img.shields.io/badge/Apache_PDFBox-D22128?style=flat&logo=apache&logoColor=white)

---

## Table of Contents

- [Project Overview](#project-overview)
- [Legal Basis](#legal-basis)
- [Key Features](#key-features)
- [Technologies Used](#technologies-used)
- [NGINX Configuration](#nginx-configuration)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
- [Usage](#usage)
    - [User Roles](#user-roles)
    - [Endpoints](#endpoints)
- [Contribution Guidelines](#contribution-guidelines)
- [License](#license)
- [Contact](#contact)

---

## Project Overview

This system is tailored for businesses and professionals who need to manage travel expenses effectively. It provides:

- **Automated Expense Calculation**: Covers diet, transport, and accommodation costs.
- **Detailed PDF Reports**: Generate professional travel expense documentation.
- **User Role Management**: Administrators, Managers, Accountants, and Users with customized permissions.
- **Secure and Scalable Backend**: Leveraging PostgreSQL and Docker for robust performance.

---

## Legal Basis

The system's calculations and functionalities are designed in accordance with the Polish Labor Code (Kodeks pracy), specifically:

- **Art. 775 § 2**: Governs the entitlement to travel allowances for domestic and international business trips.
- Covers:
    - Travel diets (allowances).
    - Transport and accommodation costs.
    - Other necessary expenses as defined by the employer.

By adhering to these regulations, the system ensures accurate and compliant management of travel expenses.

---

## Key Features

1. **Expense Management**
    - Automatic calculation of diets, transport costs, and accommodation fees.

2. **PDF Report Generation**
    - Generate professional and detailed reports in PDF format using Apache PDFBox.

3. **User Management**
    - Role-based access control (Admin, Manager, Accountant, User).
    - Secure OAuth2-based authentication, including Google Login integration.

4. **Data Storage**
    - PostgreSQL database with Liquibase for schema management.
    - Dockerized environment for consistent deployments.

5. **Additional Features**
    - Supports JSON Patch for efficient updates.
    - Background job scheduling using JobRunr.

---

## Technologies Used

- **Java 21**: Latest features for modern backend development.
- **Spring Boot**: Framework for building RESTful services.
- **PostgreSQL**: Secure and scalable relational database.
- **Docker & Docker Compose**: For easy application deployment.
- **NGINX**: Reverse proxy server for traffic management.
- **OAuth2**: Secure authentication flow with third-party providers.
- **Spring Security**: Ensuring application security.
- **JSON Patch (via java-json-tools)**: Enables efficient and standard-compliant partial updates.
- **Liquibase**: For seamless database migration management.
- **H2 Database**: In-memory database for testing and development.
- **JUnit 5**: For testing.
- **Apache PDFBox**: Library for creating PDF reports dynamically.
- **JobRunr**: Background job processing.

---

## NGINX Configuration

**NGINX** is used as a reverse proxy server to manage traffic and improve application performance.

### Example Configuration

```nginx

events {}

http {
    upstream javaapp_upstream {
        server javaapp:8080;
    }

    server {
        listen 80;

        location / {
            proxy_pass http://javaapp_upstream;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
        }
    }
}
   ```
---

## Getting Started

### Prerequisites

Ensure you have the following installed:

- **Java 21** or later
- **Docker & Docker Compose**
- **Maven**

---

### Installation

1. **Clone the repository**:
    ```bash
    git clone https://github.com/your-username/travel-expense-management.git
    cd travel-expense-management
    ```

2. **Configure Environment Variables**:

   Create a `.env` file in the project root and add:

    ```env
    POSTGRES_DB=travel_expense
    POSTGRES_USER=postgres
    POSTGRES_PASSWORD=admin
    ```

3. **Build and Run with Docker**:

    ```bash
    docker-compose up --build
    ```

4. **Access the Application**:

   - **Backend API**: [http://localhost:8080](http://localhost:8080)
   - **PgAdmin (Database Management)**: [http://localhost:5050](http://localhost:5050)
   - **Default Credentials**:
      - **Email**: `admin@admin.com`
      - **Password**: `admin`
---

## Usage

### User Roles

- **Admin**: Manage users and configurations.
- **Manager**: Review and approve reports.
- **Accountant**: Process financial data. Review and approve reports.
- **User**: Records personal travel expenses.

### Endpoints

**General:**
- Registers a new user:
```
POST /api/v1/users/new-user
```

**Admin:**
- Retrieves a list of all registered users:
```
GET /api/v1/users/all-users
```

- Changes the specified user's role to ACCOUNTANT:
```
PATCH /api/v1/users/{email}/change-role-to-accountant
```

- Changes the specified user's role to MANAGER:
```
PATCH /api/v1/users/{email}/change-role-to-manager
```

**Employee:**
- Creates a new travel expense report:
```
POST /api/v1/travels
```

- Retrieves all travel expense reports associated with the current user:
```
GET /api/v1/travels
```

- Retrieves details of a specific travel expense report by its unique identifier (techId):
```
GET /api/v1/travels/{techId}
```

- Updates an existing travel expense report using a JSON Patch:
```
PATCH /api/v1/travels/update/{techId}
```

**Accountant, Manager:**
- Retrieves all travel reports pending approval for the current approver:
```
GET /api/v1/approvals/pending
```

- Approves a specific travel report identified by travelId:
```
POST /api/v1/approvals/{travelId}/approve
```

- Rejects a specific travel report identified by travelId:
```
POST /api/v1/approvals/{travelId}/reject
```

**Common Functionality:**
- Generates a PDF report for a specific travel expense report identified by techId. The report is returned as a downloadable file:
```
POST /api/v1/travels/print/{techId}
```

---

## Contribution Guidelines

1. **Fork the repository**.
2. **Create a feature branch**:
    ```bash
    git checkout -b feature/your-feature
    ```
3. **Commit your changes**:
    ```bash
    git commit -m 'Add a new feature'
    ```
4. **Push to the branch**:
    ```bash
    git push origin feature/your-feature
    ```
5. **Open a pull request**.

---

## License

This project is licensed under the [Apache License 2.0](LICENSE). See the LICENSE file for details.

---

## Contact

For any inquiries or suggestions, please reach out to **Sergii Ginkota**.

---
