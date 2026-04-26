package org.DAO_JDBC;

import org.DTO.OrderItem;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This test class tests the behaviour of the JdbcOrderItemDao
 * The purpose is to ensure that SQL CRUD operations work correctly:
 *
 * - Read operations - getById, getAll
 * - Insert operation with auto generated ID
 * - Update operation to confirm data is modified correctly
 * - Delete operation to confirm records are removed
 *
 *  Using @BeforeEach makes sure each test runs independently to create a clean DAO instance
 */

public class JdbcOrderItemDaoTest
{
    private JdbcOrderItemDao dao;

    @BeforeEach
    void setUp()
    {
        dao = new JdbcOrderItemDao();
    }

    //Read
    @Test
    void getOrderItemById_returnsEmptyOptional_whenIdDoesNotExist()
    {
        Optional<OrderItem> result = dao.getOrderItemById(-99);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllOrderItems_returnsList_whenDatabaseHasData()
    {
        List<OrderItem> items = dao.getAllOrderItems();
        assertNotNull(items);
    }

    //Insert
    @Test
    void insertOrderItem_returnsGeneratedId_whenSuccessful()
    {
        OrderItem item = new OrderItem(1, 1, 2, 9.99);

        OrderItem inserted = dao.insertOrderItem(item);

        assertNotNull(inserted);
        assertTrue(inserted.getOrderItemId() > 0);
    }

    //Update
    @Test
    void updateOrderItem_returnsUpdatedItem_whenSuccessful()
    {
        OrderItem item = new OrderItem(1, 1, 2, 10.0);
        OrderItem inserted = dao.insertOrderItem(item);

        OrderItem update = new OrderItem(1, 3, 2, 20.0);

        OrderItem result = dao.updateOrderItem(inserted.getOrderItemId(), update);

        assertEquals(inserted.getOrderItemId(), result.getOrderItemId());
        assertEquals(3, result.getProductId());
    }

    //Delete
    @Test
    void deleteOrderItemById_returnsTrue_whenItemExists()
    {
        OrderItem item = dao.insertOrderItem(new OrderItem(1, 1, 2, 10.0));
        boolean deleted = dao.deleteOrderItemById(item.getOrderItemId());
        assertTrue(deleted);
    }
}