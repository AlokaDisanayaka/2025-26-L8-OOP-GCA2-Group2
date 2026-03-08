package org;
import org.DAO.CustomerDao;
import org.DAO_JDBC.JdbcCustomerDao;
import org.DTO.Customer;
import org.Services.CustomerService;

import org.DAO.ProductDao;
import org.DAO_JDBC.JdbcProductDao;
import org.DTO.Product;
import org.Services.ProductService;

import org.DAO.OrdersDao;
import org.DAO_JDBC.JdbcOrdersDao;
import org.DTO.Orders;
import org.Services.OrdersService;

import org.DAO.OrderItemDao;
import org.DAO_JDBC.JdbcOrderItemDao;
import org.DTO.OrderItem;
import org.Services.OrderItemService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public class Main {
    public static void main(String[] args) {

        CustomerDao customerDao = new JdbcCustomerDao();
        CustomerService customerService = new CustomerService(customerDao);

        // test customer ...

        //test add customer
        Customer newCustomer = new Customer("Test User1", "test1user@email.com", "0871234567");
        Customer inserted = customerService.addCustomer(newCustomer);
        //test getCustomerID
        System.out.println("Inserted customer id = " + inserted.getCustomerId());
        //test getAllCustomers
        System.out.println("Total customers = " + customerService.getAllCustomers().size());
        //test getCustomerById
        System.out.println("Customer with cus_Id 2: "+customerService.getCustomerById(2));
        //test updateCustomer
        Customer updatedCustomer=new Customer("Anna Murphy", "anna@gmail.com","0875689654");
        System.out.println("Updating the details of the customer with customer Id=>1"+customerService.updateCustomer(1,updatedCustomer));
        //test delete customer
        System.out.println("Deleting the customer with customer Id=> 6"+customerService.deleteCustomer(6));


        //test product......
        ProductDao productDao = new JdbcProductDao();
        ProductService productService = new ProductService(productDao);

        //testing insertProduct
        System.out.println("Insert product");

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

        //testing getAllProducts
        System.out.println("Get all products");

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

        //testing getProductByID
        System.out.println("Get product ID");

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

        //testing updateProduct
        System.out.println("Update product");

        Product updatedData = new Product("Updated Keyboard", 55.99, 20, true);
        Product updatedProduct = productService.updateProduct(testId, updatedData);

        System.out.println("Updated product:");
        System.out.println("ID: " + updatedProduct.getProductId());
        System.out.println("Name: " + updatedProduct.getProductName());
        System.out.println("Price: " + updatedProduct.getUnitPrice());
        System.out.println("Stock: " + updatedProduct.getStockQty());
        System.out.println("Active: " + updatedProduct.isActive());

        System.out.println();

        //testing deleteProduct
        System.out.println("Delete product");

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

      //Check if the product is deleted
        System.out.println("=== CHECK IF PRODUCT STILL EXISTS ===");

        Optional<Product> deletedCheck = productService.getProductById(testId);

        if (deletedCheck.isPresent()) {
            System.out.println("Product still exists.");
        } else {
            System.out.println("Product no longer exists.");
        }



        //Test Orders and OrderItem....

        OrdersDao ordersDao = new JdbcOrdersDao();
        OrdersService ordersService = new OrdersService(ordersDao);

        Orders newOrder = new Orders(1, LocalDateTime.now(), Orders.OrderStatus.NEW);
        Orders insertedOrder = ordersService.addOrder(newOrder);

        System.out.println("\nInserted Order ID = "+insertedOrder.getOrderId());
        System.out.println("Total Orders = "+ordersService.getAllOrders().size());

        OrderItemDao orderItemDao = new JdbcOrderItemDao();
        OrderItemService orderItemService = new OrderItemService(orderItemDao);

        OrderItem newItem = new OrderItem(insertedOrder.getOrderId(), 1,2, 99.99);
        OrderItem insertedItem = orderItemService.addOrderItem(newItem);

        System.out.println("\nInserted OrderItem ID = "+insertedItem.getOrderItemId());
        System.out.println("Total OrderItems = "+orderItemService.getAllOrderItems().size());


    }
}