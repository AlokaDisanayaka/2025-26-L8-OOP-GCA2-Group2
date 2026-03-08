package org.JSON;

import org.DTO.Customer;
import java.util.List;

//Handles JSON conversion for Customer objects
public class CustomerJson
{
    //Converts a Customer object to a JSON string
    public static String customerToJson(Customer customer)
    {
        String json = "{"
                + "\"customerId\":" + customer.getCustomerId() + ","
                + "\"name\":\"" + customer.getName() + "\","
                + "\"email\":\"" + customer.getEmail() + "\","
                + "\"phone\":\"" + customer.getPhone() + "\""
                + "}";

        return json;
    }

    //Converts a JSON string back into a Customer object
    public static Customer customerFromJson(String json)
    {
        //Remove the curly braces
        json = json.replace("{", "").replace("}", "");

        //Split into key value pairs
        String[] pairs = json.split(",");

        int customerId = 0;
        String name  = "";
        String email = "";
        String phone = "";

        for (int i = 0; i < pairs.length; i++)
        {
            String[] keyValue = pairs[i].split(":", 2);
            String key   = keyValue[0].replace("\"", "").trim();
            String value = keyValue[1].replace("\"", "").trim();

            if (key.equals("customerId"))
            {
                customerId = Integer.parseInt(value);
            }
            else if (key.equals("name"))
            {
                name = value;
            }
            else if (key.equals("email"))
            {
                email = value;
            }
            else if (key.equals("phone"))
            {
                phone = value;
            }
        }

        return new Customer(customerId, name, email, phone);
    }

    //Converts a list of Customer objects to a JSON array string
    public static String customerListToJson(List<Customer> customers)
    {
        StringBuilder sb = new StringBuilder("[");

        for (int i = 0; i < customers.size(); i++)
        {
            sb.append(customerToJson(customers.get(i)));

            if (i < customers.size() - 1)
            {
                sb.append(",");
            }
        }

        sb.append("]");
        return sb.toString();
    }
}