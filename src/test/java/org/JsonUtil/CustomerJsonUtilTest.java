package org.JsonUtil;

import org.DTO.Customer;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerJsonUtilTest {

    @Test
    void customer_jsonRoundTrip_preservesAllFields() throws Exception {

        Customer original = new Customer(
                1,
                "JSON Customer",
                "json@email.com",
                "0872222222"
        );

        original.setFileName("test.txt");
        original.setContentType("text/plain");
        original.setFileSize(100);
        original.setFileData("test".getBytes());

        String json = CustomerJsonUtil.customerToJson(original);
        Customer result = CustomerJsonUtil.customerFromJson(json);

        assertEquals(original.getCustomerId(), result.getCustomerId());
        assertEquals(original.getName(), result.getName());
        assertEquals(original.getEmail(), result.getEmail());
        assertEquals(original.getPhone(), result.getPhone());

        assertEquals(original.getFileName(), result.getFileName());
        assertEquals(original.getContentType(), result.getContentType());
        assertEquals(original.getFileSize(), result.getFileSize());
        assertArrayEquals(original.getFileData(), result.getFileData());
    }

    // Tests that binary file fields are kept after converting to JSON and back
    @Test
    void customerJsonRoundTrip_preservesBinaryFields() throws Exception
    {
        Customer original = new Customer(
                2,
                "Binary Customer",
                "binary@email.com",
                "0873333333"
        );

        byte[] fileData = "Hello".getBytes();
        original.setFileName("hello.txt");
        original.setContentType("text/plain");
        original.setFileSize(fileData.length);
        original.setFileData(fileData);

        String json = CustomerJsonUtil.customerToJson(original);
        Customer result = CustomerJsonUtil.customerFromJson(json);

        assertEquals(original.getFileName(), result.getFileName());
        assertEquals(original.getContentType(), result.getContentType());
        assertEquals(original.getFileSize(), result.getFileSize());
        assertArrayEquals(original.getFileData(), result.getFileData());
    }

    // Tests that a list of customers is converted to JSON text
    @Test
    void customerListToJson_returnsValidJson() throws Exception
    {
        List<Customer> customers = List.of(
                new Customer(1, "First Customer", "first@email.com", "0871111111"),
                new Customer(2, "Second Customer", "second@email.com", "0872222222")
        );

        String json = CustomerJsonUtil.customerListToJson(customers);

        assertNotNull(json);
        assertTrue(json.contains("First Customer"));
        assertTrue(json.contains("Second Customer"));
    }

    // Tests that valid JSON text is converted into a Customer object
    @Test
    void customerFromJson_returnsCustomerObject_whenJsonValid() throws Exception
    {
        String json = "{\"customerId\":5,\"name\":\"Manual Customer\",\"email\":\"manual@email.com\",\"phone\":\"0875555555\"}";

        Customer customer = CustomerJsonUtil.customerFromJson(json);

        assertNotNull(customer);
        assertEquals(5, customer.getCustomerId());
        assertEquals("Manual Customer", customer.getName());
        assertEquals("manual@email.com", customer.getEmail());
        assertEquals("0875555555", customer.getPhone());
    }
}
