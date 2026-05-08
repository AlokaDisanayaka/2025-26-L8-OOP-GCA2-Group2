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
 *
 * @author Adhira Pillai (primary)
 */

public class OrderItemJsonUtilTest
{
    //tests full JSON round trip conversion preserves all order item fields correctly
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

    //tests that serialising a single order item produces valid JSON output containing expected fields
    @Test
    void orderItem_toJson_returnsValidJson()
    {
        OrderItem item = new OrderItem(1, 1, 1, 10, 5.0);

        String json = OrderItemJsonUtil.orderItemToJson(item);

        assertNotNull(json);
        assertTrue(json.contains("orderId"));
    }

    //tests that a list of order item objects is correctly converted into a JSON array
    @Test
    void orderItemList_toJson_returnsJsonArray()
    {
        List<OrderItem> items = List.of(new OrderItem(1, 1, 1, 5, 10.0),
                                        new OrderItem(2, 1, 2, 10, 20.0));

        String json = OrderItemJsonUtil.orderItemListToJson(items);

        assertNotNull(json);
        assertTrue(json.startsWith("["));
    }

    //tests that JSON serialisation and deserialisation correctly handles null file data
    @Test
    void orderItem_json_handlesNullFileData()
    {
        OrderItem item = new OrderItem(1, 1, 2, 10.0);
        item.setFileData(null);

        String json = OrderItemJsonUtil.orderItemToJson(item);
        OrderItem result = OrderItemJsonUtil.orderItemFromJson(json);

        assertNull(result.getFileData());
    }

    //tests that serialising an empty list of order items returns an empty JSON array
    @Test
    void orderItemList_toJson_emptyList_returnsEmptyArray()
    {
        String json = OrderItemJsonUtil.orderItemListToJson(List.of());
        assertEquals("[]", json);
    }
}
