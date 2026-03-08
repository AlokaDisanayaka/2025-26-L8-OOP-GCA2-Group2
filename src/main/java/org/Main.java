package org;
import org.DAO.CustomerDao;
import org.DAO_JDBC.JdbcCustomerDao;
import org.DTO.Customer;
import org.JsonUtil.CustomerJsonUtil;
import org.JsonUtil.ProductJsonUtil;
import org.Services.CustomerService;

import org.DAO.ProductDao;
import org.DAO_JDBC.JdbcProductDao;
import org.DTO.Product;
import org.Services.ProductService;

import org.DAO.OrdersDao;
import org.DAO_JDBC.JdbcOrdersDao;
import org.DTO.Orders;
import org.Services.OrdersService;
import org.JsonUtil.OrdersJsonUtil;

import org.DAO.OrderItemDao;
import org.DAO_JDBC.JdbcOrderItemDao;
import org.DTO.OrderItem;
import org.Services.OrderItemService;
import org.JsonUtil.OrderItemJsonUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public class Main {
    public static void main(String[] args) {

        CustomerDao customerDao = new JdbcCustomerDao();
        CustomerService customerService = new CustomerService(customerDao);


        // ----------------------------------------------------
        // TEST CUSTOMER
        // ----------------------------------------------------
        System.out.println(" CUSTOMER TESTS");
        System.out.println("---------------------------------------------------");

        // 1. INSERT CUSTOMER
        System.out.println("\nINSERT CUSTOMER");

        Customer newCustomer = new Customer("Test Customer", "testcustomer@email.com", "0871234567");
        Customer insertedCustomer = customerService.addCustomer(newCustomer);

        System.out.println("Inserted customer:");
        System.out.println("ID: " + insertedCustomer.getCustomerId());
        System.out.println("Name: " + insertedCustomer.getName());
        System.out.println("Email: " + insertedCustomer.getEmail());
        System.out.println("Phone: " + insertedCustomer.getPhone());

        int customerTestId = insertedCustomer.getCustomerId();

        // 2. GET ALL CUSTOMERS
        System.out.println("\nGET ALL CUSTOMERS");

        List<Customer> allCustomers = customerService.getAllCustomers();

        for (int i=0;i< allCustomers.size();i++)
        {
            Customer c=allCustomers.get(i);
            System.out.println(
                    c.getCustomerId() + " | " +
                            c.getName() + " | " +
                            c.getEmail() + " | " +
                            c.getPhone()
            );
        }

        // 3. GET CUSTOMER BY ID
        System.out.println("\nGET CUSTOMER BY ID ");

        Optional<Customer> foundCustomer = customerService.getCustomerById(customerTestId);

        if (foundCustomer.isPresent()) {
            Customer c = foundCustomer.get();
            System.out.println("Customer found:");
            System.out.println("ID: " + c.getCustomerId());
            System.out.println("Name: " + c.getName());
            System.out.println("Email: " + c.getEmail());
            System.out.println("Phone: " + c.getPhone());
        } else {
            System.out.println("Customer not found.");
        }

        // 4. UPDATE CUSTOMER
        System.out.println("\nUPDATE CUSTOMER ");

        Customer updatedCustomerData = new Customer("Updated Customer", "updatedcustomer@email.com", "0899999999");
        Customer updatedCustomer = customerService.updateCustomer(customerTestId, updatedCustomerData);

        System.out.println("Updated customer:");
        System.out.println("ID: " + updatedCustomer.getCustomerId());
        System.out.println("Name: " + updatedCustomer.getName());
        System.out.println("Email: " + updatedCustomer.getEmail());
        System.out.println("Phone: " + updatedCustomer.getPhone());

        // 5. DELETE CUSTOMER
        System.out.println("\nDELETE CUSTOMER");

        boolean customerDeleted = customerService.deleteCustomer(customerTestId);

        if (customerDeleted) {
            System.out.println("Customer deleted successfully.");
        } else {
            System.out.println("Customer was not deleted.");
        }

        // 6. CHECK IF CUSTOMER STILL EXISTS
        System.out.println("\nCHECK IF CUSTOMER STILL EXISTS");

        Optional<Customer> deletedCustomerCheck = customerService.getCustomerById(customerTestId);

        if (deletedCustomerCheck.isPresent()) {
            System.out.println("Customer still exists.");
        } else {
            System.out.println("Customer no longer exists.");
        }


     // CUSTOMER FILTER TESTS

        System.out.println("\nFILTER CUSTOMERS BY NAME CONTAINING 'a'(lowercase a)");

        List<Customer> filteredCustomers = customerService.findCustomersByFilter(
                customer -> customer.getName().toLowerCase().contains("a")
        );

        for (int i=0;i< filteredCustomers.size();i++)
        {
            Customer c= filteredCustomers.get(i);
            System.out.println(
                    c.getCustomerId() + " | " +
                            c.getName() + " | " +
                            c.getEmail() + " | " +
                            c.getPhone()
            );
        }

        System.out.println("\nFILTER CUSTOMERS BY EMAIL CONTAINING 'email.com'");

        List<Customer> emailCustomers = customerService.findCustomersByFilter(
                customer -> customer.getEmail().contains("email.com")
        );

        for (int i=0;i< emailCustomers.size();i++)
        {
            Customer c= emailCustomers.get(i);
            System.out.println(
                    c.getCustomerId() + " | " +
                            c.getName() + " | " +
                            c.getEmail() + " | " +
                            c.getPhone()
            );
        }

        // CUSTOMER JSON TESTS

        System.out.println("\n\n CUSTOMER JSON TESTS");


        // Create a sample customer object
        Customer customer = new Customer(1, "Gia Jordan", "gg1@email.com", "0851111111");

        // Convert Customer object to JSON
        String customerJson = CustomerJsonUtil.customerToJson(customer);
        System.out.println("\nCustomer object converted to JSON:");
        System.out.println(customerJson);

        // Convert JSON back to Customer object
        Customer customerFromJson = CustomerJsonUtil.customerFromJson(customerJson);
        System.out.println("\nJSON converted back to Customer object:");
        System.out.println("ID: " + customerFromJson.getCustomerId());
        System.out.println("Name: " + customerFromJson.getName());
        System.out.println("Email: " + customerFromJson.getEmail());
        System.out.println("Phone: " + customerFromJson.getPhone());

        // Get all customers from database
        List<Customer> customerList = customerService.getAllCustomers();

        // Convert list of customers to JSON
        String customerListJson = CustomerJsonUtil.customerListToJson(customerList);
        System.out.println("\nCustomer list converted to JSON:");
        System.out.println(customerListJson);

        // Customer round trip check
        Customer roundTripCustomer = CustomerJsonUtil.customerFromJson(
                CustomerJsonUtil.customerToJson(customer)
        );

        System.out.println("\nCustomer round trip worked:");
        System.out.println("Name: " + roundTripCustomer.getName());
        System.out.println("Email: " + roundTripCustomer.getEmail());

        //--------------------------------------------------------------------
        // TEST PRODUCT
        //--------------------------------------------------------------------


        System.out.println("\n\nPRODUCT TESTS................");
        System.out.println("--------------------------------------------------");


        // 1. INSERT PRODUCT
        System.out.println("\nINSERT PRODUCT ");
        ProductDao productDao = new JdbcProductDao();
        ProductService productService = new ProductService(productDao);

        Product newProduct = new Product("Test Keyboard", 45.99, 12, true);
        Product insertedProduct = productService.addProduct(newProduct);

        System.out.println("Inserted product:");
        System.out.println("ID: " + insertedProduct.getProductId());
        System.out.println("Name: " + insertedProduct.getProductName());
        System.out.println("Price: " + insertedProduct.getUnitPrice());
        System.out.println("Stock: " + insertedProduct.getStockQty());
        System.out.println("Active: " + insertedProduct.isActive());
        int testId = insertedProduct.getProductId();

        System.out.println();

        //2.HET ALL PRODUCTS
        System.out.println("\nGET ALL PRODUCTS");

        List<Product> allProducts = productService.getAllProducts();

        for (int i=0;i< allProducts.size();i++)
        {
            Product p=allProducts.get(i);
            System.out.println(
                            p.getProductId() + " | " +
                            p.getProductName() + " | " +
                            p.getUnitPrice() + " | " +
                            p.getStockQty() + " | " +
                            p.isActive()
            );
        }

        System.out.println();

        //3.GET PRODUCT BY id
        System.out.println("\nGET PRODUCT BY ID");

        Optional<Product> foundProduct = productService.getProductById(testId);

        if (foundProduct.isPresent())
        {
            Product p = foundProduct.get();
            System.out.println("Product found:");
            System.out.println("ID: " + p.getProductId());
            System.out.println("Name: " + p.getProductName());
            System.out.println("Price: " + p.getUnitPrice());
            System.out.println("Stock: " + p.getStockQty());
            System.out.println("Active: " + p.isActive());
        }
        else
        {
            System.out.println("Product not found.");
        }

        System.out.println();

       //4.UPDATE PRODUCT
        System.out.println("\nUPDATE PRODUCT");

        Product updatedData = new Product("Updated Keyboard", 55.99, 20, true);
        Product updatedProduct = productService.updateProduct(testId, updatedData);

        System.out.println("Updated product:");
        System.out.println("ID: " + updatedProduct.getProductId());
        System.out.println("Name: " + updatedProduct.getProductName());
        System.out.println("Price: " + updatedProduct.getUnitPrice());
        System.out.println("Stock: " + updatedProduct.getStockQty());
        System.out.println("Active: " + updatedProduct.isActive());

        System.out.println();

        //5.DELETE PRODUCT
        System.out.println("DELETE PRODUCT");

        boolean deleted = productService.deleteProduct(testId);

        if (deleted)
        {
            System.out.println("Product deleted successfully.");
        }
        else
        {
            System.out.println("Product was not deleted.");
        }

        System.out.println();

      //CHECK IF THE PRODUCT IS DELETED
        System.out.println("CHECK IF PRODUCT STILL EXISTS");

        Optional<Product> deletedCheck = productService.getProductById(testId);

        if (deletedCheck.isPresent())
        {
            System.out.println("Product still exists.");
        }
        else
        {
            System.out.println("Product no longer exists.");
        }


      // PRODUCT FILTER TESTS

        System.out.println("\nFILTER PRODUCTS WITH PRICE GREATER THAN 100 (>100)");

        List<Product> expensiveProducts = productService.findProductsByFilter(
                product -> product.getUnitPrice() > 100
        );

        for (int i =0;i<expensiveProducts.size();i++)
        {
            Product p = expensiveProducts.get(i);
            System.out.println(
                    p.getProductId() + " | " +
                            p.getProductName() + " | " +
                            p.getUnitPrice() + " | " +
                            p.getStockQty() + " | " +
                            p.isActive()
            );
        }

        System.out.println("\nFILTER PRODUCTS WITH STOCK LESS THAN 15 (<15)");

        List<Product> lowStockProducts = productService.findProductsByFilter(
                product -> product.getStockQty() < 15
        );

        for (int i=0;i< lowStockProducts.size();i++)
        {
            Product p= lowStockProducts.get(i);
            System.out.println(
                    p.getProductId() + " | " +
                            p.getProductName() + " | " +
                            p.getUnitPrice() + " | " +
                            p.getStockQty() + " | " +
                            p.isActive()
            );
        }


        // PRODUCT JSON TESTS

        System.out.println("\n\nPRODUCT JSON TESTS");

        // Create one sample product object
        Product product = new Product(1, "Laptop", 999.99, 10, true);

        // Convert Product object to JSON
        String productJson = ProductJsonUtil.productToJson(product);
        System.out.println("\nProduct object converted to JSON:");
        System.out.println(productJson);

        // Convert JSON back to Product object
        Product productFromJson = ProductJsonUtil.productFromJson(productJson);
        System.out.println("\nJSON converted back to Product object:");
        System.out.println("ID: " + productFromJson.getProductId());
        System.out.println("Name: " + productFromJson.getProductName());
        System.out.println("Price: " + productFromJson.getUnitPrice());
        System.out.println("Stock: " + productFromJson.getStockQty());
        System.out.println("Active: " + productFromJson.isActive());

        // Get all products from database
        List<Product> productList = productService.getAllProducts();

        // Convert list of products to JSON
        String productListJson = ProductJsonUtil.productListToJson(productList);
        System.out.println("\nProduct list converted to JSON:");
        System.out.println(productListJson);

        // Product round trip check
        Product roundTripProduct = ProductJsonUtil.productFromJson(
                ProductJsonUtil.productToJson(product)
        );

        System.out.println("\nProduct round trip worked:");
        System.out.println("Name: " + roundTripProduct.getProductName());
        System.out.println("Price: " + roundTripProduct.getUnitPrice());



        //------------------------------------------------------------------
        // TEST ORDER AND ORDER ITEMS
        //-----------------------------------------------------------------
        System.out.println("\nTESTING ORDERS");

        //TESTING ORDERS
        OrdersDao ordersDao = new JdbcOrdersDao();
        OrdersService ordersService = new OrdersService(ordersDao);

        //Add new order
        Orders newOrder = new Orders(1, LocalDateTime.now(), Orders.OrderStatus.NEW);
        System.out.println("\nTesting addOrder:");
        Orders insertedOrder = ordersService.addOrder(newOrder);
        System.out.println("Inserted Order ID: "+insertedOrder.getOrderId());
        System.out.println("Total Orders: "+ordersService.getAllOrders().size());

        //Get Order by ID
        System.out.println("\nTesting getOrderById:");
        ordersService.getOrderById(insertedOrder.getOrderId())
        .ifPresent(o -> System.out.println("Found Order: ID: " + o.getOrderId() +", Status: " + o.getStatus()));

        //Get Orders By CustomerID
        System.out.println("\nTesting getOrdersByCustomerId (customer 1):");
        ordersService.getOrdersByCustomerId(1)
        .forEach(o -> System.out.println("OrderID: " + o.getOrderId() +" CustomerID: " + o.getCustomerId()));

        //Update Order
        System.out.println("\nTesting updateOrder:");
        Orders updatedOrder = new Orders(1, LocalDateTime.now(), Orders.OrderStatus.PAID);
        Orders result = ordersService.updateOrder(insertedOrder.getOrderId(), updatedOrder);
        System.out.println("Updated Order: ID: " + result.getOrderId() +", Status: " + result.getStatus());

        //Delete Order
        System.out.println("\nTesting deleteOrder:");
        boolean deletedOrder = ordersService.deleteOrder(insertedOrder.getOrderId());
        System.out.println("Order deleted: " + deletedOrder);
        System.out.println("Total Orders after delete: " +ordersService.getAllOrders().size());

        //Predicate
        System.out.println("\nTesting Order Predicate (If status is Paid)");
        //If status is PAID
        ordersService.findOrdersByFilter(o -> o.getStatus() == Orders.OrderStatus.PAID)
        .forEach(o ->System.out.println("OrderID: " + o.getOrderId() + " Status: " + o.getStatus()));

        //JSON Conversion
        System.out.println("\nTesting OrdersJsonUtil");
        String orderJson = OrdersJsonUtil.ordersToJson(insertedOrder);
        System.out.println("Orders To JSON: "+ orderJson);

        Orders orderFromJson = OrdersJsonUtil.ordersFromJson(orderJson);
        System.out.println("Orders From JSON: Order ID: " +orderFromJson.getOrderId() + ", Status: " + orderFromJson.getStatus());

        List<Orders> ordersList = ordersService.getAllOrders();
        String ordersListJson = OrdersJsonUtil.ordersListToJson(ordersList);
        System.out.println("Orders List JSON:");
        System.out.println(ordersListJson);


        System.out.println("\nTESTING ORDERITEM");

        //TESTING ORDERITEM
        OrderItemDao orderItemDao = new JdbcOrderItemDao();
        OrderItemService orderItemService = new OrderItemService(orderItemDao);

        //Add new Order Item
        System.out.println("\nTesting addOrderItem:");
        OrderItem newItem = new OrderItem(1, 1, 2, 99.99);
        OrderItem insertedItem = orderItemService.addOrderItem(newItem);
        System.out.println("Inserted OrderItem ID: " + insertedItem.getOrderItemId());
        System.out.println("Total OrderItems: " + orderItemService.getAllOrderItems().size());

        //Get OrderItem by ID
        System.out.println("\nTesting getOrderItemById:");
        orderItemService.getOrderItemById(insertedItem.getOrderItemId())
        .ifPresent(i -> System.out.println("Found OrderItem: ID: " + i.getOrderItemId() +
                        " ProductID: " + i.getProductId() +" Quantity: " + i.getQuantity()));

        //Get OrderItems by OrderID
        System.out.println("\nTesting getOrderItemsByOrderId:");
        orderItemService.getOrderItemsByOrderId(insertedOrder.getOrderId())
        .forEach(i -> System.out.println("OrderItemID: " + i.getOrderItemId() + " OrderID: " + i.getOrderId()));

        //Update OrderItem
        System.out.println("\nTesting updateOrderItem:");
        OrderItem updatedItem = new OrderItem(1, 1, 5, 89.99);
        OrderItem resultItem = orderItemService.updateOrderItem(insertedItem.getOrderItemId(), updatedItem);
        System.out.println("Updated OrderItem: ID: " + resultItem.getOrderItemId() + " Quantity: " +resultItem.getQuantity());

        //Delete OrderItem
        System.out.println("\nTesting deleteOrderItem:");
        boolean deletedItem = orderItemService.deleteOrderItem(insertedItem.getOrderItemId());
        System.out.println("OrderItem deleted: " + deletedItem);
        System.out.println("Total OrderItems after delete: " + orderItemService.getAllOrderItems().size());

        //Predicate
        System.out.println("\nTesting OrderItem Predicate (Items with quantity > 3");
        //Items with quantity > 3
        orderItemService.findOrderItemsByFilter(i -> i.getQuantity() > 3)
        .forEach(i -> System.out.println("OrderItemID: " + i.getOrderItemId() +" Quantity: "+i.getQuantity()));

        //JSON Conversion
        System.out.println("\nTesting OrderItemJsonUtil");
        String orderItemJson = OrderItemJsonUtil.orderItemToJson(insertedItem);
        System.out.println("OrderItem to JSON: " + orderItemJson);

        OrderItem orderItemFromJson = OrderItemJsonUtil.orderItemFromJson(orderItemJson);
        System.out.println("OrderItem From JSON OrderItem ID: " + orderItemFromJson.getOrderItemId() + ", Quantity: " + orderItemFromJson.getQuantity());

        List<OrderItem> orderItemList = orderItemService.getAllOrderItems();
        String orderItemListJson = OrderItemJsonUtil.orderItemListToJson(orderItemList);
        System.out.println("OrderItem List to JSON:");
        System.out.println(orderItemListJson);
    }
}