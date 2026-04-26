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


## 8. Design Patterns, Generics, Lambdas

### 8.1 Patterns used 
- Pattern 1: `DAO` - Separates database logic from business logic. All SQL queries are inside DAO classes
- Pattern 2: `Service Layer` - Contains business logic and keeps request handling code clean

### 8.2 Generics usage
- `ServerResponse<T>`
  - Used to wrap all server responses in a consistent structure
  - Allows different data types (Customer, Products, Orders, OrderItem, Review) without duplicating response classes
- Additional Generic Usage:
  - Java collections such as `List<OrderItem>` and `Optional<Orders>` use generics to ensure type safety when handling data

### 8.3 Functional interfaces / lambdas
- `Predicate<T>` filtering
  - Used in service methods (eg. `findOrderItemsByFilter`)- Allows flexible filtering logic without creating multiple methods

---

## 9. Stage 3 Implementation Details

Stage 3 extends the existing client-server system by adding binary file upload, binary file retrieval, metadata-only queries, structured disconnect requests, and JUnit 5 testing.

The implementation keeps all Stage 1 and Stage 2 functionality working while adding file support to the selected entity tables.

---

### 9.1 Stage 3 Features Implemented

| Feature | Description | Status |
| :- | :- | :- |
| F17 Binary Schema Extension | Customer and Product tables were extended with BLOB and metadata fields | Implemented |
| F18 Binary File Upload | Client reads a file from disk, sends it to server as Base64 JSON, and server stores it in database | Implemented |
| F19 Binary File Retrieval | Client requests file by ID, server returns Base64 data, client reconstructs file on disk | Implemented |
| F20 Metadata Query | Client can request filename, content type, and file size without downloading the BLOB | Implemented |
| F21 Disconnect / Exit | Client sends a structured disconnect request before closing socket | Implemented |
| F22 Unit Tests | JUnit 5 tests added for DAO, Service, and JSON behaviour | Implemented |

---

### 9.2 Binary Schema Extension

The following binary fields were added to the selected entity tables:

```sql
file_name VARCHAR(255),
content_type VARCHAR(100),
file_size INT,
file_data BLOB
```

### 9.3 DTO Binary Fields 
The following fields were added to the relevant DTO classes:
```
private String fileName;
private String contentType;
private int fileSize;
private byte[] fileData;
```
These fields allow the application to store:

- the original file name
- the content type
- the file size in bytes
- the actual binary file data

Jackson automatically serialises the byte[] field as Base64 when converting the DTO to JSON.

### 9.4 Binary Upload Flow 
The upload flow works as follows:

1. Client reads a file from disk using Files.readAllBytes(...)
2. File data is stored in the DTO as a byte[]
3. Jackson converts the DTO to JSON
4. The byte[] is automatically represented as Base64 in JSON
5. Client sends a structured ClientRequest
6. Server receives the request
7. Server converts the JSON back into a DTO
8. DAO stores the binary data using PreparedStatement.setBytes(...)
Example upload request:
```
{
  "action": "uploadFile",
  "entity": "product",
  "id": 1,
  "jsonData": "{...}"
}
```
### 9.5 Binary Retrieval Flow 
he retrieval flow works as follows:

1. Client sends a getFile request with the record ID
2. Server retrieves the record including the BLOB column
3. Server returns the DTO in a ServerResponse<T>
4. Jackson serialises the byte[] as Base64
5. Client receives the response
6. Client decodes the Base64 data
7. Client writes the bytes back to disk using Files.write(...)

Example retrieval request:
```
{
  "action": "getFile",
  "entity": "customer",
  "id": 1,
  "jsonData": null
}
```
Expected result:

```
File saved as: customer.jpg
```
### 9.6 Metadata Only Query 

The metadata query allows the client to request file information without downloading the full binary file.

This is important because it avoids unnecessary transfer of large BLOB data.

Example metadata request:

```
{
  "action": "getFileMetadata",
  "entity": "product",
  "id": 1,
  "jsonData": null
}
```
Example metadata response:
```
{
  "status": "success",
  "message": "Product metadata retrieved",
  "data": {
    "productId": 1,
    "productName": "Keyboard",
    "unitPrice": 80.0,
    "stockQty": 25,
    "fileName": "product.jpg",
    "contentType": "image/png",
    "fileSize": 6725,
    "fileData": null,
    "active": true
  }
}
```
The metadata DAO query does not select the file_data column.

### 9.7 Disconnect Request 

Stage 3 requires the client to send a structured disconnect request before closing the socket.

Example request:

```
{
  "action": "disconnect",
  "entity": "server",
  "id": null,
  "jsonData": null
}
```
Expected response:

```
{
  "status": "success",
  "message": "Client disconnected successfully",
  "data": null
}
```
The server logs the disconnect and releases the client thread cleanly

## 10 JUnit 5 Testing 
JUnit 5 tests were added to verify core behaviour required by Stage 3.

Tests are organised by layer:
```
src/test/java/org/DAO_JDBC
src/test/java/org/JsonUtil
src/test/java/org/Services
```
### 10.1 DAO tests

DAO tests verify database behaviour such as:

- reading records using getById
- inserting records and checking generated IDs
- returning empty Optional values when records do not exist

Example DAO test names:
```
getCustomerById_returnsEmptyOptional_whenIdDoesNotExist
insertCustomer_returnsGeneratedId_whenSuccessful
getProductById_returnsProduct_whenIdExists
insertProduct_returnsGeneratedId_whenSuccessful
getOrderById_returnsEmpty_whenIdDoesNotExist()
getOrdersByCustomerId_returnsMatchingOrders()
void getOrderItemById_returnsEmptyOptional_whenIdDoesNotExist()
updateOrderItem_returnsUpdatedItem_whenSuccessful()
getReviewById_returnsEmpty_whenIdDoesNotExist()
updateReview_returnsUpdatedReview()
```
### 10.2 JSON tests

JSON tests verify object serialisation and deserialisation.

They confirm that an object can be converted:

object -> JSON -> object

without losing field values.

### 10.3 Service Tests 
Service tests verify that the service layer correctly calls DAO functionality.

Example service test names:
```
getCustomerById_returnsCustomer_whenExists
addCustomer_returnsCustomerWithGeneratedId
```
All tests use @BeforeEach to create fresh DAO or Service objects before each test.

### 10.4 Running the Test Suite
The test suite verifies:

- DAO read operations
= inserts with generated IDs
= JSON round-trip conversion
= service layer behaviour

The tests do not depend on execution order.

### 10.5 Stage 3 Demonstration Checklist

Before demonstration, the following should be verified:

- Server starts successfully on port 5000
- Client can connect to server
- Customer file can be uploaded
- Customer file metadata can be retrieved without downloading the BLOB
- Customer file can be downloaded and reconstructed
- Product file can be uploaded
- Product file metadata can be retrieved without downloading the BLOB
- Product file can be downloaded and reconstructed
- Client sends disconnect request
- Server logs disconnect cleanly
- JUnit tests pass using mvn test



---
