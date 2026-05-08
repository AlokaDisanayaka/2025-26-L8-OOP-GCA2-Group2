package org.JsonUtil;
import org.DTO.Orders;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This test class tests JSON serialisation and deserialisation
 *
 * - JSON round trip: object -> JSON -> object
 * - Serialisation of a single object to JSON
 * - Serialisation of a list of objects to JSON*
 *
 * @author Adhira Pillai (primary)
 */

public class OrdersJsonUtilTest
{
    //tests full JSON round trip conversion preserves all order fields correctly
    @Test
    void orders_jsonRoundTrip_preservesAllFields()
    {
        Orders original = new Orders(1, 2, LocalDateTime.of(2026, 4, 23, 10, 30), Orders.OrderStatus.NEW);

        original.setFileName("order.txt");
        original.setContentType("text/plain");
        original.setFileSize(123);
        original.setFileData("test".getBytes());

        String json = OrdersJsonUtil.ordersToJson(original);
        Orders result = OrdersJsonUtil.ordersFromJson(json);

        assertEquals(original.getCustomerId(), result.getCustomerId());
        assertEquals(original.getStatus(), result.getStatus());
        assertEquals(original.getOrderDate(), result.getOrderDate());

        assertEquals(original.getFileName(), result.getFileName());
        assertEquals(original.getContentType(), result.getContentType());
        assertEquals(original.getFileSize(), result.getFileSize());
        assertArrayEquals(original.getFileData(), result.getFileData());
    }

    //tests that serialising a single order object produces valid JSON output containing expected fields
    @Test
    void ordersToJson_returnsValidJsonString()
    {
        Orders order = new Orders(1, 2, LocalDateTime.now(), Orders.OrderStatus.NEW);
        String json = OrdersJsonUtil.ordersToJson(order);

        assertNotNull(json);
        assertTrue(json.contains("customerId"));
        assertTrue(json.contains("status"));
    }

    //tests that a list of order objects is correctly converted into a JSON array
    @Test
    void ordersListToJson_returnsJsonArray()
    {
        List<Orders> orders = List.of(new Orders(1, 2, LocalDateTime.now(), Orders.OrderStatus.NEW),
                                      new Orders(2, 3, LocalDateTime.now(), Orders.OrderStatus.PAID));
        String json = OrdersJsonUtil.ordersListToJson(orders);

        assertNotNull(json);
        assertTrue(json.startsWith("["));
        assertTrue(json.contains("customerId"));
    }

    //tests that JSON serialisation and deserialisation correctly handles null file data
    @Test
    void orders_json_handlesNullFileData()
    {
        Orders order = new Orders(1, 2, LocalDateTime.of(2026, 4, 23, 10, 30), Orders.OrderStatus.NEW);
        order.setFileData(null);

        String json = OrdersJsonUtil.ordersToJson(order);
        Orders result = OrdersJsonUtil.ordersFromJson(json);

        assertNull(result.getFileData());
    }

    //tests that serialising an empty list of orders returns an empty JSON array
    @Test
    void ordersList_toJson_emptyList_returnsEmptyArray()
    {
        String json = OrdersJsonUtil.ordersListToJson(List.of());
        assertEquals("[]", json);
    }
}
