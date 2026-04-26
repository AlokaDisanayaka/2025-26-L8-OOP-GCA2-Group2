package org.JsonUtil;

import org.DTO.Customer;
import org.junit.jupiter.api.Test;
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
}