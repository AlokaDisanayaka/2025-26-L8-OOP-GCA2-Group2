package org.JsonUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.DTO.OrderItem;

import java.util.List;

//Utility class used to convert Orders objects to and from JSON using Jackson
public class OrderItemJsonUtil
{
    private static final ObjectMapper MAPPER = new ObjectMapper();

    //Convert one OrderItem object into JSON text
    public static String orderItemToJson(OrderItem item)
    {
        try
        {
            return MAPPER.writeValueAsString(item);
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException("Error converting OrderItem to JSON", e);
        }
    }

    //Convert JSON text to an OrderItem object
    public static OrderItem orderItemFromJson(String json)
    {
        try
        {
            return MAPPER.readValue(json, OrderItem.class);
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException("Error converting JSON to OrderItem", e);
        }
    }

    //Convert list of OrderItem objects to JSON text
    public static String orderItemListToJson(List<OrderItem> items)
    {
        try
        {
            return MAPPER.writeValueAsString(items);
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException("Error converting OrderItem list to JSON", e);
        }
    }
}
