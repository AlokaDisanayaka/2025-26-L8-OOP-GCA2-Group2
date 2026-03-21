package org.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.DTO.Customer;
import org.DTO.Product;
import org.DTO.Orders;
import org.Interface.ClientRequest;
import org.Interface.ServerResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

//testing client
public class ClientMain
{
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
            String orderJson = mapper.writeValueAsString(newOrder);
            sendRequest(new ClientRequest("insert", "orders", null, orderJson));

            //update order
            Orders updatedOrder = new Orders(1, java.time.LocalDateTime.now(), Orders.OrderStatus.PAID);
            String updatedOrderJson = mapper.writeValueAsString(updatedOrder);
            sendRequest(new ClientRequest("update", "orders", 1, updatedOrderJson));

            //delete order
            sendRequest(new ClientRequest("delete", "orders", 1, null));

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