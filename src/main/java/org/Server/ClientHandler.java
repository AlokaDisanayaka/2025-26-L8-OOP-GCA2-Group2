package org.Server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.DAO.CustomerDao;
import org.DAO.ProductDao;
import org.DAO_JDBC.JdbcCustomerDao;
import org.DAO_JDBC.JdbcProductDao;
import org.DTO.Product;
import org.DTO.Customer;
import org.Services.CustomerService;
import org.Services.ProductService;
import org.Interface.ObjectMapperProvider;
import org.Interface.ClientRequest;
import org.Interface.ServerResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Optional;

 // Handles one connected client and runs on its own thread.

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final ObjectMapper mapper;

    //services used by the server
    private final CustomerService customerService;
    private final ProductService productService;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.mapper = ObjectMapperProvider.getMapper();


        //create Dao objects
        CustomerDao customerDao = new JdbcCustomerDao();
        ProductDao productDao = new JdbcProductDao();

        //create service objects
        this.customerService= new CustomerService(customerDao);
        this.productService=new ProductService(productDao);
    }
    @Override
    public void run() {

        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        )
        {

            // Read one line of JSON sent by the client
            String requestJson = in.readLine();

            if (requestJson == null || requestJson.trim().isEmpty()) {
                ServerResponse<String> response =
                        ServerResponse.error("Empty request received");

                out.println(mapper.writeValueAsString(response));
                return;
            }

            // Convert JSON text into a RequestMessage object
            ClientRequest request = mapper.readValue(requestJson, ClientRequest.class);

            // confirm that the server received the request
            String message = "Request received successfully. Action = " +
                    request.getAction() + ", Entity = " + request.getEntity();

            ServerResponse<String> response =
                    ServerResponse.success(message, null);

            // Send JSON response back to client
            out.println(mapper.writeValueAsString(response));

        }
        catch (Exception e)
        {
            // always return a structured error response
            try
            {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                ServerResponse<String> errorResponse =
                        ServerResponse.error("Server failed to process request");

                out.println(mapper.writeValueAsString(errorResponse));

            }
            catch (IOException ioException)
            {
                System.out.println("Error sending error response: " + ioException.getMessage());
            }

            System.out.println("Client handler error: " + e.getMessage());
            e.printStackTrace();

        }
        finally
        {
            // always close the socket when finished..
            try
            {
                clientSocket.close();
                System.out.println("Client disconnected");
            }
            catch (IOException e)
            {
                System.out.println("Error closing client socket: " + e.getMessage());
            }
        }
    }


     // the method handles one request and returns a JSON response string.
    private String handleRequest(ClientRequest request) throws Exception {

        String action = request.getAction();
        String entity = request.getEntity();


        // CUSTOMER REQUESTS
        //-----------------------------------------------------------------------
        if ("customer".equalsIgnoreCase(entity))
        {

            // Get all customers
            if ("getAll".equalsIgnoreCase(action))
            {
                List<Customer> customers = customerService.getAllCustomers();

                ServerResponse<List<Customer>> response = ServerResponse.success("Customers retrieved successfully", customers);

                return mapper.writeValueAsString(response);
            }

            // Get customer by ID
            if ("getById".equalsIgnoreCase(action))
            {

                if (request.getId() == null)
                {
                    ServerResponse<String> response = ServerResponse.error("Customer ID is required");
                    return mapper.writeValueAsString(response);
                }

                Optional<Customer> customer = customerService.getCustomerById(request.getId());

                if (customer.isPresent())
                {
                    ServerResponse<Customer> response = ServerResponse.success("Customer found", customer.get());
                    return mapper.writeValueAsString(response);
                }

                else
                {
                    ServerResponse<String> response = ServerResponse.error("Customer not found");
                    return mapper.writeValueAsString(response);
                }
            }
        }

        // PRODUCT REQUESTS
        // --------------------------------------------------------------------------------
        if ("product".equalsIgnoreCase(entity))
        {

            // Get all products
            if ("getAll".equalsIgnoreCase(action))
            {
                List<Product> products = productService.getAllProducts();

                ServerResponse<List<Product>> response = ServerResponse.success("Products retrieved successfully", products);

                return mapper.writeValueAsString(response);
            }

            // Get product by ID
            if ("getById".equalsIgnoreCase(action))
            {

                if (request.getId() == null)
                {
                    ServerResponse<String> response = ServerResponse.error("Product ID is required");
                    return mapper.writeValueAsString(response);
                }

                Optional<Product> product = productService.getProductById(request.getId());

                if (product.isPresent())
                {
                    ServerResponse<Product> response = ServerResponse.success("Product found", product.get());
                    return mapper.writeValueAsString(response);
                }
                else
                {
                    ServerResponse<String> response = ServerResponse.error("Product not found");
                    return mapper.writeValueAsString(response);
                }
            }
        }

        // If the request does not match any supported action/entity error message
        ServerResponse<String> response = ServerResponse.error("Unsupported action or entity");
        return mapper.writeValueAsString(response);
    }
}

