# 🚀 Travel Expense Management System

**Travel Expense Management System** is an application designed to manage business travel expenses. It helps users calculate travel costs, generate detailed reports in PDF format, and store travel-related data in a PostgreSQL database.

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white)

## 📜 Project Overview

The **Travel Expense Management System** simplifies the process of managing travel expenses. The system allows users to:

- **Calculate travel expenses** based on diet, transport, and accommodation costs.
- **Generate PDF reports** that summarize the entire travel expense.
- **Manage user profiles** and link them to their respective travel data.
- Store all data in a **PostgreSQL database**, easily deployable using Docker.

---

## 🎯 Key Features

- **Expense Calculation**: Automatically calculates travel costs, including daily allowances, transport costs, and accommodation fees.
- **PDF Report Generation**: Generates detailed travel expense reports in PDF format, which can be downloaded or viewed online.
- **User Management**: Administrators can add and manage users, linking them to travel data.
- **Database Integration**: All data related to travel and expenses is stored securely in a PostgreSQL database for easy access and reporting.

---

## 🛠️ Technologies Used

The project is built using:

- **Java**: Backend development language.
- **Spring Boot**: Framework for creating scalable backend applications.
- **PostgreSQL**: Relational database for securely storing user and travel data.
- **Docker**: Containerization to simplify deployment and management of the application and its database.
- **Apache PDFBox**: Library for generating PDF reports dynamically.

---

## 🚀 Getting Started

### Prerequisites

To run this project locally, ensure you have the following installed:

- **Java 21** or later
- **Docker & Docker Compose**
- **Maven**

### Installation

Follow these steps to set up the project locally:

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/travel-expense-management.git
   cd travel-expense-management
   
2. **Build and run the application using Docker**:
   ```bash
   docker-compose up --build
   
3. **Access the application**:
   - The backend API will be available at: http://localhost:8080
   - PgAdmin (for managing the database) will be accessible at: http://localhost:5050 with login credentials:
   - Email: admin@admin.com
   - Password: admin