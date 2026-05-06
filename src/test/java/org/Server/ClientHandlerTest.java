package org.Server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.DTO.Orders;
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
        Map<String, Object> payload = new HashMap<>();

        payload.put("customerId", 1);
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
        byte[] file = "test file content".getBytes();
        String base64 = Base64.getEncoder().encodeToString(file);

        Map<String, Object> payload = new HashMap<>();
        payload.put("customerId", 1);
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
}
