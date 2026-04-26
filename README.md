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

### Key features (Stage 1 + Stage 2 Implemented)

- DTO classes with encapsulated validated fields for each entity
- DAO layer implemented using the JDBC API and PreparedStatements
- Full CRUD operations for Customer and Product
- Predicate-based filtering for flexible querying
- JSON serialization and deserialization using Jackson
- Round-trip JSON conversion testing
- MySQL relational database with normalized schema
- Architecture diagram showing Client → Server → DAO → Database

### Stage 2 additions:

- Multithreaded client–server architecture using ExecutorService
- JSON-based communication between client and server
- Generic ServerResponse<T> wrapper for all responses
- Client-server support for:
        getAll
        getById
        add
        update
        delete
- Structured error handling (no raw exceptions sent to client)

### Future stages will extend the system with:
- binary file storage using BLOBs
- automated testing using JUnit
  
## 2. How to Run
### Prerequisites

Java: 17+
IntelliJ IDEA
MySQL Server (local)
Maven

### 2.1 Database setup

1. Create a database:
`shopspheredb`

2. Run the script:
```
shopspheredb.sql
```

3. Verify seed data:
  - Each table has at least 10 rows.

### 2.2 Configure credentials
```
db.url=jdbc:mysql://localhost:3306/shopspheredb?useSSL=false&serverTimezone=UTC
db.user=...
db.password=...
```
### 2.3 Run the application (Stage 1 testing)

Main class: `app.Main`

This class is used to test:

- DAO CRUD operations
- Predicate filtering
- JSON conversion
  
### Stage 2 Execution
### 2.4 Run the Server

Main class: `server.ServerMain`

Default port: `5000`

Expected output:
`Server listening on port 5000`

### 2.5 Run the Client

Main class: `client.ClientMain`

The client:

- Sends JSON requests to the server
- Receives JSON responses
- Prints responses to console

Run multiple clients to demonstrate multithreading.

## 3. DAO Layer Overview

The DAO layer separates database access from business logic. Each entity has a corresponding DAO interface and a JDBC implementation class.

- CustomerDao -> JdbcCustomerDao
- ProductDao -> JdbcProductDao
- OrderDao -> JbbcOrderDao
- OrderItemDao -> JbdcOrderItemDao
- ReviewDao -> JdbcReviewDao

The service layer depends only on the DAO interface, allowing the underlying database implementation to be changed without affecting the business logic.

All SQL statements use PreparedStatement to prevent SQL injection and follow the project requirements.

## 4. JSON Conversion

Jackson’s ObjectMapper is used to convert DTO objects to and from JSON.

Implemented methods:
```
customerToJson(Customer)
customerFromJson(String json)
customerListToJson(List)

productToJson(Product)
productFromJson(String json)
productListToJson(List)

ordersToJson(Orders order)
ordersFromJson(String json)
ordersListToJson(List<Orders> orders)

orderItemToJson(OrderItem item)
orderItemFromJson(String json)
orderItemListToJson(List<OrderItem> items)

reviewToJson(Review review)
reviewFromJson(String json)
reviewListToJson(List<Review> reviews)
```

Round-trip testing ensures that objects can be converted to JSON and reconstructed without data loss.

## 5. DTO Structure 

```
org
 ├── Client
 │     ClientMain.java
 │    
 ├── Server
 │     ClientHandler.java
 │     ServerMain.java
 │     
 ├── Interface
 │     ServerResponse.java
 │     ClientRequest.java
 │     ObjectMapperProvider.java
 │   
 ├── DTO
 │     Customer.java
 │     Product.java
 │     Orders.java
 │     OrderItem.java
 │     Review.java
 │
 ├── DAO
 │     CustomerDao.java
 │     ProductDao.java
 │     OrdersDao.java
 │     OrderItemDao.java
 │     ReviewDao.java
 │
 ├── DAO_JDBC
 │     JdbcCustomerDao.java
 │     JdbcProductDao.java
 │     JdbcOrdersDao.java
 │     JdbcOrderItemDao.java
 │     JdbcReviewDao.java
 │
 ├── Services
 │     CustomerService.java
 │     ProductService.java
 │     OrdersService.java
 │     OrderItemService.java
 │     ReviewService.java
 │
 ├── JsonUtil
 │     CustomerJsonUtil.java
 │     ProductJsonUtil.java
 │     OrdersJsonUtil.java
 │     OrderItemJsonUtil.java
 │     ReviewJsonUtil.java
 │
 └── DB
       DBConnection.java
```
## 6. Architecture Summary

### 6.1 N-tier overview
- Client (console application sending JSON requests)
- Server (socket listener + request handlers + multithreading using ExecutorService)
- DAO layer (interfaces + JDBC implementations)
- Database (MySQL)
  
### 6.2 Architecture diagram

- Path: `docs/architecture.md`
- Diagram format: Mermaid

## 7. Protocol Documentation (Stage 2)

## 7.1 Request Format
```
{
  "action": "getAll",
  "entity": "customer",
  "id": null,
  "jsonData": null
}
```

## 7.2 Request Fields
- action → operation to perform
(getAll, getById, add, update, delete)
- entity → target entity
(customer, product, orders, orderItem)
- id → used for:
    getById
    update
    delete
- jsonData → used for:
    add
    update


## 7.3 Response Format
```
{
  "status": "success",
  "message": "Operation successful",
  "data": {}
}
```
 
## 7.4 Response Fields
- status → "success" or "error"
- message → description of result
- data → returned object or list

  
## 7.5 Supported Operations
- getAll

Returns all records for an entity

- getById

Returns one record by ID

- add

Adds a new entity
Returns inserted object with generated ID

- update

Updates an existing entity
Returns updated object

- delete

Deletes a record by ID
Returns success/failure response

## 7.6 Example Response (Success)
```
{
  "status": "success",
  "message": "Customer retrieved successfully",
  "data": {
    "customerId": 1,
    "name": "Alice Murphy",
    "email": "alice@email.com",
    "phone": "0851111111"
  }
}
```
## 7.7 Example Response (Error)
```
{
  "status": "error",
  "message": "Customer not found",
  "data": null
}
```

## 5. Binary File Handling (Stage 3+)

### 5.1 What binary data represents in our domain
In this system, binary data represents files attached to:
  - Customer - (eg. profile pictures)
  - Product - (eg. product images)
  - Orders - (eg. receipts)
  - OrderItems - (eg. item specific attachments)
  - Review - (eg. supporting evidence)
    
These files are stored and transferred between client and server

### 5.2 Storage approach
DB table includes:
  - `file_data` (BLOB)
  - `file_name` (VARCHAR)
  - `content_type` (VARCHAR)
  - `file_size` (INT)
    
This schema is implemented across each table

### 5.3 Supported binary operations
| Operation | Request type | Notes |
| :- | :- | :- |
| Upload file | `insert` | Base64 encode bytes + include metadata |
| Upload file | `update` | Replaces existing file using Base64 data |
| Retrieve file | `download` | Base64 returned, client reconstructs file |
| Query metadata only | `getMetadata` | Must not fetch the BLOB payload |

---

## 6. Testing & Coverage

### 6.1 Running tests
- Command:
  - `mvn test`  
- Location:
  - `src/test/java/...`
- Tests structure:  
  Tests are organised by layer to match the project architecture
  - `org.DAO_JDBC` - Tests database operations (CRUD methods)
  - `org.JsonUtil` - Tests JSON serialisation and deserialisation (object <-> JSON round trip)
  - `org.Services` - Tests business logic and filtering using predicates

---

## 7. Design Patterns, Generics, Lambdas

### 7.1 Patterns used 
- Pattern 1: `DAO` - Separates database logic from business logic. All SQL queries are inside DAO classes
- Pattern 2: `Service Layer` - Contains business logic and keeps request handling code clean

### 7.2 Generics usage
- `ServerResponse<T>`
  - Used to wrap all server responses in a consistent structure
  - Allows different data types (Customer, Products, Orders, OrderItem, Review) without duplicating response classes
- Additional Generic Usage:
  - Java collections such as `List<OrderItem>` and `Optional<Orders>` use generics to ensure type safety when handling data

### 7.3 Functional interfaces / lambdas
- `Predicate<T>` filtering
  - Used in service methods (eg. `findOrderItemsByFilter`)- Allows flexible filtering logic without creating multiple methods

---
