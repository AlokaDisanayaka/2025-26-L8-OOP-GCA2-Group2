package org.JsonUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.DTO.Orders;

import java.util.List;

/**
 * @author Adhira Pillai (primary)
 **/

//Utility class used to convert Orders objects to and from JSON using Jackson
public class OrdersJsonUtil
{
    private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    //Convert one Orders object into JSON text
    public static String ordersToJson(Orders order)
    {
        try
        {
            return MAPPER.writeValueAsString(order);
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException("Error converting Orders to JSON", e);
        }
    }

    //Convert JSON text to an Orders object
    public static Orders ordersFromJson(String json)
    {
        try
        {
            return MAPPER.readValue(json, Orders.class);
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException("Error converting JSON to Orders", e);
        }
    }

    //Convert a list of Orders objects to JSON text
    public static String ordersListToJson(List<Orders> orders)
    {
        try
        {
            return MAPPER.writeValueAsString(orders);
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException("Error converting Orders list to JSON", e);
        }
    }
}
