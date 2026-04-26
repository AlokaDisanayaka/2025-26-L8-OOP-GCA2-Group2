package org.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.DTO.*;
import org.Interface.ClientRequest;
import org.Interface.ObjectMapperProvider;
import org.Interface.ServerResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import java.nio.file.Paths;

//testing client
public class ClientMain
{
    public static void main(String[] args)
    {
        ObjectMapper mapper = ObjectMapperProvider.getMapper();
        try
        {
            // Test different requests one by one
            //CUSTOMER
            //-------------------------------------------------------------

            //testing get all customers
            sendRequest(new ClientRequest("getAll", "customer", null, null));
            //testing get customers by id
            sendRequest(new ClientRequest("getById", "customer", 1, null));
            //testing add customer
            Customer newCustomer = new Customer("Server Test Customer", "servertest@email.com", "0871111111");
            String customerJson = mapper.writeValueAsString(newCustomer);
            sendRequest(new ClientRequest("add", "customer", null, customerJson));
            //testing update customer
            Customer updatedCustomer = new Customer("Updated Server Customer", "updatedserver@email.com", "0899999999");
            String updatedCustomerJson = mapper.writeValueAsString(updatedCustomer);
            sendRequest(new ClientRequest("update", "customer", 1, updatedCustomerJson));
            //testing delete customer by id
            sendRequest(new ClientRequest("delete", "customer", 21, null));

            //testing upload customer file
            uploadCustomerFile(1, "test_files/customer.jpg");
            //testing metadata
            getCustomerFileMetadata(1);
            //testing download file
            getCustomerFile(1);


            //PRODUCT
            //-------------------------------------------------------------

            //testing get all products
            sendRequest(new ClientRequest("getAll", "product", null, null));
            //testing get product by id
            sendRequest(new ClientRequest("getById", "product", 1, null));
            //testing add product
            Product newProduct = new Product("Server Test Product", 123.45, 8, true);
            String productJson = mapper.writeValueAsString(newProduct);
            sendRequest(new ClientRequest("add", "product", null, productJson));
            //testing upadate product by id
            Product updatedProduct = new Product("Updated Server Product", 555.55, 20, true);
            String updatedProductJson = mapper.writeValueAsString(updatedProduct);
            sendRequest(new ClientRequest("update", "product", 1, updatedProductJson));
            //testing delete product by id
            sendRequest(new ClientRequest("delete", "product", 21, null));
            //testing upload product file
            uploadProductFile(1, "test_files/product.jpg");
            //testing metadata
            getProductFileMetadata(1);
            //testing download file
            getProductFile(1);

            //ORDERS
            //-------------------------------------------------------------
            //get all orders
            sendRequest(new ClientRequest("getAll", "orders", null, null));

            //get order by id
            sendRequest(new ClientRequest("getById", "orders", 1, null));

            //insert new order
            byte[] orderFile = readFile("test_files/sample.png");
            String orderBase64 = Base64.getEncoder().encodeToString(orderFile);
            Map<String, Object> orderPayload = new HashMap<>();
            orderPayload.put("customerId", 1);
            orderPayload.put("orderDate", java.time.LocalDateTime.now());
            orderPayload.put("status", "NEW");
            orderPayload.put("fileData", orderBase64);
            orderPayload.put("fileName", "sample.png");
            orderPayload.put("contentType", "image/png");
            orderPayload.put("fileSize", orderFile.length);
            String orderJson = mapper.writeValueAsString(orderPayload);
            sendRequest(new ClientRequest("insert", "orders", null, orderJson));

            //update order
            byte[] updatedOrderFile = readFile("test_files/sample.png");
            String updatedOrderBase64 = Base64.getEncoder().encodeToString(updatedOrderFile);
            Map<String, Object> orderUpdatePayload = new HashMap<>();
            orderUpdatePayload.put("customerId", 1);
            orderUpdatePayload.put("orderDate", java.time.LocalDateTime.now());
            orderUpdatePayload.put("status", "PAID");
            orderUpdatePayload.put("fileData", updatedOrderBase64);
            orderUpdatePayload.put("fileName", "sample.png");
            orderUpdatePayload.put("contentType", "image/png");
            orderUpdatePayload.put("fileSize", updatedOrderFile.length);
            String updatedOrderJson = mapper.writeValueAsString(orderUpdatePayload);
            sendRequest(new ClientRequest("update", "orders", 1, updatedOrderJson));

            //metadata test for orders
            sendRequest(new ClientRequest("getMetadata", "orders", 1, null));

            //download test
            sendRequest(new ClientRequest("download", "orders", 1, null));

            //delete order
            sendRequest(new ClientRequest("delete", "orders", 1, null)); //will fail because order item rows referenced the orders row via a foreign key (MySQL prevented deletion)

            // ORDER ITEMS
            //-------------------------------------------------------------
            //get all order items
            sendRequest(new ClientRequest("getAll", "orderitem", null, null));

            //get order item by id
            sendRequest(new ClientRequest("getById", "orderitem", 1, null));

            //insert order item
            byte[] fileBytes = readFile("test_files/sample.png");
            OrderItem newItem = new OrderItem(1, 1, 2, 99.99);
            String base64File = Base64.getEncoder().encodeToString(fileBytes);
            Map<String, Object> payload = new HashMap<>();
            payload.put("orderId", newItem.getOrderId());
            payload.put("productId", newItem.getProductId());
            payload.put("quantity", newItem.getQuantity());
            payload.put("unitPriceAtTime", newItem.getUnitPriceAtTime());
            payload.put("fileData", base64File);
            payload.put("fileName", "sample.png");
            payload.put("contentType", "image/png");
            payload.put("fileSize", fileBytes.length);
            String itemJson = mapper.writeValueAsString(payload);
            sendRequest(new ClientRequest("insert", "orderitem", null, itemJson));

            //update order item
            OrderItem updatedItem = new OrderItem(1, 1, 5, 89.99);
            updatedItem.setFileData(fileBytes);
            updatedItem.setFileName("sample.png");
            updatedItem.setContentType("image/png");
            updatedItem.setFileSize(fileBytes.length);
            String base64FileUpdate = Base64.getEncoder().encodeToString(fileBytes);
            Map<String, Object> payloadUpdate = new HashMap<>();
            payloadUpdate.put("orderId", updatedItem.getOrderId());
            payloadUpdate.put("productId", updatedItem.getProductId());
            payloadUpdate.put("quantity", updatedItem.getQuantity());
            payloadUpdate.put("unitPriceAtTime", updatedItem.getUnitPriceAtTime());
            payloadUpdate.put("fileData", base64FileUpdate);
            payloadUpdate.put("fileName", "sample.png");
            payloadUpdate.put("contentType", "image/png");
            payloadUpdate.put("fileSize", fileBytes.length);
            String updatedItemJson = mapper.writeValueAsString(payloadUpdate);
            sendRequest(new ClientRequest("update", "orderitem", 1, updatedItemJson));

            //metadata test
            sendRequest(new ClientRequest("getMetadata", "orderitem", 1, null));

            //download test
            sendRequest(new ClientRequest("download", "orderitem", 1, null));

            //delete order item
            sendRequest(new ClientRequest("delete", "orderitem", 1, null));


            // REVIEW
            //-------------------------------------------------------------
            // testing get all reviews
            sendRequest(new ClientRequest("getAll", "review", null, null));

            // testing get review by id
            sendRequest(new ClientRequest("getById", "review", 1, null));
            // testing add review
            Review newReview = new Review(
                    1, // customerId
                    1, // productId
                    5, // rating
                    "Server test review",
                    new java.sql.Timestamp(System.currentTimeMillis())
            );
            String reviewJson = mapper.writeValueAsString(newReview);
            sendRequest(new ClientRequest("insert", "review", null, reviewJson));

            // testing update review
            Review updatedReview = new Review(
                    1, // customerId
                    1, // productId
                    4, // rating
                    "Updated server review",
                    new java.sql.Timestamp(System.currentTimeMillis())
            );
            String updatedReviewJson = mapper.writeValueAsString(updatedReview);
            sendRequest(new ClientRequest("update", "review", 1, updatedReviewJson));

            // testing delete review by id
            sendRequest(new ClientRequest("delete", "review", 1, null));

            // testing filter reviews by rating
            sendRequest(new ClientRequest("filterByRating", "review", 5, null));


          disconnect();
        }
        catch (Exception e)
        {
            System.out.println("Client setup error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * the method sendRequest=>
     * Opens a connection to the server,
     * sends one request,
     * prints the response,
     * then closes the connection.
     * .....................................
     */
    public static void sendRequest(ClientRequest request)
    {
        try (
                Socket socket = new Socket("localhost", 5000);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        )
        {
            ObjectMapper mapper = ObjectMapperProvider.getMapper();

            // converting  the request object into JSON
            String requestJson = mapper.writeValueAsString(request);

            System.out.println("\nSending Request:");
            System.out.println(requestJson);

            // Send the JSON request to the server
            out.println(requestJson);

            // read the JSON response from the server
            String responseJson = in.readLine();

            ServerResponse response = mapper.readValue(responseJson, ServerResponse.class);

            System.out.println("\nResponse from the Server:");
            System.out.println(responseJson);


            //file download handling
            if (response.getData() instanceof LinkedHashMap data)
            {
                Object fileDataObj = data.get("fileData");

                if (fileDataObj != null)
                {
                    String base64 = (String) fileDataObj;
                    String fileName = (String) data.get("fileName");

                    byte[] decoded = Base64.getDecoder().decode(base64);

                    java.nio.file.Path path = java.nio.file.Path.of(fileName);

                    if (java.nio.file.Files.exists(path))
                    {
                        path = java.nio.file.Path.of("downloaded_" + System.currentTimeMillis() + "_" + fileName);
                    }

                    java.nio.file.Files.write(path, decoded);

                    System.out.println("File saved as: " + path);
                }
            }

        }
        catch (Exception e)
        {
            System.out.println("Client error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static byte[] readFile(String filePath) throws Exception
    {
        return Files.readAllBytes(Path.of(filePath));
    }

    //customer file upload
    public static void uploadCustomerFile(int customerId, String filePath)
    {
        try
        {
            ObjectMapper mapper = ObjectMapperProvider.getMapper();

            //  Read file from disk into byte[]
            Path path = Paths.get(filePath);
            byte[] fileBytes = Files.readAllBytes(path);

            //  Create a Customer object ONLY for file data
            Customer customer = new Customer();

            // Temporary required fields so DTO validation passes during file upload
            customer.setName("Temp Customer");
            customer.setEmail("temp@email.com");
            customer.setPhone("0000000000");

            // Store metadata
            customer.setFileName(path.getFileName().toString());
            customer.setContentType("image/png"); // you can change if needed
            customer.setFileSize(fileBytes.length);

            // Store actual binary file
            customer.setFileData(fileBytes);

            // Convert Customer object to JSON
            String jsonData = mapper.writeValueAsString(customer);

            // Send request to server
            sendRequest(new ClientRequest(
                    "uploadFile",
                    "customer",
                    customerId,
                    jsonData
            ));
        }
        catch (Exception e)
        {
            System.out.println("Error uploading customer file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    //product file upload
    public static void uploadProductFile(int productId, String filePath)
    {
        try
        {
            ObjectMapper mapper = ObjectMapperProvider.getMapper();

            Path path = Paths.get(filePath);
            byte[] fileBytes = Files.readAllBytes(path);

            Product product = new Product();

            // Temporary required fields so DTO validation passes during file upload
            product.setProductName("Temp Product");
            product.setUnitPrice(1.0);
            product.setStockQty(1);
            product.setActive(true);

            product.setFileName(path.getFileName().toString());
            product.setContentType("image/png");
            product.setFileSize(fileBytes.length);
            product.setFileData(fileBytes);

            String jsonData = mapper.writeValueAsString(product);

            sendRequest(new ClientRequest(
                    "uploadFile",
                    "product",
                    productId,
                    jsonData
            ));
        }
        catch (Exception e)
        {
            System.out.println("Error uploading product file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    //get customer file
    public static void getCustomerFile(int customerId)
    {
        // This will trigger file download automatically inside sendRequest()
        sendRequest(new ClientRequest(
                "getFile",
                "customer",
                customerId,
                null
        ));
    }
    //get product file
    public static void getProductFile(int productId)
    {
        sendRequest(new ClientRequest(
                "getFile",
                "product",
                productId,
                null
        ));
    }
    //get metadata only
    public static void getCustomerFileMetadata(int customerId)
    {
        sendRequest(new ClientRequest(
                "getFileMetadata",
                "customer",
                customerId,
                null
        ));
    }

    public static void getProductFileMetadata(int productId)
    {
        sendRequest(new ClientRequest(
                "getFileMetadata",
                "product",
                productId,
                null
        ));
    }//disconnect
    public static void disconnect()
    {
        sendRequest(new ClientRequest(
                "disconnect",
                "server",
                null,
                null
        ));
    }






}
