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

        //TESTING CUSTOMER
        CustomerDao customerDao = new JdbcCustomerDao();
        CustomerService customerService = new CustomerService(customerDao);

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
        boolean deleted = ordersService.deleteOrder(insertedOrder.getOrderId());
        System.out.println("Order deleted: " + deleted);
        System.out.println("Total Orders after delete: " +ordersService.getAllOrders().size());

        //Predicate
        System.out.println("\nTesting Order Predicate (If status is Paid)");
        //If status is PAID
        ordersService.findOrdersByFilter(o -> o.getStatus() == Orders.OrderStatus.PAID)
        .forEach(o ->System.out.println("OrderID: " + o.getOrderId() + " Status: " + o.getStatus()));


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

        //Predicate
        System.out.println("\nTesting OrderItem Predicate (Items with quantity > 3");
        //Items with quantity > 3
        orderItemService.findOrderItemsByFilter(i -> i.getQuantity() > 3)
        .forEach(i -> System.out.println("OrderItemID: " + i.getOrderItemId() +" Quantity: "+i.getQuantity()));

        //Delete OrderItem
        System.out.println("\nTesting deleteOrderItem:");
        boolean deletedItem = orderItemService.deleteOrderItem(insertedItem.getOrderItemId());
        System.out.println("OrderItem deleted: " + deletedItem);
        System.out.println("Total OrderItems after delete: " + orderItemService.getAllOrderItems().size());
    }
}
