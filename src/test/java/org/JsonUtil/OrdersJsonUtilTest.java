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
 */

public class OrdersJsonUtilTest
{
    @Test
    void orders_jsonRoundTrip_preservesAllFields()
    {
        Orders original = new Orders(1, 2, LocalDateTime.of(2026, 4, 23, 10, 30), Orders.OrderStatus.NEW);

        String json = OrdersJsonUtil.ordersToJson(original);
        Orders result = OrdersJsonUtil.ordersFromJson(json);

        assertEquals(original.getCustomerId(), result.getCustomerId());
        assertEquals(original.getStatus(), result.getStatus());
        assertEquals(original.getOrderDate(), result.getOrderDate());
    }

    //Serialisation
    @Test
    void ordersToJson_returnsValidJsonString()
    {
        Orders order = new Orders(1, 2, LocalDateTime.now(), Orders.OrderStatus.NEW);
        String json = OrdersJsonUtil.ordersToJson(order);

        assertNotNull(json);
        assertTrue(json.contains("customerId"));
        assertTrue(json.contains("status"));
    }

    //List serialisation
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
}
