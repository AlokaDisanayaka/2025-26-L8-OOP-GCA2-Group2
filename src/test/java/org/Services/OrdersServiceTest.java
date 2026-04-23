package org.Services;
import org.DAO_JDBC.JdbcOrdersDao;
import org.DTO.Orders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This test class tests the behaviour of the OrdersService layer
 * These tests focus on service level logic
 *
 * - Retrieving orders through the service
 * - Adding orders and returning generated IDs
 * - Updating and deleting orders through the service
 * - Filtering orders using custom predicates business logic
 *
 *  Shows the separation between service and data access layers
 */

public class OrdersServiceTest
{
    private OrdersService service;
    private JdbcOrdersDao dao;

    @BeforeEach
    void setUp()
    {
        dao = new JdbcOrdersDao();
        service = new OrdersService(dao);
    }

    //Read
    @Test
    void getOrderById_returnsOrder_whenExists()
    {
        Orders inserted = dao.insertOrder(new Orders(1, LocalDateTime.now(), Orders.OrderStatus.NEW));
        Optional<Orders> result = service.getOrderById(inserted.getOrderId());

        assertTrue(result.isPresent());
        assertEquals(inserted.getOrderId(), result.get().getOrderId());
    }

    @Test
    void getAllOrders_returnsList()
    {
        List<Orders> result = service.getAllOrders();
        assertNotNull(result);
    }

    @Test
    void getOrdersByCustomerId_returnsMatchingOrders()
    {
        List<Orders> result = service.getOrdersByCustomerId(1);
        assertNotNull(result);
    }

    //Insert
    @Test
    void addOrder_returnsOrderWithGeneratedId()
    {
        Orders order = new Orders(1, LocalDateTime.now(), Orders.OrderStatus.NEW);
        Orders result = service.addOrder(order);

        assertNotNull(result);
        assertTrue(result.getOrderId() > 0);
    }

    //Update
    @Test
    void updateOrder_returnsUpdatedOrder()
    {
        Orders inserted = dao.insertOrder(new Orders(1, LocalDateTime.now(), Orders.OrderStatus.NEW));
        Orders update = new Orders(1, LocalDateTime.now(), Orders.OrderStatus.PAID);
        Orders result = service.updateOrder(inserted.getOrderId(), update);

        assertEquals(Orders.OrderStatus.PAID, result.getStatus());
    }

    //Delete
    @Test
    void deleteOrder_returnsTrue_whenSuccessful()
    {
        Orders inserted = dao.insertOrder(new Orders(1, LocalDateTime.now(), Orders.OrderStatus.NEW));
        boolean result = service.deleteOrder(inserted.getOrderId());

        assertTrue(result);
    }

    //Metadata
    @Test
    void getOrdersMetadataById_returnsOptional()
    {
        Orders inserted = dao.insertOrder(new Orders(1, LocalDateTime.now(), Orders.OrderStatus.NEW));
        Optional<Orders> result = service.getOrdersMetadataById(inserted.getOrderId());

        assertTrue(result.isPresent());
    }

    //Business logic
    @Test
    void findOrdersByFilter_returnsOnlyMatchingOrders()
    {
        Orders o1 = dao.insertOrder(new Orders(1, LocalDateTime.now(), Orders.OrderStatus.NEW));
        Orders o2 = dao.insertOrder(new Orders(1, LocalDateTime.now(), Orders.OrderStatus.PAID));

        List<Orders> result = service.findOrdersByFilter(o -> o.getStatus() == Orders.OrderStatus.PAID);

        assertTrue(result.stream().anyMatch(o -> o.getStatus() == Orders.OrderStatus.PAID));
    }
}
