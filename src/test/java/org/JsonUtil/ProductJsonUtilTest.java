package org.JsonUtil;

import org.DTO.Product;
import org.junit.jupiter.api.Test;
import java.util.List;
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

    // Tests that binary file fields are kept after converting to JSON and back
    @Test
    void productJsonRoundTrip_preservesBinaryFields() throws Exception
    {
        Product original = new Product(
                2,
                "Binary Product",
                10.0,
                5,
                true
        );

        byte[] fileData = "Hello Product".getBytes();
        original.setFileName("hello-product.txt");
        original.setContentType("text/plain");
        original.setFileSize(fileData.length);
        original.setFileData(fileData);

        String json = ProductJsonUtil.productToJson(original);
        Product result = ProductJsonUtil.productFromJson(json);

        assertEquals(original.getFileName(), result.getFileName());
        assertEquals(original.getContentType(), result.getContentType());
        assertEquals(original.getFileSize(), result.getFileSize());
        assertArrayEquals(original.getFileData(), result.getFileData());
    }

    // Tests that a list of products is converted to JSON text
    @Test
    void productListToJson_returnsValidJson() throws Exception
    {
        List<Product> products = List.of(
                new Product(1, "First Product", 10.0, 5, true),
                new Product(2, "Second Product", 20.0, 10, false)
        );

        String json = ProductJsonUtil.productListToJson(products);

        assertNotNull(json);
        assertTrue(json.contains("First Product"));
        assertTrue(json.contains("Second Product"));
    }

    // Tests that valid JSON text is converted into a Product object
    @Test
    void productFromJson_returnsProductObject_whenJsonValid() throws Exception
    {
        String json = "{\"productId\":5,\"productName\":\"Manual Product\",\"unitPrice\":15.5,\"stockQty\":8,\"active\":true}";

        Product product = ProductJsonUtil.productFromJson(json);

        assertNotNull(product);
        assertEquals(5, product.getProductId());
        assertEquals("Manual Product", product.getProductName());
        assertEquals(15.5, product.getUnitPrice());
        assertEquals(8, product.getStockQty());
        assertTrue(product.isActive());
    }
}
