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

    //READ
    //tests that requesting a nonexistent order item id returns an empty optional
    @Test
    void getOrderItemById_returnsEmptyOptional_whenIdDoesNotExist()
    {
        Optional<OrderItem> result = dao.getOrderItemById(-99);
        assertTrue(result.isEmpty());
    }

    //tests that getAllOrderItems returns a not null list when data exists in the database
    @Test
    void getAllOrderItems_returnsList_whenDatabaseHasData()
    {
        List<OrderItem> items = dao.getAllOrderItems();
        assertNotNull(items);
    }

    //INSERT
    //tests that inserting an order item returns a generated id and a valid inserted object
    @Test
    void insertOrderItem_returnsGeneratedId_whenSuccessful()
    {
        OrderItem item = new OrderItem(1, 1, 2, 9.99);

        OrderItem inserted = dao.insertOrderItem(item);

        assertNotNull(inserted);
        assertTrue(inserted.getOrderItemId() > 0);
    }

    //UPDATE
    //tests that updating an existing order item modifies the data correctly and keeps the same id
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

    //DELETE
    //tests that deleting an existing order item returns true when the record is successfully removed
    @Test
    void deleteOrderItemById_returnsTrue_whenItemExists()
    {
        OrderItem item = dao.insertOrderItem(new OrderItem(1, 1, 2, 10.0));
        boolean deleted = dao.deleteOrderItemById(item.getOrderItemId());
        assertTrue(deleted);
    }

    //FILTER
    //tests that getOrderItemsByOrderId only returns items that match the given order id
    @Test
    void getOrderItemsByOrderId_returnsOnlyMatchingItems()
    {
        dao.insertOrderItem(new OrderItem(1, 1, 2, 10.0));
        dao.insertOrderItem(new OrderItem(2, 1, 3, 20.0));

        List<OrderItem> result = dao.getOrderItemsByOrderId(1);

        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(i -> i.getOrderId() == 1));
    }

    //METADATA
    //tests that metadata retrieval returns order item data without loading file (blob) content
    @Test
    void getOrderItemMetadataById_returnsMetadataWithoutFileData()
    {
        OrderItem item = new OrderItem(1, 1, 2, 10.0);
        item.setFileName("test.txt");
        item.setContentType("text/plain");
        item.setFileSize(100);

        OrderItem inserted = dao.insertOrderItem(item);
        Optional<OrderItem> result = dao.getOrderItemMetadataById(inserted.getOrderItemId());

        assertTrue(result.isPresent());
        assertNull(result.get().getFileData());
        assertEquals("test.txt", result.get().getFileName());
    }

    //BINARY DATA
    //tests that file byte[] data is correctly stored and retrieved without corruption
    @Test
    void insertAndRetrieveOrderItem_withFile_preservesBytes()
    {
        byte[] fileData = "OrderItem".getBytes();

        OrderItem item = new OrderItem(1, 1, 2, 10.0);
        item.setFileData(fileData);
        item.setFileName("test.txt");
        item.setContentType("text/plain");
        item.setFileSize(fileData.length);

        OrderItem inserted = dao.insertOrderItem(item);
        Optional<OrderItem> retrieved = dao.getOrderItemById(inserted.getOrderItemId());

        assertTrue(retrieved.isPresent());
        assertArrayEquals(fileData, retrieved.get().getFileData());
    }
}