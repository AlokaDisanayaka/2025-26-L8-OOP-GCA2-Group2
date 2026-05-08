package org.DAO_JDBC;

import org.DTO.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;


//Tests for Product DAO

public class JdbcProductDaoTest {

    private JdbcProductDao dao;

    @BeforeEach
    void setUp() {
        dao = new JdbcProductDao();
    }

    // READ
    @Test
    void getProductById_returnsEmptyOptional_whenIdDoesNotExist()
    {
        Optional<Product> result = dao.getProductById(-99);
        assertTrue(result.isEmpty());
    }

    @Test
    void getProductById_returnsProduct_whenIdExists()
    {
        Product inserted = dao.insertProduct(new Product("Test Product", 10.0, 5, true));

        Optional<Product> result = dao.getProductById(inserted.getProductId());

        assertTrue(result.isPresent());
        assertEquals("Test Product", result.get().getProductName());
    }

    //INSERT
    @Test
    void insertProduct_returnsGeneratedId_whenSuccessful() {
        Product product = new Product("Insert Product", 20.0, 10, true);

        Product inserted = dao.insertProduct(product);

        assertNotNull(inserted);
        assertTrue(inserted.getProductId() > 0);
    }

    // Tests that all products are returned when products exist
    @Test
    void getAllProducts_returnsList_whenProductsExist()
    {
        List<Product> products = dao.getAllProducts();

        assertNotNull(products);
        assertTrue(products.size() > 0);
    }

    // Tests that a product can be updated and retrieved with new values
    @Test
    void updateProduct_returnsUpdatedProduct_whenSuccessful()
    {
        String productName = "Update Product " + System.nanoTime();
        String updatedProductName = "Updated Product " + System.nanoTime();

        Product inserted = dao.insertProduct(new Product(productName, 30.0, 15, true));

        Product updatedProduct = new Product(updatedProductName, 40.0, 20, false);
        dao.updateProduct(inserted.getProductId(), updatedProduct);

        Optional<Product> result = dao.getProductById(inserted.getProductId());

        assertTrue(result.isPresent());
        assertEquals(updatedProductName, result.get().getProductName());
        assertEquals(40.0, result.get().getUnitPrice());
        assertEquals(20, result.get().getStockQty());
        assertFalse(result.get().isActive());
    }

    // Tests that deleting an existing product returns true
    @Test
    void deleteProduct_returnsTrue_whenProductExists()
    {
        String productName = "Delete Product " + System.nanoTime();

        Product inserted = dao.insertProduct(new Product(productName, 50.0, 25, true));

        boolean deleted = dao.deleteProductById(inserted.getProductId());

        assertTrue(deleted);
    }

    // Tests that filtering products returns matching products
    @Test
    void filterProducts_returnsMatchingProducts()
    {
        String productName = "Filter Product " + System.nanoTime();

        Product inserted = dao.insertProduct(new Product(productName, 60.0, 30, true));

        List<Product> products = dao.findProductsByFilter(product ->
                product.getProductName().equals(inserted.getProductName()));

        assertNotNull(products);
        assertFalse(products.isEmpty());
    }

    // Tests that uploaded product file bytes can be retrieved
    @Test
    void uploadProductFileAndRetrieve_returnsMatchingBytes()
    {
        byte[] fileData = "Hello Product".getBytes();
        String productName = "File Product " + System.nanoTime();

        Product inserted = dao.insertProduct(new Product(productName, 70.0, 35, true));

        Product productFile = new Product(productName, 70.0, 35, true);
        productFile.setFileName("hello-product.txt");
        productFile.setContentType("text/plain");
        productFile.setFileSize(fileData.length);
        productFile.setFileData(fileData);

        dao.uploadProductFile(inserted.getProductId(), productFile);

        Optional<Product> result = dao.getProductFileById(inserted.getProductId());

        assertTrue(result.isPresent());
        assertArrayEquals(fileData, result.get().getFileData());
    }
}
