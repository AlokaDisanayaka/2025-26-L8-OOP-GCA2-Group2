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

    //READ
    //tests that requesting a nonexistent order id returns an empty optional
    @Test
    void getOrderById_returnsEmpty_whenIdDoesNotExist()
    {
        Optional<Orders> result = dao.getOrderById(-99);
        assertTrue(result.isEmpty());
    }

    //tests that getAllOrders returns a not null list even if empty
    @Test
    void getAllOrders_returnsList()
    {
        List<Orders> orders = dao.getAllOrders();
        assertNotNull(orders);
    }

    //tests that getOrdersByCustomerId returns a valid list for a given customer
    @Test
    void getOrdersByCustomerId_returnsMatchingOrders()
    {
        List<Orders> orders = dao.getOrdersByCustomerId(1);
        assertNotNull(orders);
    }

    //INSERT
    //tests that inserting an order returns a generated ID and a valid object
    @Test
    void insertOrder_returnsGeneratedId()
    {
        Orders order = new Orders(1, LocalDateTime.now(), Orders.OrderStatus.NEW);
        Orders inserted = dao.insertOrder(order);

        assertNotNull(inserted);
        assertTrue(inserted.getOrderId() > 0);
    }

    //UPDATE
    //tests that updating an order modifies its status correctly
    @Test
    void updateOrder_returnsUpdatedOrder()
    {
        Orders order = dao.insertOrder(new Orders(1, LocalDateTime.now(), Orders.OrderStatus.NEW));

        Orders update = new Orders(1, LocalDateTime.now(), Orders.OrderStatus.PAID);
        Orders result = dao.updateOrder(order.getOrderId(), update);

        assertEquals(Orders.OrderStatus.PAID, result.getStatus());
    }

    //DELETE
    //tests that deleting an order returns true when the record exists and is removed
    @Test
    void deleteOrder_returnsTrue_whenExists()
    {
        Orders order = dao.insertOrder(new Orders(1, LocalDateTime.now(), Orders.OrderStatus.NEW));
        boolean deleted = dao.deleteOrderById(order.getOrderId());

        assertTrue(deleted);
    }

    //FILTER
    //tests that getOrdersByCustomerId only returns orders belonging to the given customer
    @Test
    void getOrdersByCustomerId_returnsOnlyMatchingOrders()
    {
        dao.insertOrder(new Orders(1, LocalDateTime.now(), Orders.OrderStatus.NEW));
        dao.insertOrder(new Orders(1, LocalDateTime.now(), Orders.OrderStatus.PAID));
        dao.insertOrder(new Orders(2, LocalDateTime.now(), Orders.OrderStatus.NEW));

        List<Orders> result = dao.getOrdersByCustomerId(1);
        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(o -> o.getCustomerId() == 1));
    }

    //METADATA
    //tests that metadata retrieval returns order details without loading file (blob) data
    @Test
    void getOrdersMetadataById_returnsMetadataWithoutFileData()
    {
        Orders order = new Orders(1, LocalDateTime.now(), Orders.OrderStatus.NEW);
        order.setFileName("order.txt");
        order.setContentType("text/plain");
        order.setFileSize(200);

        Orders inserted = dao.insertOrder(order);
        Optional<Orders> result = dao.getOrdersMetadataById(inserted.getOrderId());
        assertTrue(result.isPresent());

        assertNull(result.get().getFileData());
        assertEquals("order.txt", result.get().getFileName());
        assertEquals("text/plain", result.get().getContentType());
        assertEquals(200, result.get().getFileSize());
    }

    //BINARY DATA
    //tests that file byte[] data is stored and retrieved correctly without corruption
    @Test
    void insertAndRetrieveOrder_withFile_preservesBytes()
    {
        byte[] fileData = "Order".getBytes();

        Orders order = new Orders(1, LocalDateTime.now(), Orders.OrderStatus.NEW);
        order.setFileData(fileData);
        order.setFileName("text.txt");
        order.setContentType("text/plain");
        order.setFileSize(fileData.length);

        Orders inserted = dao.insertOrder(order);
        Optional<Orders> retrieved = dao.getOrderById(inserted.getOrderId());

        assertTrue(retrieved.isPresent());
        assertArrayEquals(fileData, retrieved.get().getFileData());
    }
}