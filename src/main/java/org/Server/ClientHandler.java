package org.Server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.DAO.CustomerDao;
import org.DAO.ProductDao;
import org.DAO.ReviewDao;
import org.DAO.OrdersDao;
import org.DAO.OrderItemDao;
import org.DAO_JDBC.JdbcCustomerDao;
import org.DAO_JDBC.JdbcProductDao;
import org.DAO_JDBC.JdbcReviewDao;
import org.DAO_JDBC.JdbcOrdersDao;
import org.DAO_JDBC.JdbcOrderItemDao;
import org.DTO.Product;
import org.DTO.Customer;
import org.DTO.Review;
import org.DTO.Orders;
import org.DTO.OrderItem;
import org.Services.CustomerService;
import org.Services.ProductService;
import org.Services.ReviewService;
import org.Services.OrdersService;
import org.Services.OrderItemService;
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

public class ClientHandler implements Runnable
{
    private final Socket clientSocket;
    private final ObjectMapper mapper;

    //services used by the server
    private final CustomerService customerService;
    private final ProductService productService;
    private final ReviewService reviewService;
    private final OrdersService ordersService;
    private final OrderItemService orderItemService;

    public ClientHandler(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
        this.mapper = ObjectMapperProvider.getMapper();

        //create Dao objects
        CustomerDao customerDao = new JdbcCustomerDao();
        ProductDao productDao = new JdbcProductDao();
        ReviewDao reviewDao = new JdbcReviewDao();
        OrdersDao ordersDao = new JdbcOrdersDao();
        OrderItemDao orderItemDao = new JdbcOrderItemDao();

        //create service objects
        this.customerService= new CustomerService(customerDao);
        this.productService=new ProductService(productDao);
        this.reviewService = new ReviewService(reviewDao);
        this.ordersService = new OrdersService(ordersDao);
        this.orderItemService = new OrderItemService(orderItemDao);
    }
    @Override
    public void run()
    {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        )
        {
            // Read one line of JSON sent by the client
            String requestJson = in.readLine();

            if (requestJson == null || requestJson.trim().isEmpty())
            {
                ServerResponse<String> response = ServerResponse.error("Empty request received");

                out.println(mapper.writeValueAsString(response));
                return;
            }

            // Convert JSON text into a ClientRequest object
            ClientRequest request = mapper.readValue(requestJson, ClientRequest.class);

            // Send request to the request handler
            String responseJson = handleRequest(request);

            // Send JSON response back to client
            out.println(responseJson);

        }
        catch (Exception e)
        {
            try
            {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                // Clean and clear error message.....
                ServerResponse<String> errorResponse = ServerResponse.error("Server error: " + e.getMessage());

                out.println(mapper.writeValueAsString(errorResponse));

            }
            catch (IOException ioException)
            {
                System.out.println("Error sending error response: " + ioException.getMessage());
            }

            // full error only on server side
            System.out.println("Server exception:");
            e.printStackTrace();

        }
        finally
        {
            // always close the socket when finished
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
        try
        {
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

                // Add new customer
                if ("add".equalsIgnoreCase(action))
                {

                    // Check whwther  jsonData was sent
                    if (request.getJsonData() == null || request.getJsonData().trim().isEmpty())
                    {
                        ServerResponse<String> response = ServerResponse.error("Customer JSON data is required");
                        return mapper.writeValueAsString(response);
                    }

                    // Convert jsonData into a Customer object
                    Customer customer = mapper.readValue(request.getJsonData(), Customer.class);

                    // Insert customer using service
                    Customer insertedCustomer = customerService.addCustomer(customer);

                    // Return inserted customer (with the generated ID)
                    ServerResponse<Customer> response = ServerResponse.success("Customer added successfully", insertedCustomer);

                    return mapper.writeValueAsString(response);
                }

                // Delete customer by ID
                if ("delete".equalsIgnoreCase(action))
                {

                    // Check if an id was provided
                    if (request.getId() == null)
                    {
                        ServerResponse<String> response = ServerResponse.error("Customer ID is required for delete");
                        return mapper.writeValueAsString(response);
                    }

                    // Call service to delete customer
                    boolean deleted = customerService.deleteCustomer(request.getId());

                    if (deleted)
                    {
                        ServerResponse<Boolean> response = ServerResponse.success("Customer deleted successfully", true);
                        return mapper.writeValueAsString(response);
                    }
                    else
                    {
                        ServerResponse<Boolean> response = ServerResponse.error("Customer not found or could not be deleted");
                        return mapper.writeValueAsString(response);
                    }
                }

                // Update customer by ID
                if ("update".equalsIgnoreCase(action))
                {

                    // check whether an ID was provided
                    if (request.getId() == null)
                    {
                        ServerResponse<String> response = ServerResponse.error("Customer ID is required for update");
                        return mapper.writeValueAsString(response);
                    }

                    // Check that updated customer JSON was provided
                    if (request.getJsonData() == null || request.getJsonData().trim().isEmpty())
                    {
                        ServerResponse<String> response = ServerResponse.error("Updated customer JSON data is required");
                        return mapper.writeValueAsString(response);
                    }

                    // Convert jsonData into a Customer object
                    Customer customer = mapper.readValue(request.getJsonData(), Customer.class);

                    // Update the customer using service layer
                    Customer updatedCustomer = customerService.updateCustomer(request.getId(), customer);

                    // Return updated customer
                    ServerResponse<Customer> response = ServerResponse.success("Customer updated successfully", updatedCustomer);

                    return mapper.writeValueAsString(response);
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

                // Add new product
                if ("add".equalsIgnoreCase(action))
                {

                    // Check that jsonData was sent
                    if (request.getJsonData() == null || request.getJsonData().trim().isEmpty())
                    {
                        ServerResponse<String> response = ServerResponse.error("Product JSON data is required");
                        return mapper.writeValueAsString(response);
                    }

                    // Convert jsonData into a Product object
                    Product product = mapper.readValue(request.getJsonData(), Product.class);

                    // Insert product using service
                    Product insertedProduct = productService.addProduct(product);

                    // Return inserted product (with the generated ID)
                    ServerResponse<Product> response = ServerResponse.success("Product added successfully", insertedProduct);

                    return mapper.writeValueAsString(response);
                }


                // Delete product by ID x
                if ("delete".equalsIgnoreCase(action))
                {

                    // Check whether an ID was provided
                    if (request.getId() == null)
                    {
                        ServerResponse<String> response = ServerResponse.error("Product ID is required for delete");
                        return mapper.writeValueAsString(response);
                    }

                    // Call service to delete product
                    boolean deleted = productService.deleteProduct(request.getId());

                    if (deleted)
                    {
                        ServerResponse<Boolean> response = ServerResponse.success("Product deleted successfully", true);
                        return mapper.writeValueAsString(response);
                    }
                    else
                    {
                        ServerResponse<Boolean> response = ServerResponse.error("Product not found or could not be deleted");
                        return mapper.writeValueAsString(response);
                    }
                }

                // Update product by ID
                if ("update".equalsIgnoreCase(action))
                {

                    // Check that an ID was provided
                    if (request.getId() == null)
                    {
                        ServerResponse<String> response = ServerResponse.error("Product ID is required for update");
                        return mapper.writeValueAsString(response);
                    }

                    // Check that updated product JSON was provided
                    if (request.getJsonData() == null || request.getJsonData().trim().isEmpty())
                    {
                        ServerResponse<String> response = ServerResponse.error("Updated product JSON data is required");
                        return mapper.writeValueAsString(response);
                    }

                    // Convert jsonData into a Product object
                    Product product = mapper.readValue(request.getJsonData(), Product.class);

                    // Update the product using service
                    Product updatedProduct = productService.updateProduct(request.getId(), product);

                    // Return updated product
                    ServerResponse<Product> response = ServerResponse.success("Product updated successfully", updatedProduct);

                    return mapper.writeValueAsString(response);
                }
            }
            // REVIEW REQUESTS

            if ("review".equalsIgnoreCase(entity)) {

                // Get all reviews - MY PART ADDED
                if ("getAll".equalsIgnoreCase(action)) {
                    List<Review> reviews = reviewService.getallReviews();

                    ServerResponse<List<Review>> response =
                            ServerResponse.success("Reviews retrieved successfully", reviews);

                    return mapper.writeValueAsString(response);
                }

                // Get review by ID
                if ("getById".equalsIgnoreCase(action)) {
                    if (request.getId() == null) {
                        ServerResponse<String> response = ServerResponse.error("Review ID is required");
                        return mapper.writeValueAsString(response);
                    }

                    Optional<Review> review = reviewService.getreviewbyid(request.getId());

                    if (review.isPresent()) {
                        ServerResponse<Review> response =
                                ServerResponse.success("Review found", review.get());
                        return mapper.writeValueAsString(response);
                    } else {
                        ServerResponse<String> response = ServerResponse.error("Review not found");
                        return mapper.writeValueAsString(response);
                    }
                }

                // Insert review
                if ("insert".equalsIgnoreCase(action)) {
                    if (request.getJsonData() == null || request.getJsonData().trim().isEmpty()) {
                        ServerResponse<String> response =
                                ServerResponse.error("Review JSON data is required");
                        return mapper.writeValueAsString(response);
                    }

                    Review review = mapper.readValue(request.getJsonData(), Review.class);
                    Review savedReview = reviewService.insertReview(review);

                    ServerResponse<Review> response =
                            ServerResponse.success("Review inserted successfully", savedReview);

                    return mapper.writeValueAsString(response);
                }

                // Update review
                if ("update".equalsIgnoreCase(action)) {
                    if (request.getId() == null) {
                        ServerResponse<String> response =
                                ServerResponse.error("Review ID is required for update");
                        return mapper.writeValueAsString(response);
                    }

                    if (request.getJsonData() == null || request.getJsonData().trim().isEmpty()) {
                        ServerResponse<String> response =
                                ServerResponse.error("Review JSON data is required for update");
                        return mapper.writeValueAsString(response);
                    }

                    Review review = mapper.readValue(request.getJsonData(), Review.class);
                    Review updatedReview = reviewService.updateReview(request.getId(), review);

                    ServerResponse<Review> response =
                            ServerResponse.success("Review updated successfully", updatedReview);

                    return mapper.writeValueAsString(response);
                }

                // Delete review
                if ("delete".equalsIgnoreCase(action)) {
                    if (request.getId() == null) {
                        ServerResponse<String> response =
                                ServerResponse.error("Review ID is required for delete");
                        return mapper.writeValueAsString(response);
                    }

                    boolean deleted = reviewService.deleteReview(request.getId());

                    if (deleted) {
                        ServerResponse<String> response =
                                ServerResponse.success("Review deleted successfully", null);
                        return mapper.writeValueAsString(response);
                    } else {
                        ServerResponse<String> response =
                                ServerResponse.error("Review not found or could not be deleted");
                        return mapper.writeValueAsString(response);
                    }
                }
            }

            //ORDERS REQUEST
            //-----------------------------------------------------------------------
            if ("orders".equalsIgnoreCase(entity))
            {
                //Get all orders
                if ("getAll".equalsIgnoreCase(action))
                {
                    List<Orders> orders = ordersService.getAllOrders();

                    ServerResponse<List<Orders>> response =
                            ServerResponse.success("Orders retrieved successfully", orders);

                    return mapper.writeValueAsString(response);
                }

                //Get order by ID
                if ("getById".equalsIgnoreCase(action))
                {
                    if (request.getId() == null)
                    {
                        return mapper.writeValueAsString(ServerResponse.error("Order ID is required"));
                    }

                    Optional<Orders> order = ordersService.getOrderById(request.getId());

                    if (order.isPresent())
                    {
                        return mapper.writeValueAsString(ServerResponse.success("Order found", order.get()));
                    }
                    else
                    {
                        return mapper.writeValueAsString(ServerResponse.error("Order not found"));
                    }
                }

                //Insert order
                if ("insert".equalsIgnoreCase(action))
                {
                    if (request.getJsonData() == null || request.getJsonData().isEmpty())
                    {
                        return mapper.writeValueAsString(ServerResponse.error("Order JSON data is required"));
                    }

                    Orders order = mapper.readValue(request.getJsonData(), Orders.class);
                    Orders savedOrder = ordersService.addOrder(order);

                    return mapper.writeValueAsString(ServerResponse.success("Order inserted successfully", savedOrder));
                }

                //Update order
                if ("update".equalsIgnoreCase(action))
                {
                    if (request.getId() == null)
                    {
                        return mapper.writeValueAsString(ServerResponse.error("Order ID is required for update"));
                    }

                    Orders order = mapper.readValue(request.getJsonData(), Orders.class);
                    Orders updated = ordersService.updateOrder(request.getId(), order);

                    return mapper.writeValueAsString(ServerResponse.success("Order updated successfully", updated));
                }

                //Delete order
                if ("delete".equalsIgnoreCase(action))
                {
                    if (request.getId() == null)
                    {
                        return mapper.writeValueAsString(ServerResponse.error("Order ID is required for delete"));
                    }

                    boolean deleted = ordersService.deleteOrder(request.getId());

                    if (deleted)
                    {
                        return mapper.writeValueAsString(ServerResponse.success("Order deleted successfully", null));
                    }
                    else
                    {
                        return mapper.writeValueAsString(ServerResponse.error("Order not found"));
                    }
                }
            }

            //ORDER ITEM REQUESTS
            //-----------------------------------------------------------------------
            if ("orderitem".equalsIgnoreCase(entity))
            {
                //Get all items
                if ("getAll".equalsIgnoreCase(action))
                {
                    List<OrderItem> items = orderItemService.getAllOrderItems();

                    return mapper.writeValueAsString(ServerResponse.success("Order items retrieved", items));
                }

                //Get by ID
                if ("getById".equalsIgnoreCase(action))
                {
                    if (request.getId() == null)
                    {
                        return mapper.writeValueAsString(ServerResponse.error("OrderItem ID required"));
                    }

                    Optional<OrderItem> item = orderItemService.getOrderItemById(request.getId());

                    if (item.isPresent())
                    {
                        return mapper.writeValueAsString(ServerResponse.success("OrderItem found", item.get()));
                    }
                    else
                    {
                        return mapper.writeValueAsString(ServerResponse.error("OrderItem not found"));
                    }
                }

                //Insert
                if ("insert".equalsIgnoreCase(action))
                {
                    OrderItem item = mapper.readValue(request.getJsonData(), OrderItem.class);
                    OrderItem saved = orderItemService.addOrderItem(item);

                    return mapper.writeValueAsString(ServerResponse.success("OrderItem inserted", saved));
                }

                //Update
                if ("update".equalsIgnoreCase(action))
                {
                    OrderItem item = mapper.readValue(request.getJsonData(), OrderItem.class);
                    OrderItem updated = orderItemService.updateOrderItem(request.getId(), item);

                    return mapper.writeValueAsString(ServerResponse.success("OrderItem updated", updated));
                }

                //Delete
                if ("delete".equalsIgnoreCase(action))
                {
                    boolean deleted = orderItemService.deleteOrderItem(request.getId());

                    if (deleted)
                    {
                        return mapper.writeValueAsString(ServerResponse.success("OrderItem deleted", null));
                    }
                    else
                    {
                        return mapper.writeValueAsString(ServerResponse.error("OrderItem not found"));
                    }
                }
            }

            // If the request does not match any supported action/entity error message
            ServerResponse<String> response = ServerResponse.error("Unsupported action or entity");
            return mapper.writeValueAsString(response);
        }
        catch (Exception e)
        {

            // Return structured error instead of crashing
            ServerResponse<String> response = ServerResponse.error("Failed to process request: " + e.getMessage());

            try
            {
                return mapper.writeValueAsString(response);
            }
            catch (Exception ex)
            {
                return "{\"status\":\"error\",\"message\":\"Critical server error\",\"data\":null}";
            }
        }
    }


}