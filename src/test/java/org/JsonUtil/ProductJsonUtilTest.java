package org.JsonUtil;

import org.DTO.Product;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductJsonUtilTest {

    @Test
    void product_jsonRoundTrip_preservesAllFields() throws Exception {

        Product original = new Product(
                1,
                "JSON Product",
                99.99,
                5,
                true
        );

        original.setFileName("test.txt");
        original.setContentType("text/plain");
        original.setFileSize(100);
        original.setFileData("test".getBytes());

        String json = ProductJsonUtil.productToJson(original);
        Product result = ProductJsonUtil.productFromJson(json);

        assertEquals(original.getProductId(), result.getProductId());
        assertEquals(original.getProductName(), result.getProductName());
        assertEquals(original.getUnitPrice(), result.getUnitPrice());
        assertEquals(original.getStockQty(), result.getStockQty());

        assertEquals(original.getFileName(), result.getFileName());
        assertEquals(original.getContentType(), result.getContentType());
        assertEquals(original.getFileSize(), result.getFileSize());
        assertArrayEquals(original.getFileData(), result.getFileData());
    }
}