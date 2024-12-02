# Travel Expense Management System

**Travel Expense Management System** is a modern web application designed to simplify the management of business travel expenses. It automates the process of expense calculation, generates detailed reports, and supports secure role-based user management.

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.5-F2F4F9?style=for-the-badge&logo=spring-boot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white)
![OAuth2](https://img.shields.io/badge/OAuth2-Security-brightgreen?style=for-the-badge&logo=oauth)
![NGINX](https://img.shields.io/badge/NGINX-009639?style=for-the-badge&logo=nginx&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white)
![H2 Database](https://img.shields.io/badge/H2_Database-F7DF1E?style=for-the-badge&logo=h2database&logoColor=black)
![Liquibase](https://img.shields.io/badge/Liquibase-4A86E8?style=for-the-badge&logo=liquibase&logoColor=white)

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
- **Chapter 1: General Provisions**
   - Defines the scope of travel expenses, including domestic ("podróżą krajową") and international trips ("podróżą zagraniczną").
- **Chapter 2: Domestic Travel**
   - Outlines the specifics of domestic travel allowances, meal costs, transportation, accommodation, and other necessary expenses.

By adhering to these regulations, the system ensures accurate and compliant management of travel expenses for employees in state or local government budgetary units.

---

## Key Features

1. **Expense Management**
   - **Automatic Calculation**: Computes travel costs, including allowances (diet), transport, and accommodation based on Kodeks pracy.
   - **JSON Patch Support**: Enables efficient and partial updates of travel records.

2. **PDF Report Generation**
   - **Dynamic Reports**: Generates detailed travel reports in PDF format using Apache PDFBox.

3. **User Management**
   - **Role-Based Access Control**: Assigns roles such as Admin, Manager, Accountant, and User with specific permissions.
   - **Secure Authentication**: Implements OAuth2-based authentication for enhanced security, including Google Login integration.

4. **Data Storage**
   - **PostgreSQL Database**: Stores all travel and expense data securely with Liquibase managing schema migrations.
   - **Dockerized Deployment**: Ensures easy and consistent deployment across different environments.

5. **Job Scheduling and Sessions**
   - **Asynchronous Tasks**: Manages background jobs using JobRunr.
   - **Session Management**: Utilizes JDBC-based session storage for durability and scalability.

---

## Technologies Used

- **Java 21**: Latest features for modern backend development.
- **Spring Boot 3.3.5**: Framework for building RESTful services.
- **PostgreSQL**: Secure and scalable relational database.
- **Docker & Docker Compose**: For easy application deployment.
- **NGINX**: Reverse proxy server for traffic management.
- **OAuth2**: Secure authentication flow with third-party providers.
- **Spring Security**: Ensuring application security.
- **Liquibase**: For seamless database migration management.
- **H2 Database**: In-memory database for testing and development.
- **Apache PDFBox**: Library for creating PDF reports dynamically.
- **JobRunr**: Background job processing.

---

## NGINX Configuration

**NGINX** is used as a reverse proxy server to manage incoming traffic, enhance security, and improve application performance. Below is an example configuration that includes the essential `events {}` block.

### Example NGINX Configuration

```nginx
events {}

http {
    upstream javaapp_upstream {
        server javaapp:8080;
        server javaapp:8080;
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

- **Admin**: Full control over user and expense management.
- **Manager**: Manages team expenses and reports.
- **Accountant**: Handles financial documentation and data.
- **User**: Records personal travel expenses.

### Endpoints

- **Expense Management**: `/api/expenses`
- **PDF Report Generation**: `/api/reports`
- **User Management**: `/api/users`

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
