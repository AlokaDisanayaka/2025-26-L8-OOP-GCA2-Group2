package org.JsonUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.DTO.Customer;

import java.util.List;


  //Utility class used to convert Customer objects to and from JSON using Jackson.

/**
 * @author Aloka (primary)
 */
public class CustomerJsonUtil {

    // ObjectMapper is the main Jackson class for JSON conversion
    private static final ObjectMapper mapper = new ObjectMapper();


     //Convert one Customer object into JSON text.

    public static String customerToJson(Customer customer)
    {
        try
        {
            return mapper.writeValueAsString(customer);
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException("Error converting Customer to JSON", e);
        }
    }


     //Convert JSON text into a Customer object.

    public static Customer customerFromJson(String json)
    {
        try
        {
            return mapper.readValue(json, Customer.class);
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException("Error converting JSON to Customer", e);
        }
    }


     // Convert a list of Customer objects into JSON text.

    public static String customerListToJson(List<Customer> customers)
    {
        try
        {
            return mapper.writeValueAsString(customers);
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException("Error converting Customer list to JSON", e);
        }
    }
}
