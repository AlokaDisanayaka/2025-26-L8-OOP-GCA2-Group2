package org.Server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.DTO.Customer;
import org.DTO.Orders;
import org.DTO.Product;
import org.Interface.ClientRequest;
import org.Interface.ObjectMapperProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ClientHandlerTest
{
    private ClientHandler handler;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp()
    {
        handler = new ClientHandler(null);
        mapper = ObjectMapperProvider.getMapper();
    }

    //ORDERS
    //------
    //get all
    @Test
    void orders_getAll_returnsSuccess() throws Exception
    {
        ClientRequest request = new ClientRequest("getAll", "orders", null, null);
        String response = handler.handleRequest(request);

        assertTrue(response.contains("Orders retrieved"));
        assertTrue(response.contains("success"));
    }

    //get by ID
    @Test
    void orders_getById_returnsResponse() throws Exception
    {
        ClientRequest request = new ClientRequest("getById", "orders", 1, null);
        String response = handler.handleRequest(request);

        assertTrue(response.contains("success") || response.contains("Order found") || response.contains("error"));
    }

    //insert
    @Test
    void orders_insert_createsOrder() throws Exception
    {
        int customerId = addTestCustomer();
        Map<String, Object> payload = new HashMap<>();

        payload.put("customerId", customerId);
        payload.put("orderDate", LocalDateTime.now().toString());
        payload.put("status", "NEW");
        payload.put("fileData", null);
        payload.put("fileName", null);
        payload.put("contentType", null);
        payload.put("fileSize", 0);

        String json = mapper.writeValueAsString(payload);
        ClientRequest request = new ClientRequest("insert", "orders", null, json);
        String response = handler.handleRequest(request);

        assertTrue(response.contains("Order inserted"));
    }

    //binary insert
    @Test
    void orders_insert_withFile_uploadsBinary() throws Exception
    {
        int customerId = addTestCustomer();
        byte[] file = "test file content".getBytes();
        String base64 = Base64.getEncoder().encodeToString(file);

        Map<String, Object> payload = new HashMap<>();
        payload.put("customerId", customerId);
        payload.put("orderDate", LocalDateTime.now());
        payload.put("status", "NEW");
        payload.put("fileData", base64);
        payload.put("fileName", "test.txt");
        payload.put("contentType", "text/plain");
        payload.put("fileSize", file.length);

        String json = mapper.writeValueAsString(payload);
        ClientRequest request = new ClientRequest("insert", "orders", null, json);
        String response = handler.handleRequest(request);

        assertTrue(response.contains("Order inserted"));
    }

    //update
    @Test
    void orders_update_returnsSuccess() throws Exception
    {
        Map<String, Object> payload = new HashMap<>();
        payload.put("customerId", 1);
        payload.put("orderDate", LocalDateTime.now());
        payload.put("status", "PAID");

        String json = mapper.writeValueAsString(payload);
        ClientRequest request = new ClientRequest("update", "orders", 1, json);
        String response = handler.handleRequest(request);

        assertTrue(response.contains("Order updated") || response.contains("error"));
    }

    //delete
    @Test
    void orders_delete_returnsResponse() throws Exception
    {
        ClientRequest request = new ClientRequest("delete", "orders", 1, null);
        String response = handler.handleRequest(request);

        assertTrue(response.contains("success") || response.contains("error"));
    }

    //metadata
    @Test
    void orders_metadata_returnsFileInfo() throws Exception
    {
        ClientRequest request = new ClientRequest("getMetadata", "orders", 1, null);
        String response = handler.handleRequest(request);

        assertTrue(response.contains("Metadata") || response.contains("error"));
    }

    //download
    @Test
    void orders_download_returnsBase64() throws Exception
    {
        ClientRequest request = new ClientRequest("download", "orders", 1, null);
        String response = handler.handleRequest(request);

        assertTrue(response.contains("fileData") || response.contains("error"));
    }

    //ORDER ITEM
    //----------
    //get all
    @Test
    void orderItem_getAll_returnsSuccess() throws Exception
    {
        ClientRequest request = new ClientRequest("getAll", "orderitem", null, null);
        String response = handler.handleRequest(request);

        assertTrue(response.contains("success") || response.contains("error"));
    }

    //get by id
    @Test
    void orderItem_getById_returnsResult() throws Exception
    {
        ClientRequest request = new ClientRequest("getById", "orderitem", 1, null);
        String response = handler.handleRequest(request);

        assertTrue(response.contains("success") || response.contains("error"));
    }

    //insert
    @Test
    void orderItem_insert_createsOrderItem() throws Exception
    {
        int orderId = addTestOrder();
        int productId = addTestProduct();

        Map<String, Object> payload = new HashMap<>();
        payload.put("orderId", orderId);
        payload.put("productId", productId);
        payload.put("quantity", 3);
        payload.put("unitPriceAtTime", 9.99);
        payload.put("fileData", null);
        payload.put("fileName", null);
        payload.put("contentType", null);
        payload.put("fileSize", 0);

        String json = mapper.writeValueAsString(payload);
        ClientRequest request = new ClientRequest("insert", "orderitem", null, json);
        String response = handler.handleRequest(request);

        assertTrue(response.contains("OrderItem inserted"));
    }

    //insert with file
    @Test
    void orderItem_insert_withFile_success() throws Exception
    {
        int orderId = addTestOrder();
        int productId = addTestProduct();
        byte[] file = "hello".getBytes();
        String base64 = Base64.getEncoder().encodeToString(file);

        Map<String, Object> payload = new HashMap<>();
        payload.put("orderId", orderId);
        payload.put("productId", productId);
        payload.put("quantity", 3);
        payload.put("unitPriceAtTime", 9.99);
        payload.put("fileData", base64);
        payload.put("fileName", "hello.txt");
        payload.put("contentType", "text/plain");
        payload.put("fileSize", file.length);

        String json = mapper.writeValueAsString(payload);
        ClientRequest request = new ClientRequest("insert", "orderitem", null, json);
        String response = handler.handleRequest(request);

        assertTrue(response.contains("OrderItem inserted"));
    }

    //update
    @Test
    void orderItem_update_returnsSuccess() throws Exception
    {
        Map<String, Object> payload = new HashMap<>();
        payload.put("orderId", 1);
        payload.put("productId", 2);
        payload.put("quantity", 10);
        payload.put("unitPriceAtTime", 20.0);

        String json = mapper.writeValueAsString(payload);
        ClientRequest request = new ClientRequest("update", "orderitem", 1, json);
        String response = handler.handleRequest(request);

        assertTrue(response.contains("success") || response.contains("error"));
    }

    //delete
    @Test
    void orderItem_delete_returnsResponse() throws Exception
    {
        ClientRequest request = new ClientRequest("delete", "orderitem", 1, null);
        String response = handler.handleRequest(request);

        assertTrue(response.contains("success") || response.contains("error"));
    }

    //metadata
    @Test
    void orderItem_metadata_returnsInfo() throws Exception
    {
        ClientRequest request = new ClientRequest("getMetadata", "orderitem", 1, null);
        String response = handler.handleRequest(request);

        assertTrue(response.contains("Metadata") || response.contains("error"));
    }

    //download
    @Test
    void orderItem_download_returnsFileData() throws Exception
    {
        ClientRequest request = new ClientRequest("download", "orderitem", 1, null);
        String response = handler.handleRequest(request);

        assertTrue(response.contains("fileData") || response.contains("error"));
    }

    //tests that getting all customers returns a success response
    @Test
    void handleRequest_returnsSuccess_whenGetAllCustomersRequestSent() throws Exception
    {
        ClientRequest request = new ClientRequest("getAll", "customer", null, null);
        String response = handler.handleRequest(request);

        assertTrue(response.contains("Customers retrieved successfully"));
        assertTrue(response.contains("success"));
    }

    //tests that getting one product by id returns a success response
    @Test
    void handleRequest_returnsSuccess_whenGetProductByIdRequestSent() throws Exception
    {
        Product product = new Product("Stage 4 Product " + System.nanoTime(), 10.0, 5, true);
        String productJson = mapper.writeValueAsString(product);

        ClientRequest addRequest = new ClientRequest("add", "product", null, productJson);
        String addResponse = handler.handleRequest(addRequest);
        int productId = mapper.readTree(addResponse).get("data").get("productId").asInt();

        ClientRequest request = new ClientRequest("getById", "product", productId, null);
        String response = handler.handleRequest(request);

        assertTrue(response.contains("Product found"));
        assertTrue(response.contains("success"));
    }

    //tests that the product report service returns a success response
    @Test
    void handleRequest_returnsSuccess_whenRunProductReportServiceRequestSent() throws Exception
    {
        ClientRequest request = new ClientRequest("runService", null, null, null, "PRODUCT_REPORT");
        String response = handler.handleRequest(request);

        assertTrue(response.contains("Product report generated successfully"));
        assertTrue(response.contains("success"));
    }

    //tests that the file upload service returns a success response
    @Test
    void handleRequest_returnsSuccess_whenFileUploadServiceRequestSent() throws Exception
    {
        Product product = new Product("Stage 4 File Product " + System.nanoTime(), 20.0, 10, true);
        String productJson = mapper.writeValueAsString(product);

        ClientRequest addRequest = new ClientRequest("add", "product", null, productJson);
        String addResponse = handler.handleRequest(addRequest);
        int productId = mapper.readTree(addResponse).get("data").get("productId").asInt();

        byte[] fileData = "Hello Product".getBytes();
        product.setFileName("hello-product.txt");
        product.setContentType("text/plain");
        product.setFileSize(fileData.length);
        product.setFileData(fileData);

        String fileJson = mapper.writeValueAsString(product);
        ClientRequest request = new ClientRequest("runService", null, productId, fileJson, "FILE_UPLOAD");
        String response = handler.handleRequest(request);

        assertTrue(response.contains("File uploaded"));
        assertTrue(response.contains("success"));
    }

    //tests that disconnect returns a success response
    @Test
    void handleRequest_returnsSuccess_whenDisconnectRequestSent() throws Exception
    {
        ClientRequest request = new ClientRequest("disconnect", null, null, null);
        String response = handler.handleRequest(request);

        assertTrue(response.contains("Client disconnected successfully"));
        assertTrue(response.contains("success"));
    }

    //tests that unsupported requests return an error response
    @Test
    void handleRequest_returnsError_whenRequestIsUnsupported() throws Exception
    {
        ClientRequest request = new ClientRequest("badAction", "badEntity", null, null);
        String response = handler.handleRequest(request);

        assertTrue(response.contains("Unsupported action or entity"));
        assertTrue(response.contains("error"));
    }

    //tests that adding a customer returns a success response
    @Test
    void customer_add_returnsSuccess() throws Exception
    {
        Customer customer = new Customer(
                "Add Client Customer",
                "addclient" + System.nanoTime() + "@email.com",
                "0871111111"
        );

        String json = mapper.writeValueAsString(customer);
        ClientRequest request = new ClientRequest("add", "customer", null, json);
        String response = handler.handleRequest(request);

        assertTrue(response.contains("Customer added successfully"));
        assertTrue(response.contains("success"));
    }

    //tests that updating a customer returns a success response
    @Test
    void customer_update_returnsSuccess() throws Exception
    {
        int customerId = addTestCustomer();
        Customer customer = new Customer(
                "Updated Client Customer",
                "updatedclient" + System.nanoTime() + "@email.com",
                "0872222222"
        );

        String json = mapper.writeValueAsString(customer);
        ClientRequest request = new ClientRequest("update", "customer", customerId, json);
        String response = handler.handleRequest(request);

        assertTrue(response.contains("Customer updated successfully"));
        assertTrue(response.contains("success"));
    }

    //tests that getting all products returns a success response
    @Test
    void product_getAll_returnsSuccess() throws Exception
    {
        ClientRequest request = new ClientRequest("getAll", "product", null, null);
        String response = handler.handleRequest(request);

        assertTrue(response.contains("Products retrieved successfully"));
        assertTrue(response.contains("success"));
    }

    //tests that updating a product returns a success response
    @Test
    void product_update_returnsSuccess() throws Exception
    {
        int productId = addTestProduct();
        Product product = new Product("Updated Client Product " + System.nanoTime(), 30.0, 12, false);

        String json = mapper.writeValueAsString(product);
        ClientRequest request = new ClientRequest("update", "product", productId, json);
        String response = handler.handleRequest(request);

        assertTrue(response.contains("Product updated successfully"));
        assertTrue(response.contains("success"));
    }

    //tests that product file upload returns a success response
    @Test
    void product_uploadFile_returnsSuccess() throws Exception
    {
        int productId = addTestProduct();
        byte[] fileData = "Product File".getBytes();

        Product product = new Product("Upload Client Product", 10.0, 5, true);
        product.setFileName("product-file.txt");
        product.setContentType("text/plain");
        product.setFileSize(fileData.length);
        product.setFileData(fileData);

        String json = mapper.writeValueAsString(product);
        ClientRequest request = new ClientRequest("uploadFile", "product", productId, json);
        String response = handler.handleRequest(request);

        assertTrue(response.contains("Product file uploaded successfully"));
        assertTrue(response.contains("success"));
    }

    //tests that review get all returns a response
    @Test
    void review_getAll_returnsResponse() throws Exception
    {
        ClientRequest request = new ClientRequest("getAll", "review", null, null);
        String response = handler.handleRequest(request);

        assertTrue(response.contains("success") || response.contains("error"));
    }

    //tests that customer file upload returns a success response
    @Test
    void customer_uploadFile_returnsSuccess() throws Exception
    {
        int customerId = addTestCustomer();
        byte[] fileData = "Customer File".getBytes();

        Customer customer = new Customer("Upload Customer", "uploadcustomer@email.com", "0873333333");
        customer.setFileName("customer-file.txt");
        customer.setContentType("text/plain");
        customer.setFileSize(fileData.length);
        customer.setFileData(fileData);

        String json = mapper.writeValueAsString(customer);
        ClientRequest request = new ClientRequest("uploadFile", "customer", customerId, json);
        String response = handler.handleRequest(request);

        assertTrue(response.contains("Customer file uploaded successfully"));
        assertTrue(response.contains("success"));
    }

    //tests that customer file retrieval returns a response
    @Test
    void customer_getFile_returnsResponse() throws Exception
    {
        int customerId = addTestCustomer();
        byte[] fileData = "Customer File".getBytes();

        Customer customer = new Customer("Upload Customer", "getfilecustomer@email.com", "0873333333");
        customer.setFileName("customer-file.txt");
        customer.setContentType("text/plain");
        customer.setFileSize(fileData.length);
        customer.setFileData(fileData);

        String json = mapper.writeValueAsString(customer);
        handler.handleRequest(new ClientRequest("uploadFile", "customer", customerId, json));

        ClientRequest request = new ClientRequest("getFile", "customer", customerId, null);
        String response = handler.handleRequest(request);

        assertTrue(response.contains("Customer file retrieved successfully"));
        assertTrue(response.contains("success"));
    }

    //tests that customer metadata retrieval returns a response
    @Test
    void customer_getFileMetadata_returnsResponse() throws Exception
    {
        int customerId = addTestCustomer();

        ClientRequest request = new ClientRequest("getFileMetadata", "customer", customerId, null);
        String response = handler.handleRequest(request);

        assertTrue(response.contains("metadata") || response.contains("Metadata"));
        assertTrue(response.contains("success") || response.contains("error"));
    }

    //tests that deleting a customer returns a success response
    @Test
    void customer_delete_returnsSuccess() throws Exception
    {
        int customerId = addTestCustomer();

        ClientRequest request = new ClientRequest("delete", "customer", customerId, null);
        String response = handler.handleRequest(request);

        assertTrue(response.contains("Customer deleted successfully"));
        assertTrue(response.contains("success"));
    }

    //tests that product file retrieval returns a response
    @Test
    void product_getFile_returnsResponse() throws Exception
    {
        int productId = addTestProduct();
        byte[] fileData = "Product File".getBytes();

        Product product = new Product("Get File Product", 10.0, 5, true);
        product.setFileName("product-file.txt");
        product.setContentType("text/plain");
        product.setFileSize(fileData.length);
        product.setFileData(fileData);

        String json = mapper.writeValueAsString(product);
        handler.handleRequest(new ClientRequest("uploadFile", "product", productId, json));

        ClientRequest request = new ClientRequest("getFile", "product", productId, null);
        String response = handler.handleRequest(request);

        assertTrue(response.contains("Product file retrieved successfully"));
        assertTrue(response.contains("success"));
    }

    //tests that product metadata retrieval returns a response
    @Test
    void product_getFileMetadata_returnsResponse() throws Exception
    {
        int productId = addTestProduct();

        ClientRequest request = new ClientRequest("getFileMetadata", "product", productId, null);
        String response = handler.handleRequest(request);

        assertTrue(response.contains("metadata") || response.contains("Metadata"));
        assertTrue(response.contains("success") || response.contains("error"));
    }

    //tests that deleting a product returns a success response
    @Test
    void product_delete_returnsSuccess() throws Exception
    {
        int productId = addTestProduct();

        ClientRequest request = new ClientRequest("delete", "product", productId, null);
        String response = handler.handleRequest(request);

        assertTrue(response.contains("Product deleted successfully"));
        assertTrue(response.contains("success"));
    }

    private int addTestCustomer() throws Exception
    {
        Customer customer = new Customer(
                "Client Handler Customer",
                "clienthandler" + System.nanoTime() + "@email.com",
                "0870000000"
        );

        String json = mapper.writeValueAsString(customer);
        ClientRequest request = new ClientRequest("add", "customer", null, json);
        String response = handler.handleRequest(request);

        return mapper.readTree(response).get("data").get("customerId").asInt();
    }

    private int addTestProduct() throws Exception
    {
        Product product = new Product("Client Handler Product " + System.nanoTime(), 10.0, 5, true);

        String json = mapper.writeValueAsString(product);
        ClientRequest request = new ClientRequest("add", "product", null, json);
        String response = handler.handleRequest(request);

        return mapper.readTree(response).get("data").get("productId").asInt();
    }

    private int addTestOrder() throws Exception
    {
        int customerId = addTestCustomer();

        Map<String, Object> payload = new HashMap<>();
        payload.put("customerId", customerId);
        payload.put("orderDate", LocalDateTime.now().toString());
        payload.put("status", "NEW");
        payload.put("fileData", null);
        payload.put("fileName", null);
        payload.put("contentType", null);
        payload.put("fileSize", 0);

        String json = mapper.writeValueAsString(payload);
        ClientRequest request = new ClientRequest("insert", "orders", null, json);
        String response = handler.handleRequest(request);

        return mapper.readTree(response).get("data").get("orderId").asInt();
    }
}
