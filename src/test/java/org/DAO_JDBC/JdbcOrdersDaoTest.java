package org.DAO_JDBC;
import org.DTO.Orders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This test class tests the behaviour of the JdbcOrdersDao
 * The purpose is to ensure that SQL CRUD operations work correctly:
 *
 * - Read operations - getById, getAll
 * - Insert operation with auto generated ID
 * - Update operation to confirm data is modified correctly
 * - Delete operation to confirm records are removed
 *
 *  Using @BeforeEach makes sure each test runs independently to create a clean DAO instance
 */

public class JdbcOrdersDaoTest
{
    private JdbcOrdersDao dao;

    @BeforeEach
    void setUp()
    {
        dao = new JdbcOrdersDao();
    }

    //Read
    @Test
    void getOrderById_returnsEmpty_whenIdDoesNotExist()
    {
        Optional<Orders> result = dao.getOrderById(-99);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllOrders_returnsList()
    {
        List<Orders> orders = dao.getAllOrders();
        assertNotNull(orders);
    }

    @Test
    void getOrdersByCustomerId_returnsMatchingOrders()
    {
        List<Orders> orders = dao.getOrdersByCustomerId(1);
        assertNotNull(orders);
    }

    //Insert
    @Test
    void insertOrder_returnsGeneratedId()
    {
        Orders order = new Orders(1, LocalDateTime.now(), Orders.OrderStatus.NEW);
        Orders inserted = dao.insertOrder(order);

        assertNotNull(inserted);
        assertTrue(inserted.getOrderId() > 0);
    }

    //Update
    @Test
    void updateOrder_returnsUpdatedOrder()
    {
        Orders order = dao.insertOrder(new Orders(1, LocalDateTime.now(), Orders.OrderStatus.NEW));

        Orders update = new Orders(1, LocalDateTime.now(), Orders.OrderStatus.PAID);
        Orders result = dao.updateOrder(order.getOrderId(), update);

        assertEquals(Orders.OrderStatus.PAID, result.getStatus());
    }

    //Delete
    @Test
    void deleteOrder_returnsTrue_whenExists()
    {
        Orders order = dao.insertOrder(new Orders(1, LocalDateTime.now(), Orders.OrderStatus.NEW));
        boolean deleted = dao.deleteOrderById(order.getOrderId());

        assertTrue(deleted);
    }
}