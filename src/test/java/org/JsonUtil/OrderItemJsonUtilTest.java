package org.JsonUtil;

import org.DTO.OrderItem;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This test class tests JSON serialisation and deserialisation
 *
 * - JSON round trip: object -> JSON -> object
 * - Serialisation of a single object to JSON
 * - Serialisation of a list of objects to JSON*
 */

public class OrderItemJsonUtilTest
{
    @Test
    void orderItem_jsonRoundTrip_preservesAllFields()
    {
        OrderItem original = new OrderItem(1, 2, 3, 4, 99.99);

        original.setFileName("test.txt");
        original.setContentType("text/plain");
        original.setFileSize(100);
        original.setFileData("test".getBytes());

        String json = OrderItemJsonUtil.orderItemToJson(original);
        OrderItem result = OrderItemJsonUtil.orderItemFromJson(json);

        assertEquals(original.getOrderId(), result.getOrderId());
        assertEquals(original.getProductId(), result.getProductId());
        assertEquals(original.getQuantity(), result.getQuantity());
        assertEquals(original.getUnitPriceAtTime(), result.getUnitPriceAtTime());

        assertEquals(original.getFileName(), result.getFileName());
        assertEquals(original.getContentType(), result.getContentType());
        assertEquals(original.getFileSize(), result.getFileSize());
        assertArrayEquals(original.getFileData(), result.getFileData());
    }

    //JSON output
    @Test
    void orderItem_toJson_returnsValidJson()
    {
        OrderItem item = new OrderItem(1, 1, 1, 10, 5.0);

        String json = OrderItemJsonUtil.orderItemToJson(item);

        assertNotNull(json);
        assertTrue(json.contains("orderId"));
    }

    //List serialisation
    @Test
    void orderItemList_toJson_returnsJsonArray()
    {
        List<OrderItem> items = List.of(new OrderItem(1, 1, 1, 5, 10.0),
                                        new OrderItem(2, 1, 2, 10, 20.0));

        String json = OrderItemJsonUtil.orderItemListToJson(items);

        assertNotNull(json);
        assertTrue(json.startsWith("["));
    }
}
