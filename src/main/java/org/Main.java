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