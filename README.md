---
title: "GCA2 — N-tier System"
subtitle: "README"
description: "Project overview, setup, protocol, architecture, testing evidence, and contribution matrix for GCA2."
module: "COMP C8Z03 Object-Oriented Programming"
stage: "2 (Group Project)"
generated_at: "2026-03-04 20:12 Europe/Dublin"
---

# 2025-26 - OOP - L8 - GCA2 — N-tier System

## 1. Project Overview

### Domain summary
Our project is an Online Shop Order Management System which models a real e-commerce system that simulates how actual e-commerce businesses handle customers, products, and orders. This system allows multiple users to connect to a server and complete actions such as creating an account, browsing through products, and placing an order, along with the ability to review past order history. The system is supported by a relational database and implements the DAO pattern so that it separates business logic from data access. The system has four main entities: Customer, Product, Orders, and OrderItem. Customers can place multiple orders and each order can consist of many different products. The OrderItem entity operates as a join table to allow the system to maintain the many-to-many relationship between an order and product entity. The system has full CRUD functionality, provides stock management, calculates revenue from sales, and has sales reports. Binary file storage in this system represents data that may be associated with the domain, such as product images.

### Team
- **Group ID:** `2025-26-L8-OOP-GCA2-Group2`
- **Members:**
  - Aloka Muthumani Disanayaka Salpitige — `D00274456`
  - Adhira Pillai — `D00277397`
  - Victoria Olabode — `D00279232`

### Key features
- JDBC DAO layer with full CRUD (Stage 1 foundation)
- Client–server (sockets) JSON protocol + `ServerResponse<T>` wrapper
- Multithreaded server using `ExecutorService`
- Binary file upload + retrieval stored as DB BLOB with metadata
- JUnit 5 test suite with ≥70% line coverage evidence at final stage

---

## 2. How to Run

### Prerequisites
- Java: `17+`
- IntelliJ IDEA
- MySQL Server (local)
- Maven

### 2.1 Database setup
1. Create a database: `shopspheredb`
2. Run the script:
   - `shopspheredb.sql`
3. Verify seed data:
   - Each table has at least 10 rows.

### 2.2 Configure credentials
  - `db.url=jdbc:mysql://localhost:3306/shopspheredb?useSSL=false&serverTimezone=UTC`
  - `db.user=...`
  - `db.password=...`

### 2.3 Run the server
- Main class: `server.ServerMain`
- Default port: `5000` 
- Expected output:
  - “Server listening on …”
  - Logs for client connect/disconnect

### 2.4 Run the client(s)
- Main class: `client.ClientMain`
- Run **two clients simultaneously** for Stage 2+ demonstration.

---

## 3. Architecture Summary

### 3.1 N-tier overview
- Client (UI / console)
- Server (socket listener + request handlers + threading)
- DAO layer (interfaces + JDBC implementations)
- Database (MySQL)

### 3.2 Architecture diagram
- Path: `docs/architecture.md`
- Diagram format: Mermaid
  
---
