package org;
import org.DAO.CustomerDao;
import org.DAO_JDBC.JdbcCustomerDao;
import org.DTO.Customer;
import org.Services.CustomerService;

import org.DAO.OrdersDao;
import org.DAO_JDBC.JdbcOrdersDao;
import org.DTO.Orders;
import org.Services.OrdersService;

import org.DAO.OrderItemDao;
import org.DAO_JDBC.JdbcOrderItemDao;
import org.DTO.OrderItem;
import org.Services.OrderItemService;

import java.time.LocalDateTime;


public class Main {
    public static void main(String[] args) {

        CustomerDao customerDao = new JdbcCustomerDao();
        CustomerService customerService = new CustomerService(customerDao);

        // test ...
        Customer newCustomer = new Customer("Test User", "testuser@email.com", "0871234567");
        Customer inserted = customerService.addCustomer(newCustomer);

        System.out.println("Inserted customer id = " + inserted.getCustomerId());
        System.out.println("Total customers = " + customerService.getAllCustomers().size());


        //Test Orders and OrderItem
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