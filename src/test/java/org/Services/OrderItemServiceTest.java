package org.Services;

import org.DAO_JDBC.JdbcOrderItemDao;
import org.DTO.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This test class tests the behaviour of the OrderItemService layer
 * These tests focus on service level logic
 *
 * - Retrieving items through the service
 * - Adding items and returning generated IDs
 * - Deleting items through the service
 * - Filtering items using custom predicates business logic
 *
 *  Shows the separation between service and data access layers
 */

public class OrderItemServiceTest
{
    private OrderItemService service;
    private JdbcOrderItemDao dao;

    @BeforeEach
    void setUp()
    {
        dao = new JdbcOrderItemDao();
        service = new OrderItemService(dao);
    }

    //Read
    @Test
    void getOrderItemById_returnsItem_whenExists()
    {
        OrderItem inserted = dao.insertOrderItem(new OrderItem(1, 1, 2, 10, 5.0));
        Optional<OrderItem> result = service.getOrderItemById(inserted.getOrderItemId());

        assertTrue(result.isPresent());
        assertEquals(inserted.getOrderItemId(), result.get().getOrderItemId());
    }

    //Insert
    @Test
    void addOrderItem_returnsItemWithGeneratedId()
    {
        OrderItem item = new OrderItem(1, 1, 2, 9.99);
        OrderItem result = service.addOrderItem(item);

        assertNotNull(result);
        assertTrue(result.getOrderItemId() > 0);
    }

    //Delete
    @Test
    void deleteOrderItem_returnsTrue_whenSuccessful()
    {
        OrderItem inserted = dao.insertOrderItem(new OrderItem(1, 1, 2, 3, 10.0));
        boolean result = service.deleteOrderItem(inserted.getOrderItemId());

        assertTrue(result);
    }

    //Filter
    @Test
    void findOrderItemsByFilter_returnsOnlyMatchingItems()
    {
        dao.insertOrderItem(new OrderItem(1, 1, 2, 5, 10.0));
        dao.insertOrderItem(new OrderItem(1, 1, 3, 20, 10.0));

        List<OrderItem> result = service.findOrderItemsByFilter(i -> i.getQuantity() > 10);

        assertEquals(1, result.size());
        assertTrue(result.get(0).getQuantity() > 10);
    }
}
