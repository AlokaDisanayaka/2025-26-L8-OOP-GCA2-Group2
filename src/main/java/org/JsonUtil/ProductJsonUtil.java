package org.JsonUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.DTO.Product;

import java.util.List;


 // Utility class used to convert Product objects to and from JSON using Jackson.
/**
 * @author Aloka (primary)
 */
public class ProductJsonUtil {

    // ObjectMapper is the main Jackson class for JSON conversion
    private static final ObjectMapper mapper = new ObjectMapper();


     //Convert one Product object into JSON text.

    public static String productToJson(Product product)
    {
        try
        {
            return mapper.writeValueAsString(product);
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException("Error converting Product to JSON", e);
        }
    }


     // Convert JSON text into a Product object.

    public static Product productFromJson(String json)
    {
        try
        {
            return mapper.readValue(json, Product.class);
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException("Error converting JSON to Product", e);
        }
    }


     // Convert a list of Product objects into JSON text.

    public static String productListToJson(List<Product> products)
    {
        try
        {
            return mapper.writeValueAsString(products);
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException("Error converting Product list to JSON", e);
        }
    }
}
