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
 *
 *  @author Adhira Pillai (primary)
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

    //tests that the service can retrieve an order item by ID when it exists
    @Test
    void getOrderItemById_returnsItem_whenExists()
    {
        OrderItem inserted = dao.insertOrderItem(new OrderItem(1, 1, 2, 10, 5.0));
        Optional<OrderItem> result = service.getOrderItemById(inserted.getOrderItemId());

        assertTrue(result.isPresent());
        assertEquals(inserted.getOrderItemId(), result.get().getOrderItemId());
    }

    //tests that retrieving all order items through the service returns a not null list
    @Test
    void getAllOrderItems_returnsList()
    {
        List<OrderItem> result = service.getAllOrderItems();
        assertNotNull(result);
    }

    //tests that retrieving order items by order ID returns only items matching the given order ID
    @Test
    void getOrderItemsByOrderId_returnsMatchingItems()
    {
        dao.insertOrderItem(new OrderItem(1, 1, 2, 10, 5.0));
        dao.insertOrderItem(new OrderItem(1, 1, 3, 5, 5.0));

        List<OrderItem> result = service.getOrderItemsByOrderId(1);

        assertNotNull(result);
        assertTrue(result.stream().allMatch(i -> i.getOrderId() == 1));
    }

    //tests that adding an order item through the service returns an object with a generated ID
    @Test
    void addOrderItem_returnsItemWithGeneratedId()
    {
        OrderItem item = new OrderItem(1, 1, 2, 9.99);
        OrderItem result = service.addOrderItem(item);

        assertNotNull(result);
        assertTrue(result.getOrderItemId() > 0);
    }

    //tests that deleting an order item through the service returns true when successful
    @Test
    void deleteOrderItem_returnsTrue_whenSuccessful()
    {
        OrderItem inserted = dao.insertOrderItem(new OrderItem(1, 1, 2, 3, 10.0));
        boolean result = service.deleteOrderItem(inserted.getOrderItemId());

        assertTrue(result);
    }

    //tests that metadata fields (file name, content type, file size) are correctly retrieved for an order item without loading binary data
    @Test
    void getOrderItemMetadata_returnsCorrectMetadata()
    {
        byte[] data = "DATA".getBytes();

        OrderItem item = new OrderItem(1, 1, 2, 10.0);
        item.setFileData(data);
        item.setFileName("test.txt");
        item.setContentType("text/plain");
        item.setFileSize(data.length);

        OrderItem inserted = dao.insertOrderItem(item);
        Optional<OrderItem> result = service.getOrderItemMetadataById(inserted.getOrderItemId());

        assertTrue(result.isPresent());

        assertEquals("test.txt", result.get().getFileName());
        assertEquals("text/plain", result.get().getContentType());
        assertEquals(data.length, result.get().getFileSize());
    }

    //tests that the service filter method returns only items matching the given condition
    @Test
    void findOrderItemsByFilter_returnsOnlyMatchingItems()
    {
        dao.insertOrderItem(new OrderItem(1, 1, 2, 5, 10.0));
        dao.insertOrderItem(new OrderItem(1, 1, 3, 20, 10.0));

        List<OrderItem> result = service.findOrderItemsByFilter(i -> i.getQuantity() > 10);

        assertEquals(1, result.size());
        assertTrue(result.get(0).getQuantity() > 10);
    }

    //tests that updating an order item through the service correctly modifies product data
    @Test
    void updateOrderItem_returnsUpdatedItem()
    {
        OrderItem inserted = dao.insertOrderItem(new OrderItem(1, 1, 2, 10.0));
        OrderItem update = new OrderItem(1, 5, 2, 20.0);

        OrderItem result = service.updateOrderItem(inserted.getOrderItemId(), update);
        assertEquals(5, result.getProductId());
    }

    //tests that binary file data is correctly preserved when stored and retrieved through the service
    @Test
    void service_retrievesBinaryDataCorrectly()
    {
        byte[] data = "DATA".getBytes();

        OrderItem item = new OrderItem(1, 1, 2, 10.0);
        item.setFileData(data);

        OrderItem inserted = dao.insertOrderItem(item);
        Optional<OrderItem> result = service.getOrderItemById(inserted.getOrderItemId());

        assertArrayEquals(data, result.get().getFileData());
    }
}
