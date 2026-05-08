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
 *
 *  @author Adhira Pillai (primary)
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

    //tests that the service can retrieve an order by ID when it exists
    @Test
    void getOrderById_returnsOrder_whenExists()
    {
        Orders inserted = dao.insertOrder(new Orders(1, LocalDateTime.now(), Orders.OrderStatus.NEW));
        Optional<Orders> result = service.getOrderById(inserted.getOrderId());

        assertTrue(result.isPresent());
        assertEquals(inserted.getOrderId(), result.get().getOrderId());
    }

    //tests that retrieving all orders through the service returns a not null list
    @Test
    void getAllOrders_returnsList()
    {
        List<Orders> result = service.getAllOrders();
        assertNotNull(result);
    }

    //tests that retrieving orders by customer ID returns a valid list
    @Test
    void getOrdersByCustomerId_returnsMatchingOrders()
    {
        List<Orders> result = service.getOrdersByCustomerId(1);
        assertNotNull(result);
    }

    //tests that adding an order through the service returns an object with a generated ID
    @Test
    void addOrder_returnsOrderWithGeneratedId()
    {
        Orders order = new Orders(1, LocalDateTime.now(), Orders.OrderStatus.NEW);
        Orders result = service.addOrder(order);

        assertNotNull(result);
        assertTrue(result.getOrderId() > 0);
    }

    //tests that updating an order through the service correctly changes its status
    @Test
    void updateOrder_returnsUpdatedOrder()
    {
        Orders inserted = dao.insertOrder(new Orders(1, LocalDateTime.now(), Orders.OrderStatus.NEW));
        Orders update = new Orders(1, LocalDateTime.now(), Orders.OrderStatus.PAID);
        Orders result = service.updateOrder(inserted.getOrderId(), update);

        assertEquals(Orders.OrderStatus.PAID, result.getStatus());
    }

    //tests that deleting an order through the service returns true when successful
    @Test
    void deleteOrder_returnsTrue_whenSuccessful()
    {
        Orders inserted = dao.insertOrder(new Orders(1, LocalDateTime.now(), Orders.OrderStatus.NEW));
        boolean result = service.deleteOrder(inserted.getOrderId());

        assertTrue(result);
    }

    //tests that metadata fields (file name, content type, file size) are correctly stored and retrieved
    @Test
    void getOrdersMetadataById_returnsMetadataCorrectly()
    {
        Orders order = new Orders(1, LocalDateTime.now(), Orders.OrderStatus.NEW);

        order.setFileName("order.txt");
        order.setContentType("text/plain");
        order.setFileSize(100);

        Orders inserted = dao.insertOrder(order);
        Optional<Orders> result = service.getOrdersMetadataById(inserted.getOrderId());

        assertTrue(result.isPresent());
        assertEquals("order.txt", result.get().getFileName());
        assertEquals("text/plain", result.get().getContentType());
        assertEquals(100, result.get().getFileSize());
    }

    //tests that the service filter method returns only orders matching the given condition
    @Test
    void findOrdersByFilter_returnsOnlyMatchingOrders()
    {
        dao.insertOrder(new Orders(1, LocalDateTime.now(), Orders.OrderStatus.NEW));
        dao.insertOrder(new Orders(1, LocalDateTime.now(), Orders.OrderStatus.PAID));

        List<Orders> result = service.findOrdersByFilter(o -> o.getStatus() == Orders.OrderStatus.PAID);

        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(o -> o.getStatus() == Orders.OrderStatus.PAID));
    }

    //tests that binary file data is correctly preserved when stored and retrieved through the service
    @Test
    void service_retrievesBinaryDataCorrectly()
    {
        byte[] data = "ORDERDATA".getBytes();

        Orders order = new Orders(1, LocalDateTime.now(), Orders.OrderStatus.NEW);
        order.setFileData(data);

        Orders inserted = dao.insertOrder(order);
        Optional<Orders> result = service.getOrderById(inserted.getOrderId());

        assertTrue(result.isPresent());
        assertArrayEquals(data, result.get().getFileData());
    }
}
