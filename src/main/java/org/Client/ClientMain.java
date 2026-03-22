package org.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.DTO.Customer;
import org.DTO.Product;
import org.DTO.Orders;
import org.DTO.OrderItem;
import org.Interface.ClientRequest;
import org.Interface.ServerResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

//testing client
public class ClientMain
{
    //For Orders to use LocalDateTime
    private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    public static void main(String[] args) {

        ObjectMapper mapper = new ObjectMapper();
        try {
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


            //ORDERS
            //-------------------------------------------------------------
            //get all orders
            sendRequest(new ClientRequest("getAll", "orders", null, null));

            //get order by id
            sendRequest(new ClientRequest("getById", "orders", 1, null));

            //insert new order
            Orders newOrder = new Orders(1, java.time.LocalDateTime.now(), Orders.OrderStatus.NEW);
            String orderJson = MAPPER.writeValueAsString(newOrder);
            sendRequest(new ClientRequest("insert", "orders", null, orderJson));

            //update order
            Orders updatedOrder = new Orders(1, java.time.LocalDateTime.now(), Orders.OrderStatus.PAID);
            String updatedOrderJson = MAPPER.writeValueAsString(updatedOrder);
            sendRequest(new ClientRequest("update", "orders", 1, updatedOrderJson));

            //delete order
            sendRequest(new ClientRequest("delete", "orders", 1, null));

            // ORDER ITEMS
            //-------------------------------------------------------------
            //get all order items
            sendRequest(new ClientRequest("getAll", "orderitem", null, null));

            //get order item by id
            sendRequest(new ClientRequest("getById", "orderitem", 1, null));

            //insert order item
            OrderItem newItem = new OrderItem(1, 1, 2, 99.99);
            String itemJson = mapper.writeValueAsString(newItem);
            sendRequest(new ClientRequest("insert", "orderitem", null, itemJson));

            //update order item
            OrderItem updatedItem = new OrderItem(1, 1, 5, 89.99);
            String updatedItemJson = mapper.writeValueAsString(updatedItem);
            sendRequest(new ClientRequest("update", "orderitem", 1, updatedItemJson));

            //delete order item
            sendRequest(new ClientRequest("delete", "orderitem", 1, null));

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
    public static void sendRequest(ClientRequest request) {

        try (
                Socket socket = new Socket("localhost", 5000);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                )
        )
        {
            ObjectMapper mapper = new ObjectMapper();

            // converting  the request object into JSON
            String requestJson = mapper.writeValueAsString(request);

            System.out.println("\nSending Request:");
            System.out.println(requestJson);

            // Send the JSON request to the server
            out.println(requestJson);

            // read the JSON response from the server
            String responseJson = in.readLine();

            System.out.println("\nResponse from the Server:");
            System.out.println(responseJson);

        }
        catch (Exception e)
        {
            System.out.println("Client error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}