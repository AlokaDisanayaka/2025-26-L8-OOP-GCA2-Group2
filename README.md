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

### Key features (Stage 1)
-DTO classes with encapsulated validated fields for each entity
-DAO layer implemented using the JDBC API and PreparedStatements
-Full CRUD operations for Customer and Product
-Predicate-based filtering for flexible querying
-JSON serialization and deserialization using Jackson
-Round-trip JSON conversion testing
-MySQL relational database with normalized schema
-Architecture diagram showing Client → Server → DAO → Database

Future stages will extend the system with:
- a multithreaded client–server architecture
- JSON communication protocol
- binary file storage using BLOBs
- automated testing using JUnit

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
  - 
### 2.3 Run the application (Stage 1 testing)

Main class: app.Main

This class is used to test:
- DAO CRUD operations
- Predicate filtering
- JSON conversion

---
## 3.DAO Layer Overview 
-The DAO layer separates database access from business logic. Each entity has a corresponding DAO interface and a JDBC implementation class.

CustomerDao -> JdbcCustomerDao
ProductDao -> JdbcProductDao
OrderDao -> JbbcOrderDao
OrderItemDao -> JbdcOrderItemDao

-The service layer depends only on the DAO interface, allowing the underlying database implementation to be changed without affecting the business logic.

-All SQL statements use PreparedStatement to prevent SQL injection and follow the project requirements.

## 4. JSON Conversion

-Jackson’s ObjectMapper is used to convert DTO objects to and from JSON.

-Implemented methods:
customerToJson(Customer)
customerFromJson(String json)
customerListToJson(List<Customer>)

productToJson(Product)
productFromJson(String json)
productListToJson(List<Product>)

-Round-trip testing ensures that objects can be converted to JSON and reconstructed without data loss.

## 5. DTO Structure 

```
org
 ├── DTO
 │     Customer.java
 │     Product.java
 │     Orders.java
 │     OrderItem.java
 │
 ├── DAO
 │     CustomerDao.java
 │     ProductDao.java
 │     OrdersDao.java
 │     OrderItemDao.java
 │
 ├── DAO_JDBC
 │     JdbcCustomerDao.java
 │     JdbcProductDao.java
 │     JdbcOrdersDao.java
 │     JdbcOrderItemDao.java
 │
 ├── Services
 │     CustomerService.java
 │     ProductService.java
 │     OrdersService.java
 │     OrderItemService.java
 │
 ├── JsonUtil
 │     CustomerJsonUtil.java
 │     ProductJsonUtil.java
 │     OrdersJsonUtil.java
 │     OrderItemJsonUtil.java
 │
 └── DB
       DBConnection.java
```
## 6. Architecture Summary

### 6.1 N-tier overview
- Client (UI / console)
- Server (planned for Stage 2 – socket listener + request handlers)
- DAO layer (interfaces + JDBC implementations)
- Database (MySQL)

### 6.2 Architecture diagram
- Path: `docs/architecture.md`
- Diagram format: Mermaid
  
---
